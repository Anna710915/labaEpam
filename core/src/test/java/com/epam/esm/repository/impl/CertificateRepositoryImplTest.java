package com.epam.esm.repository.impl;

import com.epam.esm.config.DevelopmentConfig;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.repository.CertificateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DevelopmentConfig.class)
@ActiveProfiles("dev")
class CertificateRepositoryImplTest {

    @Autowired
    private CertificateRepository certificateRepository;

    @Test
    void create() {
        long actual = certificateRepository.create(new GiftCertificate("Svarovski","nkdcdnc", BigDecimal.valueOf(150), 35, LocalDateTime.of(2021,6,17,16,11,17),
                LocalDateTime.of(2022,1,17,16,15,17)));
        long expected = 3;
        assertEquals(expected, actual);
    }

    @Test
    void show() {
        List<GiftCertificate> certificateList = certificateRepository.show(1,0);
        int expected = 1;
        int actual = certificateList.size();
        assertEquals(expected, actual);
    }

    @Test
    void showNothing(){
        List<GiftCertificate> certificateList = certificateRepository.show(1,4);
        int expected = 0;
        int actual = certificateList.size();
        assertEquals(expected, actual);
    }

    @Test
    void countCertificates(){
        int actual = certificateRepository.findCountRecords();
        int expected = 2;
        assertEquals(expected, actual);
    }

    @Test
    void showById() {
        GiftCertificate giftCertificate = certificateRepository.showById(2);
        assertNotNull(giftCertificate);
    }

    @Test
    void update() {
        GiftCertificate giftCertificate = certificateRepository.showById(2);
        giftCertificate.setPrice(BigDecimal.valueOf(100));
        certificateRepository.update(giftCertificate);
        int expected = 100;
        int actual = certificateRepository.showById(2).getPrice().intValue();
        assertEquals(expected, actual);
    }

    @Test
    void delete() {
        assertTrue(certificateRepository.delete(2));
    }

    @Test
    void deleteFalse() {
        assertFalse(certificateRepository.delete(4));
    }

    @Test
    void showByTagName() {
        List<GiftCertificate> giftCertificateList = certificateRepository.showByTagName(2, 0, "#like");
        int expected = 2;
        int actual = giftCertificateList.size();
        assertEquals(expected, actual);
    }

    @Test
    void showByCertificateName() {
        Optional<GiftCertificate> giftCertificate = certificateRepository.showByName("ZIKO");
        assertTrue(giftCertificate.isPresent());
    }

    @Test
    void showByCertificateNameNull() {
        Optional<GiftCertificate> giftCertificate = certificateRepository.showByName("ZIKOjjjjjjj");
        assertTrue(giftCertificate.isEmpty());
    }

    @Test
    void showByTagNameZeroSize(){
        List<GiftCertificate> giftCertificateList = certificateRepository.showByTagName(2, 0, "aaaaaaaaa");
        int expected = 0;
        int actual = giftCertificateList.size();
        assertEquals(expected, actual);
    }

    @Test
    void showByPartNameOrDescription() {
        List<GiftCertificate> giftCertificateList = certificateRepository.showByPartNameOrDescription(2, 0, "IK");
        String expected = "ZIKO";
        String actual = giftCertificateList.get(0).getName();
        assertEquals(expected, actual);
    }

    @Test
    void findCountByTagName(){
        int actual = certificateRepository.findCountByTagName("#like");
        int expected = 2;
        assertEquals(expected, actual);
    }

    @Test
    void findCountByTagNameZero(){
        int actual = certificateRepository.findCountByTagName("#likedddddddddd");
        int expected = 0;
        assertEquals(expected, actual);
    }

    @Test
    void findByTags(){
        List<String> tags = new ArrayList<>();
        tags.add("#like");
        List<GiftCertificate> certificates = certificateRepository.findByTags(2, 0, tags);
        int actual = certificates.size();
        int expected = 2;
        assertEquals(expected, actual);
    }

    @Test
    void findCountByTags(){
        List<String> tags = new ArrayList<>();
        tags.add("#like");
        int actual = certificateRepository.findCountByTagsQuery(tags);
        int expected = 2;
        assertEquals(expected, actual);
    }

    @Test
    void sortByDateAsc() {
        List<GiftCertificate> giftCertificates = certificateRepository.sortByDateAsc(2,0);
        long expected = 1L;
        long actual = giftCertificates.get(0).getId();
        assertEquals(expected, actual);
    }


    @Test
    void sortByNameAsc() {
        long expected = 2L;
        List<GiftCertificate> certificates = certificateRepository.sortByNameAsc(2, 0);
        long actual = certificates.get(0).getId();
        assertEquals(expected, actual);
    }


    @Test
    void bothSorting(){
        long expected = 2L;
        List<GiftCertificate> certificates = certificateRepository.bothSorting(2, 0);
        long actual = certificates.get(0).getId();
        assertEquals(expected, actual);
    }

    @Test
    void updateDuration(){
        certificateRepository.updateDuration(1, 65);
        GiftCertificate giftCertificate = certificateRepository.showById(1);
        int actual = giftCertificate.getDuration();
        int expected = 65;
        assertEquals(expected, actual);
    }

    @Test
    void updatePrice(){
        certificateRepository.updatePrice(1, BigDecimal.valueOf(65));
        GiftCertificate giftCertificate = certificateRepository.showById(1);
        BigDecimal actual = giftCertificate.getPrice();
        BigDecimal expected = BigDecimal.valueOf(65.00);
        assertEquals(expected.intValue(), actual.intValue());
    }
}