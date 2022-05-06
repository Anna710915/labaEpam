package com.epam.esm.repository.impl;

import com.epam.esm.config.DevelopmentConfig;
import com.epam.esm.exception.CustomNotFoundException;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DevelopmentConfig.class)
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
        List<Tag> actual = tagRepository.show(2,0);
        assertNotNull(actual);
    }

    @Test
    void showByName(){
        Optional<Tag> tag = tagRepository.showByName("#like");
        assertTrue(tag.isPresent());
    }

    @Test
    void countAllTags(){
        int actual = tagRepository.countAllTags();
        int expected = 4;
        assertEquals(expected, actual);
    }

    @Test
    void showById() {
        Tag tag = tagRepository.showById(1);
        String expected = "#like";
        String actual = tag.getName();
        assertEquals(expected, actual);
    }

    @Test
    void showByIdException(){
        Assertions.assertThrows(CustomNotFoundException.class, () ->{
            tagRepository.showById(4444);
                });
    }

    @Test
    void delete() {
        tagRepository.delete(3);
        int expected = 3;
        int actual = tagRepository.show(3,0).size();
        assertEquals(expected, actual);
    }

    @Test
    void findWidelyUserTagWithHighestOrdersCostTest(){
        List<Tag> tags = tagRepository.findWidelyUserTagWithHighestOrdersCost();
        System.out.println(tags);
        int expected = 2;
        int actual = tags.size();
        assertEquals(expected, actual);
    }
}