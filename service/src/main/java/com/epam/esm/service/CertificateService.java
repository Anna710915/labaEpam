package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;

import java.util.List;

/**
 * The interface Certificate service contains methods for business logic with certificates
 * and their tags.
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
     * @return the list
     */
    List<CertificateDto> showAll();

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
     * @return the certificate dto
     */
    CertificateDto update(CertificateDto newDto, long id);

    /**
     * Show by tag name list certificates with tags.
     *
     * @param tagName the tag name
     * @return the list
     */
    List<CertificateDto> showByTagName(String tagName);

    /**
     * Show by part word list certificates with tags.
     *
     * @param partWord the part word
     * @return the list
     */
    List<CertificateDto> showByPartWord(String partWord);

    /**
     * Sort by name list certificates with tags.
     *
     * @return the list
     */
    List<CertificateDto> sortByName();

    /**
     * Sort by date list certificates with tags.
     *
     * @return the list
     */
    List<CertificateDto> sortByDate();

    /**
     * Both sort list certificates with tags.
     *
     * @return the list
     */
    List<CertificateDto> bothSort();

    /**
     * Delete certificate.
     *
     * @param id the id
     * @return the boolean
     */
    boolean deleteCertificate(long id);
}
