package com.epam.esm.repository.impl;

import com.epam.esm.config.DevelopmentConfig;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DevelopmentConfig.class)
@ActiveProfiles("dev")
class TagRepositoryImplTest {

    @Autowired
    private TagRepository tagRepository;

    @Test
    void create(){
        Tag actual = tagRepository.save(new Tag("nckdnkcx"));
        assertNotNull(actual);
    }

    @Test
    void tagNullTrue(){
        Tag tag1 = new Tag();
        Tag tag = tagRepository.findTagById(tag1.getId());
        assertNull(tag);
    }

    @Test
    void show() {
        List<Tag> actual = tagRepository.findAll(PageRequest.of(0 , 2)).getContent();
        assertNotNull(actual);
    }

    @Test
    void showByName(){
        Tag tag = tagRepository.findTagByName("#like");
        assertNotNull(tag);
    }

    @Test
    void countAllTags(){
        int actual = tagRepository.countAllTags();
        int expected = 4;
        assertEquals(expected, actual);
    }

    @Test
    void showById() {
        Tag tag = tagRepository.findTagById(1);
        String expected = "#like";
        String actual = tag.getName();
        assertEquals(expected, actual);
    }


    @Test
    void delete() {
        tagRepository.deleteTagById(3);
        int expected = 3;
        int actual = tagRepository.findAll(PageRequest.of(0, 3)).getContent().size();
        assertEquals(expected, actual);
    }

    @Test
    void findWidelyUserTagWithHighestOrdersCostTest(){
        List<Tag> tags = tagRepository.findWidelyUserTagWithHighestOrdersCost();
        int expected = 2;
        int actual = tags.size();
        assertEquals(expected, actual);
    }
}