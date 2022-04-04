package com.epam.esm.repository.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.TagRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

//@RunWith(SpringRunner.class)
//@ContextConfiguration(
////        loader = AnnotationConfigContextLoader.class,
//        classes=DevelopmentProfileConfig.class)
//@ActiveProfiles("dev")
class TagRepositoryImplTest {

    private EmbeddedDatabase embeddedDatabase;
    private TagRepository tagRepository;

    @BeforeEach
    public void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("classpath:data.sql")
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        tagRepository = new TagRepositoryImpl(jdbcTemplate);
    }
//    @Autowired
//    private TagRepository tagRepository;


    @Test
    void create(){
        tagRepository.create(new Tag("nckdnkcx"));
        int expected = 5;
        int actual = tagRepository.show().size();
        assertEquals(expected, actual);
    }

    @Test
    void show() {
        List<Tag> actual = tagRepository.show();
        assertNotNull(actual);
    }

    @Test
    void showById() {
        Tag tag = tagRepository.showById(1);
        String expected = "#like";
        String actual = tag.getName();
        assertEquals(expected, actual);
    }

    @Test
    void delete() {
        tagRepository.delete(3);
        int expected = 3;
        int actual = tagRepository.show().size();
        assertEquals(expected, actual);
    }

    @AfterEach
    public void tearDown() {
        embeddedDatabase.shutdown();
    }
}