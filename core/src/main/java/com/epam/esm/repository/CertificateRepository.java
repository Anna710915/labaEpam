package com.epam.esm.repository;

import com.epam.esm.model.entity.GiftCertificate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static com.epam.esm.repository.query.CertificateQuery.UPDATE_CERTIFICATE_DURATION;
import static com.epam.esm.repository.query.CertificateQuery.UPDATE_CERTIFICATE_PRICE;
import static com.epam.esm.repository.query.CertificateQuery.SHOW_CERTIFICATE_BY_TAG_NAME;
import static com.epam.esm.repository.query.CertificateQuery.FIND_CERTIFICATES_BY_PART_NAME_OR_DESCRIPTION;
import static com.epam.esm.repository.query.CertificateQuery.FIND_COUNT_RECORDS;
import static com.epam.esm.repository.query.CertificateQuery.FIND_COUNT_BY_TAG_NAME;
import static com.epam.esm.repository.query.CertificateQuery.FIND_COUNT_CERTIFICATES_BY_PART_NAME_OR_DESCRIPTION;

/**
 * The interface Certificate repository contains methods for certificates.
 *
 * @author Anna Merkul
 */
@Repository
public interface CertificateRepository extends JpaRepository<GiftCertificate, Long>, QueryCertificateRepository {

    /**
     * Show by id gift certificate.
     *
     * @param id the id
     * @return the gift certificate
     */
    GiftCertificate findGiftCertificateById(long id);

    /**
     * Show by name gift certificate.
     *
     * @param name the name
     * @return the gift certificate
     */
    GiftCertificate findGiftCertificateByName(String name);

    /**
     * Update duration boolean.
     *
     * @param certificateId the certificate id
     * @param duration      the duration
     */
    @Modifying
    @Query(value = UPDATE_CERTIFICATE_DURATION, nativeQuery = true)
    void updateDuration(@Param("certificateId") long certificateId, @Param("duration") int duration);

    /**
     * Update price boolean.
     *
     * @param certificateId the certificate id
     * @param price         the price
     */
    @Modifying
    @Query(value = UPDATE_CERTIFICATE_PRICE, nativeQuery = true)
    void updatePrice(@Param("certificateId") long certificateId, @Param("price") BigDecimal price);

    /**
     * Delete gift certificate by id int.
     *
     * @param id the id
     * @return the int
     */
    int deleteGiftCertificateById(long id);

    /**
     * Show by tag name list.
     *
     * @param name     the name
     * @param pageable the pageable
     * @return the list
     */
    @Query(value = SHOW_CERTIFICATE_BY_TAG_NAME)
    List<GiftCertificate> showByTagName(@Param("name") String name, Pageable pageable);

    /**
     * Find gift certificates by part name or description list.
     *
     * @param partName the part name
     * @param pageable the pageable
     * @return the list
     */
    @Query(value = FIND_CERTIFICATES_BY_PART_NAME_OR_DESCRIPTION)
    List<GiftCertificate> findGiftCertificatesByPartNameOrDescription(@Param("partName") String partName, Pageable pageable);



    /**
     * Find count by tag name int.
     *
     * @param tagName the tag name
     * @return the int
     */
    @Query(value = FIND_COUNT_BY_TAG_NAME, nativeQuery = true)
    int findCountByTagName(@Param("tagName") String tagName);


    /**
     * Count gift certificate by part name or description int.
     *
     * @param partName the part name
     * @return the int
     */
    @Query(value = FIND_COUNT_CERTIFICATES_BY_PART_NAME_OR_DESCRIPTION)
    int countGiftCertificateByPartNameOrDescription(@Param("partName") String partName);
}
