package com.epam.esm.repository.mapper;

import com.epam.esm.domain.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type Tag mapper implements PowMapper class and used for mapping rows of the
 * ResultSet.
 */
public class TagMapper implements RowMapper<Tag> {
    /**
     * The constant TAG_ID.
     */
    public static final String TAG_ID = "tag_id";
    /**
     * The constant TAG_NAME.
     */
    public static final String TAG_NAME = "name";

    @Override
    public Tag mapRow(ResultSet rs, int rowNum){
        Tag tag = new Tag();
        try {
            tag.setId(rs.getLong(TAG_ID));
            tag.setName(rs.getString(TAG_NAME));
        }catch(SQLException exception){
            tag = null;
        }
        return tag;
    }
}
