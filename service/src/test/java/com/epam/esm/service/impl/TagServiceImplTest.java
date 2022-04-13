package com.epam.esm.service.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;


class TagServiceImplTest {

    private TagService tagService;
    private TagRepository mock = Mockito.mock(TagRepository.class);

    @BeforeEach
    public void setUp(){
        tagService = new TagServiceImpl(mock);
    }

    @Test
    void showAll() {
        Mockito.when(mock.show()).thenReturn(Set.of(new Tag(1L,"name"), new Tag(2L,"sea")));
        tagService.showAll();
        Mockito.verify(mock).show();
    }

    @Test
    void delete() {
        Mockito.when(mock.delete(Mockito.anyLong())).thenReturn(true);
        boolean actual = tagService.delete(1L);
        assertTrue(actual);
        Mockito.verify(mock).delete(Mockito.anyLong());
    }

    @Test
    void showById() {
        Mockito.when(mock.showById(Mockito.anyLong())).thenReturn(new Tag(1L, "name"));
        Tag actual = tagService.showById(1L);
        assertNotNull(actual);
        Mockito.verify(mock).showById(Mockito.anyLong());
    }

    @AfterEach
    public void end(){
        tagService = null;
    }
}