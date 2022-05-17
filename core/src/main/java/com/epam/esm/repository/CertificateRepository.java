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
    @Query(value = "UPDATE gift_certificate SET duration = :duration WHERE gift_certificate_id = :certificateId", nativeQuery = true)
    void updateDuration(@Param("certificateId") long certificateId, @Param("duration") int duration);

    /**
     * Update price boolean.
     *
     * @param certificateId the certificate id
     * @param price         the price
     */
    @Modifying
    @Query(value = "UPDATE gift_certificate SET price = :price WHERE gift_certificate_id = :certificateId", nativeQuery = true)
    void updatePrice(@Param("certificateId") long certificateId, @Param("price") BigDecimal price);

    int deleteGiftCertificateById(long id);

    @Query(value = "SELECT certificates FROM GiftCertificate certificates JOIN certificates.tags tags WHERE tags.name = :name")
    List<GiftCertificate> showByTagName(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT g FROM GiftCertificate g WHERE g.name LIKE %:partName% OR g.description LIKE %:partName%")
    List<GiftCertificate> findGiftCertificatesByPartNameOrDescription(@Param("partName") String partName, Pageable pageable);

    /**
     * Find count records int.
     *
     * @return the int
     */
    @Query(value = "SELECT COUNT(*) FROM gift_certificate", nativeQuery = true)
    int findCountRecords();

    /**
     * Find count by tag name int.
     *
     * @param tagName the tag name
     * @return the int
     */
    @Query(value = "SELECT COUNT(*) FROM gift_certificate JOIN tags_gift_certificates " +
            "ON  tags_gift_certificates.gift_certificate_id = gift_certificate.gift_certificate_id " +
            "JOIN tag ON tag.tag_id = tags_gift_certificates.tag_id WHERE tag.name = :tagName", nativeQuery = true)
    int findCountByTagName(@Param("tagName") String tagName);


    @Query("SELECT COUNT(g) FROM GiftCertificate g WHERE g.name  LIKE %:partName% OR g.description LIKE %:partName%")
    int countGiftCertificateByPartNameOrDescription(@Param("partName") String partName);


}
