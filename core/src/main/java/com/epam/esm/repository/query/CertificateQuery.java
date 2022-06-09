package com.epam.esm.repository.query;

public final class CertificateQuery {

    public static final String UPDATE_CERTIFICATE_DURATION = """
            UPDATE gift_certificate SET duration = :duration WHERE gift_certificate_id = :certificateId""";

    public static final String UPDATE_CERTIFICATE_PRICE = """
            UPDATE gift_certificate SET price = :price WHERE gift_certificate_id = :certificateId""";

    public static final String SHOW_CERTIFICATE_BY_TAG_NAME = """
            SELECT certificates FROM GiftCertificate certificates JOIN certificates.tags tags WHERE tags.name = :name""";

    public static final String FIND_CERTIFICATES_BY_PART_NAME_OR_DESCRIPTION = """
            SELECT g FROM GiftCertificate g WHERE g.name LIKE %:partName% OR g.description LIKE %:partName%""";

    public static final String FIND_COUNT_RECORDS = """
            SELECT COUNT(*) FROM gift_certificate""";

    public static final String FIND_COUNT_BY_TAG_NAME = """
            SELECT COUNT(*) FROM gift_certificate JOIN tags_gift_certificates
            ON  tags_gift_certificates.gift_certificate_id = gift_certificate.gift_certificate_id
            JOIN tag ON tag.tag_id = tags_gift_certificates.tag_id WHERE tag.name = :tagName""";

    public static final String FIND_COUNT_CERTIFICATES_BY_PART_NAME_OR_DESCRIPTION = """
            SELECT COUNT(g) FROM GiftCertificate g WHERE g.name  LIKE %:partName% OR g.description LIKE %:partName%""";

    public static final String SQL_FIND_BY_TAGS_START = """
            SELECT gift_certificate_id, name, description, price, duration, create_date, last_update_date  FROM (
            SELECT gift_certificate.*, tag.name AS tag_name FROM gift_certificate
            JOIN tags_gift_certificates ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id\s
            JOIN tag ON tag.tag_id = tags_gift_certificates.tag_id
            WHERE tag.name = '""";
    public static final String SQL_FIND_BY_TAGS_UNION = """
            '
            UNION SELECT gift_certificate.*, tag.name AS tag_name FROM gift_certificate
            JOIN tags_gift_certificates ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id
            JOIN tag ON tag.tag_id = tags_gift_certificates.tag_id
            WHERE tag.name= '""";
    public static final String SQL_FIND_BY_TAGS_END = """
            ') AS used_tags_certificates
            GROUP BY gift_certificate_id
            HAVING COUNT(tag_name) =""";
}
