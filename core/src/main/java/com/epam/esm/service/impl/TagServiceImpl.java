package com.epam.esm.service.impl;

import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Tag service implements methods of the TagService
 * interface. The class is annotated as a service, which qualifies it to be
 * automatically created by component-scanning.
 * @author Anna Merkul
 */
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    /**
     * Instantiates a new Tag service.
     *
     * @param tagRepository the tag repository
     */
    @Autowired
    public TagServiceImpl(TagRepository tagRepository){
        this.tagRepository = tagRepository;
    }

    @Override
    public List<TagDto> showAll(int limit, int offset) {
        List<Tag> tags = tagRepository.show(limit, offset);
        return buildListTagDto(tags);
    }

    @Override
    public boolean delete(long id) {
        return tagRepository.delete(id);
    }

    @Override
    public TagDto showById(long id) {
        Tag tag = tagRepository.showById(id);
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());
        return tagDto;
    }

    @Override
    public List<TagDto> showWidelyUserTagWithHighestOrdersCost() {
        List<Tag> tags = tagRepository.findWidelyUserTagWithHighestOrdersCost();
        return buildListTagDto(tags);
    }

    @Override
    public int countAllTags() {
        return tagRepository.countAllTags();
    }

    private List<TagDto> buildListTagDto(List<Tag> tags){
        List<TagDto> tagDtos = new ArrayList<>();
        for(Tag tag: tags){
            TagDto tagDto = new TagDto();
            tagDto.setId(tag.getId());
            tagDto.setName(tag.getName());
            tagDtos.add(tagDto);
        }
        return tagDtos;
    }
}
