package com.epam.esm.service.impl;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Certificate service implements methods of the CertificateService
 * interface. The class is annotated as a service, which qualifies it to be
 * automatically created by component-scanning.
 * @author Anna Merkul
 */
@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;

    /**
     * Instantiates a new Certificate service.
     *
     * @param certificateRepository the certificate repository
     * @param tagRepository         the tag repository
     */
    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository,
                                  TagRepository tagRepository){
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long create(CertificateDto certificateDto) {
        LocalDateTime localDateTime = LocalDateTime.now();
        GiftCertificate uniqCertificate = certificateRepository.showByName(certificateDto.getName());
        if(uniqCertificate != null){
            throw new IllegalArgumentException("Certificate name is not uniq " + certificateDto.getName());
        }
        GiftCertificate giftCertificate = new GiftCertificate(certificateDto.getName(),
                certificateDto.getDescription(), certificateDto.getPrice(),
                certificateDto.getDuration(), localDateTime, localDateTime);
        Set<Tag> tagSet = certificateDto.getTagSet();
        long certificateId = certificateRepository.create(giftCertificate);
        if(certificateId < 0){
            return certificateId;
        }
        createTagsAndInsertKeys(tagSet, certificateId);
        return certificateId;
    }

    @Override
    public List<CertificateDto> showAll() {
        List<GiftCertificate> giftCertificateList = certificateRepository.show();
        return certificateDtoListBuilder(giftCertificateList);
    }

    @Override
    public CertificateDto showCertificateWithTags(long id) {
        GiftCertificate giftCertificate = certificateRepository.showById(id);
        if(giftCertificate == null){
            return null;
        }
        Set<Tag> tagSet = tagRepository.showByCertificateId(id);
        CertificateDto certificateDto = new CertificateDto();
        certificateDto.setId(id);
        certificateDto.setName(giftCertificate.getName());
        certificateDto.setDescription(giftCertificate.getDescription());
        certificateDto.setPrice(giftCertificate.getPrice());
        certificateDto.setDuration(giftCertificate.getDuration());
        certificateDto.setCreateDate(giftCertificate.getCreateDate());
        certificateDto.setLastUpdateDate(giftCertificate.getLastUpdateDate());
        certificateDto.setTagSet(tagSet);
        return certificateDto;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(CertificateDto certificateDto, long id) {
        GiftCertificate giftCertificate = certificateRepository.showById(id);
        if(giftCertificate == null){
            return false;
        }
        GiftCertificate uniqCertificate = certificateRepository.showByName(certificateDto.getName());
        if(uniqCertificate != null && uniqCertificate.getId() != id){
            throw new IllegalArgumentException("Certificate name is not uniq " + certificateDto.getName());
        }
        Set<Tag> oldTagSet = tagRepository.showByCertificateId(id);
        Set<Tag> updateTagSet = certificateDto.getTagSet();
        boolean isDelete = deleteTagsForCertificate(oldTagSet, updateTagSet, id);
        Set<Tag> insertTags = collectNewTags(oldTagSet, updateTagSet);
        if(updateData(giftCertificate, certificateDto) || !insertTags.isEmpty() || isDelete){
            giftCertificate.setLastUpdateDate(LocalDateTime.now());
        }
        createTagsAndInsertKeys(insertTags, giftCertificate.getId());
        return certificateRepository.update(id, giftCertificate);
    }


    @Override
    public List<CertificateDto> showByTagName(String tagName) {
        List<GiftCertificate> giftCertificateList = certificateRepository.showByTagName(tagName);
        return certificateDtoListBuilder(giftCertificateList);
    }

    @Override
    public List<CertificateDto> showByPartWord(String partWord) {
        List<GiftCertificate> listByPartName = certificateRepository.showByPartNameOrDescription(partWord);
        return certificateDtoListBuilder(listByPartName);
    }

    @Override
    public List<CertificateDto> sortByName() {
        List<GiftCertificate> giftCertificateList = certificateRepository.sortByNameAsc();
        return certificateDtoListBuilder(giftCertificateList);
    }

    @Override
    public List<CertificateDto> sortByDate() {
        List<GiftCertificate> giftCertificateList = certificateRepository.sortByDateAsc();
        return certificateDtoListBuilder(giftCertificateList);
    }

    @Override
    public List<CertificateDto> bothSort() {
        List<GiftCertificate> giftCertificateList = certificateRepository.bothSorting();
        return certificateDtoListBuilder(giftCertificateList);
    }

    @Override
    public boolean deleteCertificate(long id) {
        return certificateRepository.delete(id);
    }

    private boolean updateData(GiftCertificate giftCertificate, CertificateDto certificateDto){
        boolean modify = false;
        if(!giftCertificate.getName().equals(certificateDto.getName())){
            giftCertificate.setName(certificateDto.getName());
            modify = true;
        }
        if(!giftCertificate.getDescription().equals(certificateDto.getDescription())){
            giftCertificate.setDescription(certificateDto.getDescription());
            modify = true;
        }
        if(!giftCertificate.getPrice().equals(certificateDto.getPrice())){
            giftCertificate.setPrice(certificateDto.getPrice());
            modify = true;
        }
        if(giftCertificate.getDuration() != certificateDto.getDuration()){
            giftCertificate.setDuration(certificateDto.getDuration());
            modify = true;
        }
        return modify;
    }

    private boolean deleteTagsForCertificate(Set<Tag> oldTags, Set<Tag> updateTags, long certificateId){
        boolean isDelete = false;
        Set<Tag> deleteTagsKey = oldTags.stream()
                .filter(tag -> !updateTags.contains(tag))
                .collect(Collectors.toSet());
        for(Tag tag : deleteTagsKey){
            isDelete = true;
            certificateRepository.deleteKeys(tag.getId(), certificateId);
        }
        return isDelete;
    }

    private Set<Tag> collectNewTags(Set<Tag> oldTags, Set<Tag> updateTags){
        return updateTags.stream()
                .filter(tag -> !oldTags.contains(tag))
                .collect(Collectors.toSet());
    }

    private void createTagsAndInsertKeys(Set<Tag> tagSet, long certificateId){
        for(Tag tag: tagSet){
            if(tagRepository.showById(tag.getId()) == null){
                long tagId = tagRepository.create(tag);
                if(tagId < 0){
                    throw new IllegalArgumentException("Tag is not created");
                }
                certificateRepository.insertKeys(tagId, certificateId);
            }else{
                certificateRepository.insertKeys(tag.getId(), certificateId);
            }
        }
    }

    private List<CertificateDto> certificateDtoListBuilder(List<GiftCertificate> giftCertificateList){
        List<CertificateDto> certificateDtoList = new ArrayList<>();
        for (GiftCertificate giftCertificate : giftCertificateList){
            CertificateDto certificateDto = new CertificateDto();
            certificateDto.setId(giftCertificate.getId());
            certificateDto.setName(giftCertificate.getName());
            certificateDto.setPrice(giftCertificate.getPrice());
            long certificateId = giftCertificate.getId();
            certificateDto.setTagSet(tagRepository.showByCertificateId(certificateId));
            certificateDtoList.add(certificateDto);
        }
        return certificateDtoList;
    }
}
