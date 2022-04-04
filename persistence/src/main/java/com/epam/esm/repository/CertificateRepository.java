package com.epam.esm.repository;

import com.epam.esm.domain.GiftCertificate;

import java.util.List;

public interface CertificateRepository {
    void create(GiftCertificate giftCertificate);
    List<GiftCertificate> show();
    GiftCertificate showById(long id);
    void update(long id, GiftCertificate giftCertificate);
    void delete(long id);
    List<GiftCertificate> showByTagName(String name);
    List<GiftCertificate> showByPartName(String partName);
    List<GiftCertificate> showByPartDescription(String partDescription);
    List<GiftCertificate> sortByDateAsc();
    List<GiftCertificate> sortByDateDesc();
    List<GiftCertificate> sortByNameAsc();
    List<GiftCertificate> sortByNameDesc();
}
