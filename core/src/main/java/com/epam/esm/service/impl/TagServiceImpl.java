package com.epam.esm.service.impl;

import com.epam.esm.exception.CustomNotFoundException;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.util.MessageLanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Tag service implements methods of the TagService
 * interface. The class is annotated as a service, which qualifies it to be
 * automatically created by component-scanning.
 *
 * @author Anna Merkul
 */
@Service
public class TagServiceImpl  implements TagService{

    private final TagRepository tagRepository;
    private final MessageLanguageUtil messageLanguageUtil;

    /**
     * Instantiates a new Tag service.
     *
     * @param tagRepository       the tag repository
     * @param messageLanguageUtil the message language util
     */
    @Autowired
    public TagServiceImpl(TagRepository tagRepository,
                          MessageLanguageUtil messageLanguageUtil){
        this.messageLanguageUtil = messageLanguageUtil;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public List<TagDto> showAll(int page, int size) {
        List<Tag> tags = tagRepository.findAll(PageRequest.of(page - 1 , size)).getContent();
        return buildListTagDto(tags);
    }

    @Override
    @Transactional
    public boolean delete(long id) {
        return tagRepository.deleteTagById(id) == 1;
    }

    @Override
    @Transactional
    public TagDto showById(long id) {
        Tag tag = tagRepository.findTagById(id);
        checkTag(tag, id);
        return initTagDto(tag);
    }

    @Override
    @Transactional
    public List<TagDto> showWidelyUserTagWithHighestOrdersCost() {
        List<Tag> tags = tagRepository.findWidelyUserTagWithHighestOrdersCost();
        return buildListTagDto(tags);
    }

    @Override
    @Transactional
    public int countAllTags() {
        return tagRepository.countAllTags();
    }

    private List<TagDto> buildListTagDto(List<Tag> tags){
        List<TagDto> tagDtos = new ArrayList<>();
        for(Tag tag: tags){
            TagDto tagDto = initTagDto(tag);
            tagDtos.add(tagDto);
        }
        return tagDtos;
    }

    private TagDto initTagDto(Tag tag){
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());
        return tagDto;
    }

    private void checkTag(Tag tag, long id){
        if(tag == null){
            throw new CustomNotFoundException(messageLanguageUtil.getMessage("not_found.tag") + id);
        }
    }
}
