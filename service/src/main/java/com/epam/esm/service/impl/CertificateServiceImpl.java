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
import java.util.stream.Stream;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;

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
        GiftCertificate giftCertificate = new GiftCertificate(certificateDto.getName(),
                certificateDto.getDescription(), certificateDto.getPrice(),
                certificateDto.getDuration(), localDateTime, localDateTime);
        Set<Tag> tagSet = certificateDto.getTagSet();
        long certificateId = certificateRepository.create(giftCertificate);
        if(certificateId < 0){
            return certificateId;
        }
        for(Tag tag: tagSet){
            if(tagRepository.showById(tag.getId()) == null){
                long tagId = tagRepository.create(tag);
                if(tagId < 0){
                    throw new IllegalArgumentException("Tag is not created");
                }
                certificateRepository.insertKeys(tagId, certificateId);
            }else {
                certificateRepository.insertKeys(tag.getId(), certificateId);
            }
        }
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
    public  CertificateDto update(CertificateDto certificateDto, long id) {
        GiftCertificate giftCertificate = certificateRepository.showById(id);
        if(giftCertificate == null){
            return null;
        }
        boolean modify = updateData(giftCertificate, certificateDto);
        if(modify){
            giftCertificate.setLastUpdateDate(LocalDateTime.now());
        }
        certificateRepository.update(id, giftCertificate);
        Set<Tag> tagSet = certificateDto.getTagSet();
        for(Tag tag: tagSet){
            if(tagRepository.showById(tag.getId()) == null){
                long tagId = tagRepository.create(tag);
                if(tagId < 0){
                    throw new IllegalArgumentException("Tag is not created");
                }
                certificateRepository.insertKeys(tagId, giftCertificate.getId());
            }
        }
        return new CertificateDto(giftCertificate.getId(), giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(),
                giftCertificate.getDuration(), giftCertificate.getCreateDate(), giftCertificate.getLastUpdateDate(), tagSet);
    }


    @Override
    public List<CertificateDto> showByTagName(String tagName) {
        List<GiftCertificate> giftCertificateList = certificateRepository.showByTagName(tagName);
        return certificateDtoListBuilder(giftCertificateList);
    }

    @Override
    public List<CertificateDto> showByPartWord(String partWord) {
        List<GiftCertificate> listByPartName = certificateRepository.showByPartName(partWord);
        List<GiftCertificate> listByPartDescription = certificateRepository.showByPartDescription(partWord);
        List<GiftCertificate> listCommon = Stream.of(listByPartName, listByPartDescription)
                .flatMap(List :: stream)
                .collect(Collectors.toList());
        return certificateDtoListBuilder(listCommon);
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
