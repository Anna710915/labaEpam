package com.epam.esm.service.impl;

import com.epam.esm.exception.CustomNotFoundException;
import com.epam.esm.exception.CustomNotValidArgumentException;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.MessageLanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Certificate service implements methods of the CertificateService
 * interface. The class is annotated as a service, which qualifies it to be
 * automatically created by component-scanning.
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
        Optional<GiftCertificate> uniqCertificate = certificateRepository.showByName(certificateDto.getName());
        checkUniqueCertificateName(uniqCertificate);
        GiftCertificate giftCertificate = new GiftCertificate(certificateDto.getName(),
                certificateDto.getDescription(), certificateDto.getPrice(),
                certificateDto.getDuration(), localDateTime, localDateTime);
        giftCertificate.setTags(certificateDto.getTags());
        return certificateRepository.create(giftCertificate);
    }

    @Override
    @Transactional
    public List<CertificateDto> showAll(int limit, int offset) {
        List<GiftCertificate> giftCertificateList = certificateRepository.show(limit, offset);
        return certificateDtoListBuilder(giftCertificateList);
    }

    @Override
    @Transactional
    public CertificateDto showCertificateWithTags(long id) {
        GiftCertificate giftCertificate = certificateRepository.showById(id);
        return initCertificateDto(giftCertificate);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(CertificateDto certificateDto, long id) {
        GiftCertificate giftCertificate = certificateRepository.showById(id);
        Optional<GiftCertificate> uniqCertificate = certificateRepository.showByName(certificateDto.getName());
        checkUpdateUniqueCertificateName(uniqCertificate, id);
        if(updateGiftCertificateData(giftCertificate, certificateDto)) {
            giftCertificate.setLastUpdateDate(LocalDateTime.now());
        }
        giftCertificate.setTags(certificateDto.getTags());
        certificateRepository.update(giftCertificate);
    }

    @Override
    public void updateCertificateDuration(long certificateId, int duration) {
        checkPositiveDuration(duration);
        certificateRepository.updateDuration(certificateId, duration);
    }

    @Override
    public void updateCertificatePrice(long certificateId, BigDecimal price) {
        checkPositivePrice(price);
        certificateRepository.updatePrice(certificateId, price);
    }

    @Override
    @Transactional
    public List<CertificateDto> showByTagName(int limit, int offset, String tagName) {
        List<GiftCertificate> giftCertificateList = certificateRepository.showByTagName(limit, offset, tagName);
        return certificateDtoListBuilder(giftCertificateList);
    }

    @Override
    @Transactional
    public List<CertificateDto> showByPartWord(int limit, int offset, String partWord) {
        List<GiftCertificate> listByPartName = certificateRepository.showByPartNameOrDescription(limit, offset, partWord);
        return certificateDtoListBuilder(listByPartName);
    }

    @Override
    @Transactional
    public List<CertificateDto> sortByName(int limit, int offset) {
        List<GiftCertificate> giftCertificateList = certificateRepository.sortByNameAsc(limit, offset);
        return certificateDtoListBuilder(giftCertificateList);
    }

    @Override
    @Transactional
    public List<CertificateDto> sortByDate(int limit, int offset) {
        List<GiftCertificate> giftCertificateList = certificateRepository.sortByDateAsc(limit, offset);
        return certificateDtoListBuilder(giftCertificateList);
    }

    @Override
    @Transactional
    public List<CertificateDto> bothSort(int limit, int offset) {
        List<GiftCertificate> giftCertificateList = certificateRepository.bothSorting(limit, offset);
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
        return certificateRepository.findCountByPartNameOrDescription(part);
    }

    @Override
    public int findCountCertificatesByTags(String query) {
        String[] tags = query.split(REGEX_COMMA);
        List<String> tagsName = filterExistingTags(tags);
        return !tagsName.isEmpty() ? certificateRepository.findCountByTagsQuery(tagsName) : 0;
    }

    @Override
    public boolean deleteCertificate(long id) {
        return certificateRepository.delete(id);
    }


    private List<String> filterExistingTags(String[] tags){
        return Arrays.stream(tags)
                .filter(tag -> tagRepository.showByName(tag).isPresent())
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

    private void checkUniqueCertificateName(Optional<GiftCertificate> uniqCertificate){
        if(uniqCertificate.isPresent()){
            throw new CustomNotFoundException(messageLanguageUtil.getMessage("not_valid.not_uniq_certificate") + uniqCertificate.get().getName());
        }
    }

    private void checkUpdateUniqueCertificateName(Optional<GiftCertificate> uniqCertificate, long updateId){
        if(uniqCertificate.isPresent() && uniqCertificate.get().getId() != updateId){
            throw new CustomNotFoundException(messageLanguageUtil.getMessage("not_valid.not_uniq_certificate") + uniqCertificate.get().getName());
        }
    }

    private void checkPositiveDuration(int duration){
        if(duration < 0) {
            throw new CustomNotValidArgumentException(messageLanguageUtil.getMessage("not_valid.duration") + duration);
        }
    }

    private void checkPositivePrice(BigDecimal price){
        if(price.compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomNotValidArgumentException(messageLanguageUtil.getMessage("not_valid.price") + price);
        }
    }
}
