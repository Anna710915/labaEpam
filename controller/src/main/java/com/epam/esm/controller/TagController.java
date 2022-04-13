package com.epam.esm.controller;

import com.epam.esm.domain.Tag;
import com.epam.esm.exception.CustomNotFoundException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Tag controller.
 */
@RestController
@RequestMapping("/certificates")
public class TagController {

    private static final String EXCEPTION_MESSAGE_TAG = "Tag is not found by id = ";
    private final TagService tagService;

    /**
     * Instantiates a new Tag controller.
     *
     * @param tagService the tag service
     */
    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }


    /**
     * Get tag.
     *
     * @param id the id
     * @return the tag
     */
    @GetMapping(value = "/tag/{id}")
    public Tag getTag(@PathVariable long id){
        Tag tag = tagService.showById(id);
        if(tag == null) {
            throw  new CustomNotFoundException(EXCEPTION_MESSAGE_TAG + id);
        }
        return tag;
    }

    /**
     * Get tag list.
     *
     * @return the list
     */
    @GetMapping(value = "/tag")
    public List<Tag> getTags(){
        return new ArrayList<>(tagService.showAll());
    }

    /**
     * Delete tag.
     *
     * @param id the id
     * @return the list
     */
    @DeleteMapping(value = "/tag/{id}")
    public List<Tag> deleteTag(@PathVariable long id){
        if(!tagService.delete(id)){
            throw new CustomNotFoundException(EXCEPTION_MESSAGE_TAG + id);
        }
        return new ArrayList<>(tagService.showAll());
    }
}
