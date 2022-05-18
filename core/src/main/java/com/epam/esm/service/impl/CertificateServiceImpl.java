package com.epam.esm.service.impl;

import com.epam.esm.exception.CustomNotFoundException;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.MessageLanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Certificate service implements methods of the CertificateService
 * interface. The class is annotated as a service, which qualifies it to be
 * automatically created by component-scanning.
 *
 * @author Anna Merkul
 */
@Service
public class CertificateServiceImpl implements CertificateService {

    private static final String REGEX_COMMA = ",";
    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final MessageLanguageUtil messageLanguageUtil;

    /**
     * Instantiates a new Certificate service.
     *
     * @param certificateRepository the certificate repository
     * @param tagRepository         the tag repository
     * @param messageLanguageUtil   the message language util
     */
    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository,
                                  TagRepository tagRepository,
                                  MessageLanguageUtil messageLanguageUtil){
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.messageLanguageUtil = messageLanguageUtil;
    }

    @Override
    public int findCountCertificateRecords() {
        return certificateRepository.findCountRecords();
    }

    @Override
    @Transactional
    public long create(CertificateDto certificateDto) {
        LocalDateTime localDateTime = LocalDateTime.now();
        GiftCertificate uniqCertificate = certificateRepository.findGiftCertificateByName(certificateDto.getName());
        checkUniqueCertificateName(uniqCertificate);
        GiftCertificate giftCertificate = new GiftCertificate(certificateDto.getName(),
                certificateDto.getDescription(), certificateDto.getPrice(),
                certificateDto.getDuration(), localDateTime, localDateTime);
        List<Tag> tags = tagRepository.saveAll(certificateDto.getTags());
        giftCertificate.setTags(tags);
        return certificateRepository.save(giftCertificate).getId();
    }

    @Override
    @Transactional
    public List<CertificateDto> showAll(int page, int size) {
        List<GiftCertificate> giftCertificateList = certificateRepository
                .findAll(PageRequest.of(page - 1, size)).getContent();
        return certificateDtoListBuilder(giftCertificateList);
    }

    @Override
    @Transactional
    public CertificateDto showCertificateWithTags(long id) {
        GiftCertificate giftCertificate = certificateRepository.findGiftCertificateById(id);
        checkCertificate(giftCertificate, id);
        return initCertificateDto(giftCertificate);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(CertificateDto certificateDto, long id) {
        GiftCertificate giftCertificate = certificateRepository.findGiftCertificateById(id);
        GiftCertificate uniqCertificate = certificateRepository.findGiftCertificateByName(certificateDto.getName());
        checkUpdateUniqueCertificateName(uniqCertificate, id);
        if(updateGiftCertificateData(giftCertificate, certificateDto)) {
            giftCertificate.setLastUpdateDate(LocalDateTime.now());
        }
        List<Tag> tags = tagRepository.saveAll(certificateDto.getTags());
        giftCertificate.setTags(tags);
        certificateRepository.save(giftCertificate);
    }

    @Override
    @Transactional
    public void updateCertificateDuration(long certificateId, int duration) {
        certificateRepository.updateDuration(certificateId, duration);
    }

    @Override
    @Transactional
    public void updateCertificatePrice(long certificateId, BigDecimal price) {
        certificateRepository.updatePrice(certificateId, price);
    }

    @Override
    @Transactional
    public List<CertificateDto> showByTagName(int page, int size, String tagName) {
        List<GiftCertificate> giftCertificateList = certificateRepository.showByTagName(tagName, PageRequest.of(page - 1, size));
        return certificateDtoListBuilder(giftCertificateList);
    }

    @Override
    @Transactional
    public List<CertificateDto> showByPartWord(int page, int size, String partWord) {
        List<GiftCertificate> listByPartName = certificateRepository.findGiftCertificatesByPartNameOrDescription(partWord, PageRequest.of(page - 1, size));
        return certificateDtoListBuilder(listByPartName);
    }

    @Override
    @Transactional
    public List<CertificateDto> sortByName(int page, int size) {
        List<GiftCertificate> giftCertificateList = certificateRepository
                .findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "name"))).getContent();
        return certificateDtoListBuilder(giftCertificateList);
    }

    @Override
    @Transactional
    public List<CertificateDto> sortByDate(int page, int size) {
        List<GiftCertificate> giftCertificateList = certificateRepository
                .findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "lastUpdateDate"))).getContent();
        return certificateDtoListBuilder(giftCertificateList);
    }

    @Override
    @Transactional
    public List<CertificateDto> bothSort(int page, int size) {
        List<GiftCertificate> giftCertificateList = certificateRepository
                .findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "name", "lastUpdateDate"))).getContent();
        return certificateDtoListBuilder(giftCertificateList);
    }

    @Override
    @Transactional
    public List<CertificateDto> findCertificatesByQuery(int limit, int offset, String query) {
        String[] tags = query.split(REGEX_COMMA);
        List<String> filterTags = filterExistingTags(tags);
        List<GiftCertificate> giftCertificates = !filterTags.isEmpty() ?
                certificateRepository.findByTags(limit, offset, filterTags) : new ArrayList<>();
        return certificateDtoListBuilder(giftCertificates);
    }

    @Override
    public int findCountCertificatesByTagName(String tagName) {
        return certificateRepository.findCountByTagName(tagName);
    }

    @Override
    public int findCountByPartNameOrDescription(String part) {
        return certificateRepository.countGiftCertificateByPartNameOrDescription(part);
    }

    @Override
    public int findCountCertificatesByTags(String query) {
        String[] tags = query.split(REGEX_COMMA);
        List<String> tagsName = filterExistingTags(tags);
        return !tagsName.isEmpty() ? certificateRepository.findCountByTagsQuery(tagsName) : 0;
    }

    @Override
    @Transactional
    public boolean deleteCertificate(long id) {
        return certificateRepository.deleteGiftCertificateById(id) == 1;
    }


    private List<String> filterExistingTags(String[] tags){
        return Arrays.stream(tags)
                .filter(tag -> tagRepository.findTagByName(tag) != null)
                .collect(Collectors.toList());
    }

    private boolean updateGiftCertificateData(GiftCertificate giftCertificate, CertificateDto certificateDto){
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
            CertificateDto certificateDto = initCertificateDto(giftCertificate);
            certificateDtoList.add(certificateDto);
        }
        return certificateDtoList;
    }

    private CertificateDto initCertificateDto(GiftCertificate giftCertificate){
        CertificateDto certificateDto = new CertificateDto();
        certificateDto.setId(giftCertificate.getId());
        certificateDto.setName(giftCertificate.getName());
        certificateDto.setDescription(giftCertificate.getDescription());
        certificateDto.setPrice(giftCertificate.getPrice());
        certificateDto.setDuration(giftCertificate.getDuration());
        certificateDto.setCreateDate(giftCertificate.getCreateDate());
        certificateDto.setLastUpdateDate(giftCertificate.getLastUpdateDate());
        certificateDto.setTags(giftCertificate.getTags());
        return certificateDto;
    }

    private void checkUniqueCertificateName(GiftCertificate uniqCertificate){
        if(uniqCertificate != null){
            throw new CustomNotFoundException(messageLanguageUtil.getMessage("not_valid.not_uniq_certificate") + uniqCertificate.getName());
        }
    }

    private void checkUpdateUniqueCertificateName(GiftCertificate uniqCertificate, long updateId){
        if(uniqCertificate != null && uniqCertificate.getId() != updateId){
            throw new CustomNotFoundException(messageLanguageUtil.getMessage("not_valid.not_uniq_certificate") + uniqCertificate.getName());
        }
    }

    private void checkCertificate(GiftCertificate giftCertificate, long id){
        if(giftCertificate == null){
            throw new CustomNotFoundException(messageLanguageUtil.getMessage("not_found.certificate") + id);
        }
    }
}
