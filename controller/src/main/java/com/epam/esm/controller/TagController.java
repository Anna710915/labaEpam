package com.epam.esm.controller;

import com.epam.esm.attribute.HttpMethodType;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.exception.CustomNotFoundException;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.service.TagService;
import com.epam.esm.util.MessageLanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

/**
 * The type Tag controller.
 */
@RestController
@RequestMapping("/certificates")
public class TagController {

    private final TagService tagService;
    private final MessageLanguageUtil messageLanguageUtil;

    /**
     * Instantiates a new Tag controller.
     *
     * @param tagService the tag service
     */
    @Autowired
    public TagController(TagService tagService,
                         MessageLanguageUtil messageLanguageUtil) {
        this.tagService = tagService;
        this.messageLanguageUtil = messageLanguageUtil;
    }

    /**
     * Get tag.
     *
     * @param id the id
     * @return the tag
     */
    @GetMapping(value = "/tag/{id}")
    public TagDto getTag(@PathVariable long id){
        TagDto tagDto = tagService.showById(id);
        addAllTagsLink(tagDto);
        return tagDto;
    }

    /**
     * Get tag list.
     *
     * @return the list
     */
    @GetMapping(value = "/tag")
    public CollectionModel<TagDto> getTags(@RequestParam("page") int page,
                                           @RequestParam("size") int size){
        int offset = Pagination.offset(page, size);
        int totalRecords = tagService.countAllTags();
        int pages = Pagination.findPages(totalRecords, size);
        int lastPage = Pagination.findLastPage(pages, size, totalRecords);
        Link prevLink = linkTo(methodOn(TagController.class).getTags(Pagination.findPrevPage(page), size))
                .withRel("prev").withType(HttpMethodType.GET.name());
        Link nextLink = linkTo(methodOn(TagController.class).getTags(Pagination.findNextPage(page, lastPage), size))
                .withRel("next").withType(HttpMethodType.GET.name());
        List<TagDto> tagDtos = tagService.showAll(size, offset);
        addDeleteLinksForTags(tagDtos);
        return CollectionModel.of(tagDtos, prevLink, nextLink);
    }

    /**
     * Delete tag.
     *
     * @param id the id
     * @return the list
     */
    @DeleteMapping(value = "/tag/{id}")
    public ResponseEntity<TagDto> deleteTag(@PathVariable long id){
        if(!tagService.delete(id)){
            throw new CustomNotFoundException(messageLanguageUtil.getMessage("not_found.tag") + id);
        }
        TagDto emptyTag = new TagDto();
        addAllTagsLink(emptyTag);
        return new ResponseEntity<>(emptyTag, HttpStatus.OK);
    }

    @GetMapping(value = "/tag/popular", produces = "application/json")
    public CollectionModel<TagDto> findPopularUserTag(){
        List<TagDto> tagDtos = tagService.showWidelyUserTagWithHighestOrdersCost();
        Link allTagsLink = linkTo(methodOn(TagController.class).getTags(1, 3))
                .withRel("tags").withType(HttpMethodType.GET.name());
        return CollectionModel.of(tagDtos, allTagsLink);
    }

    private void addDeleteLinksForTags(List<TagDto> tagDtos){
        for(TagDto tagDto: tagDtos){
            long tagId = tagDto.getId();
            tagDto.add(linkTo(methodOn(TagController.class).deleteTag(tagId))
                    .withRel("delete_tag").withType(HttpMethodType.DELETE.name()));
        }
    }

    private void addAllTagsLink(TagDto tagDto){
        tagDto.add(linkTo(methodOn(TagController.class).getTags(1, 3))
                .withRel("tags").withType(HttpMethodType.GET.name()));
    }
}
