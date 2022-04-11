package com.epam.esm.repository;

import com.epam.esm.domain.Tag;
import java.util.Set;

public interface TagRepository {
    long create(Tag tag);
    Set<Tag> show();
    Tag showById(long id);
    boolean delete(long id);
    Set<Tag> showByCertificateId(long certificateId);
}
