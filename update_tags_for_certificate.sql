CREATE DEFINER=`root`@`localhost` PROCEDURE `update`(in certificate_id bigint, in name_tag varchar(100))
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
		IF tag_key IS NULL THEN -- if the tag doesn't exist
			INSERT INTO tag (name)
			VALUES(name_tag);
			SET tag_key = LAST_INSERT_ID();
        ELSE 
			IF EXISTS (SELECT tag_id FROM tags_gift_certificates 
				WHERE tag_id = tag_key AND gift_certificate_id = certificate_id) THEN
            SET tag_key = NULL;    
            END IF;
		END IF;
        IF tag_key IS NOT NULL THEN 
			INSERT INTO tags_gift_certificates(tag_id, gift_certificate_id)
			VALUES (tag_key, certificate_id);
        END IF;
	COMMIT;
	SET autocommit=1;
END