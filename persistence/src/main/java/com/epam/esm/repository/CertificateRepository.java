package com.epam.esm.repository;

import com.epam.esm.domain.GiftCertificate;

import java.util.List;

/**
 * The interface Certificate repository contains methods for certificates.
 *
 * @author Anna Merkul
 */
public interface CertificateRepository {
    /**
     * Create a certificate.
     *
     * @param giftCertificate the gift certificate
     * @return the long
     */
    long create(GiftCertificate giftCertificate);

    /**
     * Show list of certificates.
     *
     * @return the list
     */
    List<GiftCertificate> show();

    /**
     * Show by id gift certificate.
     *
     * @param id the id
     * @return the gift certificate
     */
    GiftCertificate showById(long id);

    /**
     * Show by name gift certificate.
     *
     * @param name the name
     * @return the gift certificate
     */
    GiftCertificate showByName(String name);

    /**
     * Update a certificate.
     *
     * @param id              the id
     * @param giftCertificate the gift certificate
     * @return the boolean
     */
    boolean update(long id, GiftCertificate giftCertificate);

    /**
     * Delete a certificate.
     *
     * @param id the id
     * @return the boolean
     */
    boolean delete(long id);

    /**
     * Show by tag name the list of certificates.
     *
     * @param name the name
     * @return the list
     */
    List<GiftCertificate> showByTagName(String name);

    /**
     * Show by part name or description the list of certificates.
     *
     * @param partName the part name
     * @return the list
     */
    List<GiftCertificate> showByPartNameOrDescription(String partName);

    /**
     * Sort by date asc the list of certificates.
     *
     * @return the list
     */
    List<GiftCertificate> sortByDateAsc();

    /**
     * Sort by date desc the list of certificates.
     *
     * @return the list
     */
    List<GiftCertificate> sortByDateDesc();

    /**
     * Sort by name asc the list of certificates.
     *
     * @return the list
     */
    List<GiftCertificate> sortByNameAsc();

    /**
     * Sort by name desc the list of certificates.
     *
     * @return the list
     */
    List<GiftCertificate> sortByNameDesc();

    /**
     * Both sorting for the list of certificates.
     *
     * @return the list
     */
    List<GiftCertificate> bothSorting();

    /**
     * Insert keys of certificate and tag id.
     *
     * @param tagId         the tag id
     * @param certificateId the certificate id
     * @return the boolean
     */
    boolean insertKeys(long tagId, long certificateId);

    /**
     * Delete keys boolean.
     *
     * @param tagId         the tag id
     * @param certificateId the certificate id
     * @return the boolean
     */
    boolean deleteKeys(long tagId, long certificateId);
}
