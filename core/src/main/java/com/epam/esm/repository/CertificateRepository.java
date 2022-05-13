package com.epam.esm.repository;

import com.epam.esm.model.entity.GiftCertificate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
     * @param limit  the limit
     * @param offset the offset
     * @return the list
     */
    List<GiftCertificate> show(int limit, int offset);

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
    Optional<GiftCertificate> showByName(String name);

    /**
     * Update a certificate.
     *
     * @param giftCertificate the gift certificate
     */
    void update(GiftCertificate giftCertificate);

    /**
     * Update duration boolean.
     *
     * @param certificateId the certificate id
     * @param duration      the duration
     */
    void updateDuration(long certificateId, int duration);

    /**
     * Update price boolean.
     *
     * @param certificateId the certificate id
     * @param price         the price
     */
    void updatePrice(long certificateId, BigDecimal price);

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
     * @param limit  the limit
     * @param offset the offset
     * @param name   the name
     * @return the list
     */
    List<GiftCertificate> showByTagName(int limit, int offset, String name);

    /**
     * Show by part name or description the list of certificates.
     *
     * @param limit    the limit
     * @param offset   the offset
     * @param partName the part name
     * @return the list
     */
    List<GiftCertificate> showByPartNameOrDescription(int limit, int offset, String partName);

    /**
     * Sort by date asc the list of certificates.
     *
     * @param limit  the limit
     * @param offset the offset
     * @return the list
     */
    List<GiftCertificate> sortByDateAsc(int limit, int offset);

    /**
     * Sort by name asc the list of certificates.
     *
     * @param limit  the limit
     * @param offset the offset
     * @return the list
     */
    List<GiftCertificate> sortByNameAsc(int limit, int offset);

    /**
     * Both sorting for the list of certificates.
     *
     * @param limit  the limit
     * @param offset the offset
     * @return the list
     */
    List<GiftCertificate> bothSorting(int limit, int offset);


    /**
     * Find by tags list.
     *
     * @param limit    the limit
     * @param offset   the offset
     * @param tagsName the tags name
     * @return the list
     */
    List<GiftCertificate> findByTags(int limit, int offset, List<String> tagsName);

    /**
     * Find count records int.
     *
     * @return the int
     */
    int findCountRecords();

    /**
     * Find count by tag name int.
     *
     * @param tagName the tag name
     * @return the int
     */
    int findCountByTagName(String tagName);

    /**
     * Find count by part name or description int.
     *
     * @param part the part
     * @return the int
     */
    int findCountByPartNameOrDescription(String part);

    /**
     * Find count by tags query int.
     *
     * @param tagsName the tags name
     * @return the int
     */
    int findCountByTagsQuery(List<String> tagsName);
}
