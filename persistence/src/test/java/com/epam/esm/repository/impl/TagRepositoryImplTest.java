package com.epam.esm.repository.impl;

import com.epam.esm.config.DevelopmentProfileConfig;
import com.epam.esm.domain.Tag;
import com.epam.esm.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes= DevelopmentProfileConfig.class)
@ActiveProfiles("dev")
class TagRepositoryImplTest {

    @Autowired
    private TagRepository tagRepository;

    @Test
    void create(){
        long actual = tagRepository.create(new Tag("nckdnkcx"));
        long expected = 5;
        assertEquals(expected, actual);
    }

    @Test
    void tagNullTrue(){
        Tag tag1 = new Tag();
        Tag tag = tagRepository.showById(tag1.getId());
        assertNull(tag);
    }

    @Test
    void show() {
        Set<Tag> actual = tagRepository.show();
        assertNotNull(actual);
    }

    @Test
    void showById() {
        Tag tag = tagRepository.showById(1);
        String expected = "#like";
        String actual = tag.getName();
        assertEquals(expected, actual);
    }

    @Test
    void delete() {
        tagRepository.delete(3);
        int expected = 3;
        int actual = tagRepository.show().size();
        assertEquals(expected, actual);
    }

    @Test
    void showByCertificateId(){
        Set<Tag> tagSet = tagRepository.showByCertificateId(1);
        int expected = 2;
        int actual = tagSet.size();
        assertEquals(expected, actual);
    }
}