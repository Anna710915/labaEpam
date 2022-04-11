package com.epam.esm.service.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository){
        this.tagRepository = tagRepository;
    }

    @Override
    public Set<Tag> showAll() {
        return tagRepository.show();
    }

    @Override
    public boolean delete(long id) {
        return tagRepository.delete(id);
    }

    @Override
    public Tag showById(long id) {
        return tagRepository.showById(id);
    }
}
