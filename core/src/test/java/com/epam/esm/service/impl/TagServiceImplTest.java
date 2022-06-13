package com.epam.esm.service.impl;

import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.util.MessageLanguageUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TagRepository.class)
class TagServiceImplTest {

    private TagService tagService;
    private final MessageLanguageUtil messageLanguageUtil = Mockito.mock(MessageLanguageUtil.class);
    private final TagRepository mock = Mockito.mock(TagRepository.class);


    @BeforeEach
    public void setUp(){
        tagService = new TagServiceImpl(mock, messageLanguageUtil);
    }

    @Test
    void showAll() {
        Mockito.when(mock.findAll(PageRequest.of(0, 2))).thenReturn(new PageImpl<>(List.of(new Tag(1L, "name"), new Tag(2L, "sea"))));
        tagService.showAll(1,2);
        Mockito.verify(mock).findAll(PageRequest.of(0, 2));
    }

    @Test
    void delete() {
        Mockito.when(mock.deleteTagById(Mockito.anyLong())).thenReturn(1);
        boolean actual = tagService.delete(1L);
        assertTrue(actual);
        Mockito.verify(mock).deleteTagById(Mockito.anyLong());
    }

    @Test
    void showById() {
        Mockito.when(mock.findTagById(Mockito.anyLong())).thenReturn(new Tag(1L, "name"));
        TagDto actual = tagService.showById(1L);
        assertNotNull(actual);
        Mockito.verify(mock).findTagById(Mockito.anyLong());
    }

    @AfterEach
    public void end(){
        tagService = null;
    }
}