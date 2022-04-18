CREATE DEFINER=`root`@`localhost` PROCEDURE `create`(in certificate_id bigint, in name_tag varchar(100))
BEGIN
	DECLARE tag_key BIGINT;    
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
		BEGIN
			ROLLBACK;  -- rollback any error in the transaction
		END;
	SET autocommit=0;
    START TRANSACTION;
    SET tag_key = (SELECT tag_id FROM tag
					WHERE name = name_tag);
		IF tag_key IS NULL THEN
			INSERT INTO tag (name)
			VALUES(name_tag);
			SET tag_key = LAST_INSERT_ID();
		END IF;
		INSERT INTO tags_gift_certificates(tag_id, gift_certificate_id)
		VALUES (tag_key, certificate_id);
	COMMIT;
	SET autocommit=1;
END