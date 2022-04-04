package com.epam.esm.repository.impl;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.repository.CertificateRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CertificateRepositoryImplTest {

    private EmbeddedDatabase embeddedDatabase;
    private CertificateRepository certificateRepository;

    @BeforeEach
    public void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("classpath:data.sql")
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        certificateRepository = new CertificateRepositoryImpl(jdbcTemplate);
    }

    @Test
    void create() {
        certificateRepository.create(new GiftCertificate("ZIKO","nkdcdnc", BigDecimal.valueOf(150), 35, LocalDateTime.of(2021,6,17,16,11,17),
                LocalDateTime.of(2022,1,17,16,15,17)));
        int expected = 3;
        int actual = certificateRepository.show().size();
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
    }

    @Test
    void delete() {
    }

    @Test
    void showByTagName() {
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
    }

    @Test
    void sortByDate() {
    }

    @Test
    void sortByName() {
    }

    @AfterEach
    public void tearDown() {
        embeddedDatabase.shutdown();
    }
}