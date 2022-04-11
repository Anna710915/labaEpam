package com.epam.esm.repository.mapper;

import com.epam.esm.domain.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagMapper implements RowMapper<Tag> {
    public static final String TAG_ID = "tag_id";
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