package com.epam.esm.repository.impl;

import com.epam.esm.config.DevelopmentProfileConfig;
import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.repository.CertificateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = DevelopmentProfileConfig.class)
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
        List<GiftCertificate> certificateList = certificateRepository.show();
        int expected = 2;
        int actual = certificateList.size();
        assertEquals(expected, actual);
    }

    @Test
    void showById() {
        GiftCertificate giftCertificate = certificateRepository.showById(2);
        assertNotNull(giftCertificate);
    }

    @Test
    void update() {
        GiftCertificate giftCertificate = certificateRepository.showById(1);
        giftCertificate.setPrice(BigDecimal.valueOf(100));
        certificateRepository.update(1, giftCertificate);
        int expected = 100;
        int actual = certificateRepository.showById(1).getPrice().intValue();
        assertEquals(expected, actual);
    }

    @Test
    void delete() {
        certificateRepository.delete(2);
        int expected = 1;
        int actual = certificateRepository.show().size();
        assertEquals(expected, actual);
    }

    @Test
    void showByTagName() {
        List<GiftCertificate> giftCertificateList = certificateRepository.showByTagName("#like");
        int expected = 2;
        int actual = giftCertificateList.size();
        assertEquals(expected, actual);
    }

    @Test
    void showByPartName() {
        List<GiftCertificate> giftCertificateList = certificateRepository.showByPartName("IK");
        String expected = "ZIKO";
        String actual = giftCertificateList.get(0).getName();
        assertEquals(expected, actual);
    }

    @Test
    void showByPartDescription() {
        List<GiftCertificate> giftCertificateList = certificateRepository.showByPartDescription("dcd");
        int expected = 1;
        int actual = giftCertificateList.size();
        assertEquals(expected, actual);
    }

    @Test
    void sortByDateAsc() {
        String expected = "[GiftCertificate{id=1, name='ZIKO', description='nkdcdnc', price=150.00, duration=35, " +
                "createDate=2022-01-01T15:40:01, lastUpdateDate=2022-03-01T14:37:33}, GiftCertificate{id=2, name='Mila', " +
                "description='dskckjn', price=50.00, duration=30, createDate=2022-02-01T17:40:01, lastUpdateDate=2022-04-01T10:37:33}]";
        String actual = certificateRepository.sortByDateAsc().toString();
        assertEquals(expected, actual);
    }

    @Test
    void sortByDateDesc() {
        String expected = "[GiftCertificate{id=2, name='Mila', description='dskckjn', price=50.00, duration=30, createDate=2022-02-01T17:40:01, lastUpdateDate=2022-04-01T10:37:33}, " +
                "GiftCertificate{id=1, name='ZIKO', description='nkdcdnc', price=150.00, duration=35, createDate=2022-01-01T15:40:01, lastUpdateDate=2022-03-01T14:37:33}]";
        String actual = certificateRepository.sortByDateDesc().toString();
        assertEquals(expected, actual);
    }

    @Test
    void sortByNameAsc() {
        String expected = "[GiftCertificate{id=2, name='Mila', description='dskckjn', price=50.00, duration=30, createDate=2022-02-01T17:40:01, lastUpdateDate=2022-04-01T10:37:33}, " +
                "GiftCertificate{id=1, name='ZIKO', description='nkdcdnc', price=150.00, duration=35, createDate=2022-01-01T15:40:01, lastUpdateDate=2022-03-01T14:37:33}]";
        String actual = certificateRepository.sortByNameAsc().toString();
        assertEquals(expected, actual);
    }

    @Test
    void sortByNameDesc() {
        String expected = "[GiftCertificate{id=1, name='ZIKO', description='nkdcdnc', price=150.00, duration=35, " +
                "createDate=2022-01-01T15:40:01, lastUpdateDate=2022-03-01T14:37:33}, GiftCertificate{id=2, name='Mila', " +
                "description='dskckjn', price=50.00, duration=30, createDate=2022-02-01T17:40:01, lastUpdateDate=2022-04-01T10:37:33}]";
        String actual = certificateRepository.sortByNameDesc().toString();
        assertEquals(expected, actual);
    }

    @Test
    void bothSorting(){
        String expected = "[GiftCertificate{id=2, name='Mila', description='dskckjn', price=50.00, duration=30, createDate=2022-02-01T17:40:01, lastUpdateDate=2022-04-01T10:37:33}, " +
                "GiftCertificate{id=1, name='ZIKO', description='nkdcdnc', price=150.00, duration=35, createDate=2022-01-01T15:40:01, lastUpdateDate=2022-03-01T14:37:33}]";
        String actual = certificateRepository.bothSorting().toString();
        assertEquals(expected, actual);
    }

    @Test
    void insertKeys(){
        boolean actual = certificateRepository.insertKeys(4, 1);
        assertFalse(actual);
    }
}