package com.epam.esm.service;

import com.epam.esm.model.dto.CertificateDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * The interface Certificate service contains methods for business logic with certificates
 * and their tags.
 *
 * @author Anna Merkul
 */
public interface CertificateService {
    /**
     * Create certificate with tags.
     *
     * @param certificateDto the certificate dto
     * @return the long
     */
    long create(CertificateDto certificateDto);

    /**
     * Show all list of certificates with tag.
     *
     * @param limit  the limit
     * @param offset the offset
     * @return the list
     */
    List<CertificateDto> showAll(int limit, int offset);

    /**
     * Show certificate with tags certificate dto.
     *
     * @param id the id
     * @return the certificate dto
     */
    CertificateDto showCertificateWithTags(long id);

    /**
     * Update certificate dto.
     *
     * @param newDto the new dto
     * @param id     the id
     */
    void update(CertificateDto newDto, long id);

    /**
     * Show by tag name list certificates with tags.
     *
     * @param limit   the limit
     * @param offset  the offset
     * @param tagName the tag name
     * @return the list
     */
    List<CertificateDto> showByTagName(int limit, int offset, String tagName);

    /**
     * Show by part word list certificates with tags.
     *
     * @param limit    the limit
     * @param offset   the offset
     * @param partWord the part word
     * @return the list
     */
    List<CertificateDto> showByPartWord(int limit, int offset, String partWord);

    /**
     * Sort by name list certificates with tags.
     *
     * @param limit  the limit
     * @param offset the offset
     * @return the list
     */
    List<CertificateDto> sortByName(int limit, int offset);

    /**
     * Sort by date list certificates with tags.
     *
     * @param limit  the limit
     * @param offset the offset
     * @return the list
     */
    List<CertificateDto> sortByDate(int limit, int offset);

    /**
     * Both sort list certificates with tags.
     *
     * @param limit  the limit
     * @param offset the offset
     * @return the list
     */
    List<CertificateDto> bothSort(int limit, int offset);

    /**
     * Delete certificate.
     *
     * @param id the id
     * @return the boolean
     */
    boolean deleteCertificate(long id);

    /**
     * Update duration certificate.
     *
     * @param certificateId the certificate id
     * @param duration      the duration
     */
    void updateCertificateDuration(long certificateId, int duration);

    /**
     * Update certificate price boolean.
     *
     * @param certificateId the certificate id
     * @param price         the price
     */
    void updateCertificatePrice(long certificateId, BigDecimal price);

    /**
     * Find certificates by query list.
     *
     * @param limit  the limit
     * @param offset the offset
     * @param query  the query
     * @return the list
     */
    List<CertificateDto> findCertificatesByQuery(int limit, int offset, String query);

    /**
     * Find count certificate records int.
     *
     * @return the int
     */
    int findCountCertificateRecords();

    /**
     * Find count certificates by tag name int.
     *
     * @param tagName the tag name
     * @return the int
     */
    int findCountCertificatesByTagName(String tagName);

    /**
     * Find count by part name or description int.
     *
     * @param part the part
     * @return the int
     */
    int findCountByPartNameOrDescription(String part);

    /**
     * Find count certificates by tags int.
     *
     * @param query the query
     * @return the int
     */
    int findCountCertificatesByTags(String query);
}
