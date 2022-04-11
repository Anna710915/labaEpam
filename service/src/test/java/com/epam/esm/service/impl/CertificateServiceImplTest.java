package com.epam.esm.service.impl;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.CertificateService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CertificateServiceImplTest {

    private CertificateService certificateService;
    private final CertificateRepository mockCertificateRepository = Mockito.mock(CertificateRepository.class);
    private final TagRepository mockTagRepository = Mockito.mock(TagRepository.class);

    @BeforeEach
    public void setUp(){
        certificateService = new CertificateServiceImpl(mockCertificateRepository, mockTagRepository);
    }

    @Test
    void create() {
        Mockito.when(mockCertificateRepository.create(Mockito.any(GiftCertificate.class))).thenReturn(3L);
        Mockito.when(mockTagRepository.showById(0L)).thenReturn( new Tag("#dislike")).thenReturn(null);
        Mockito.when(mockTagRepository.create(Mockito.any(Tag.class))).thenReturn(5L);
        CertificateDto certificateDto = new CertificateDto();
        certificateDto.setName("Some");
        certificateDto.setDescription("Something");
        certificateDto.setPrice(BigDecimal.valueOf(130));
        certificateDto.setDuration(60);
        certificateDto.setCreateDate(LocalDateTime.of(2021, 5, 5, 15,30,44));
        certificateDto.setCreateDate(LocalDateTime.of(2021, 6, 5, 15,30,44));
        certificateDto.setTagSet(Set.of(new Tag(1, "#like"), new Tag("#dislike")));
        certificateService.create(certificateDto);
        Mockito.verify(mockTagRepository, Mockito.times(2)).showById(Mockito.anyLong());
        Mockito.verify(mockTagRepository, Mockito.times(1)).create(Mockito.any(Tag.class));
        Mockito.verify(mockTagRepository,Mockito.times(1)).create(Mockito.any());
    }


    @Test
    void showAll() {
        GiftCertificate giftCertificate = new GiftCertificate(1, "sss", "aaaa", BigDecimal.valueOf(44.3), 5,
                LocalDateTime.of(2021, 5, 4, 12, 12, 12, 12), LocalDateTime.of(2022, 2, 3, 14,15,16));
        CertificateDto certificateDto = new CertificateDto(1, "sss", BigDecimal.valueOf(44.3),  Set.of(new Tag("#like")));
        Mockito.when(mockCertificateRepository.show()).thenReturn(List.of(giftCertificate));
        Mockito.when(mockTagRepository.showByCertificateId(1)).thenReturn(Set.of(new Tag("#like")));
        List<CertificateDto> actual = certificateService.showAll();
        List<CertificateDto> expected = List.of(certificateDto);
        assertEquals(expected, actual);
        Mockito.verify(mockCertificateRepository).show();
        Mockito.verify(mockTagRepository).showByCertificateId(Mockito.anyLong());

    }

    @ParameterizedTest
    @MethodSource("dataSource")
    void showCertificateWithTags(GiftCertificate giftCertificate, CertificateDto certificateDto){
        Mockito.when(mockCertificateRepository.showById(Mockito.anyLong())).thenReturn(giftCertificate);
        Mockito.when(mockTagRepository.showByCertificateId(Mockito.anyLong())).thenReturn(certificateDto.getTagSet());
        CertificateDto actual = certificateService.showCertificateWithTags(giftCertificate.getId());
        assertEquals(actual.getName(), certificateDto.getName());
        Mockito.verify(mockCertificateRepository).showById(Mockito.anyLong());
        Mockito.verify(mockTagRepository).showByCertificateId(Mockito.anyLong());
    }

    @Test
    void update() {
        GiftCertificate giftCertificate = new GiftCertificate(1L, "sss", "aaaa", BigDecimal.valueOf(44.3), 5,
                LocalDateTime.of(2021, 5, 4, 12, 12, 12),
                LocalDateTime.of(2022, 2, 3, 14,15,16));
        Mockito.when(mockCertificateRepository.showById(Mockito.anyLong())).thenReturn(giftCertificate);
        Mockito.when(mockCertificateRepository.update(Mockito.anyLong(), Mockito.any())).thenReturn(true);
        Mockito.when(mockTagRepository.showById(0L)).thenReturn( new Tag("#dislike")).thenReturn(null);
        Mockito.when(mockTagRepository.create(Mockito.any(Tag.class))).thenReturn(5L);
        CertificateDto certificateDto = new CertificateDto();
        certificateDto.setName("Some");
        certificateDto.setDescription("Something");
        certificateDto.setPrice(BigDecimal.valueOf(100.00));
        certificateDto.setDuration(60);
        certificateDto.setTagSet(Set.of(new Tag(1L, "#like"), new Tag("#dislike")));
        certificateService.update(certificateDto, 1L);
        Mockito.verify(mockCertificateRepository, Mockito.times(1)).showById(Mockito.anyLong());
        Mockito.verify(mockCertificateRepository).update(Mockito.anyLong(), Mockito.any());
        Mockito.verify(mockTagRepository, Mockito.times(2)).showById(Mockito.anyLong());
        Mockito.verify(mockTagRepository, Mockito.times(1)).create(Mockito.any(Tag.class));
        Mockito.verify(mockTagRepository,Mockito.times(1)).create(Mockito.any());
    }

    @Test
    void showByTagName() {
        List<CertificateDto> expected = List.of(new CertificateDto(2L, "b", BigDecimal.valueOf(2), Set.of(new Tag("#dislike"), new Tag("sea"))));
        GiftCertificate giftCertificate = new GiftCertificate(2L, "b", "bb", BigDecimal.valueOf(2), 5,
                LocalDateTime.of(2021, 5, 4, 12, 12, 12),
                LocalDateTime.of(2022, 2, 3, 14,15,16));
        Mockito.when(mockCertificateRepository.showByTagName("#dislike")).thenReturn(List.of(giftCertificate));
        Mockito.when(mockTagRepository.showByCertificateId(2L)).thenReturn(Set.of(new Tag("#dislike"), new Tag("sea")));
        List<CertificateDto> actual = certificateService.showByTagName("#dislike");
        assertEquals(expected, actual);
        Mockito.verify(mockCertificateRepository).showByTagName(Mockito.anyString());
        Mockito.verify(mockTagRepository).showByCertificateId(Mockito.anyLong());
    }

    @Test
    void showByPartWord() {
        List<CertificateDto> expected = List.of(new CertificateDto(2L, "ba", BigDecimal.valueOf(2), Set.of(new Tag("#dislike"), new Tag("sea"))));
        GiftCertificate giftCertificate = new GiftCertificate(2L, "ba", "bb", BigDecimal.valueOf(2), 5,
                LocalDateTime.of(2021, 5, 4, 12, 12, 12),
                LocalDateTime.of(2022, 2, 3, 14,15,16));
        Mockito.when(mockCertificateRepository.showByPartName("b")).thenReturn(List.of(giftCertificate));
        Mockito.when(mockTagRepository.showByCertificateId(2L)).thenReturn(Set.of(new Tag("#dislike"), new Tag("sea")));
        List<CertificateDto> actual = certificateService.showByPartWord("b");
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("dataSortSource")
    void sortByName(List<GiftCertificate> giftCertificateList) {
        Mockito.when(mockCertificateRepository.sortByNameAsc()).thenReturn(giftCertificateList);
        Mockito.when(mockTagRepository.showByCertificateId(Mockito.anyLong())).thenReturn(Set.of(new Tag("fffff")));
        List<CertificateDto> list = certificateService.sortByName();
        List<Long> actual = new ArrayList<>();
        for(CertificateDto certificateDto : list){
            actual.add(certificateDto.getId());
        }
        List<Long> expected = List.of(1L, 2L, 3L);
        assertEquals(expected, actual);
        Mockito.verify(mockCertificateRepository).sortByNameAsc();
        Mockito.verify(mockTagRepository, Mockito.times(3)).showByCertificateId(Mockito.anyLong());
    }

    @ParameterizedTest
    @MethodSource("dataSortSource")
    void sortByDate(List<GiftCertificate> giftCertificateList) {
        Mockito.when(mockCertificateRepository.sortByDateAsc()).thenReturn(giftCertificateList);
        Mockito.when(mockTagRepository.showByCertificateId(Mockito.anyLong())).thenReturn(Set.of(new Tag("fffff")));
        certificateService.sortByDate();
        Mockito.verify(mockCertificateRepository).sortByDateAsc();
        Mockito.verify(mockTagRepository, Mockito.times(3)).showByCertificateId(Mockito.anyLong());
    }

    @AfterEach
    public void end(){
        certificateService = null;
    }

    private static Stream<Arguments> dataSource() {
        return Stream.of(
                Arguments.of(new GiftCertificate(1L, "a", "aaaa", BigDecimal.valueOf(44.3), 5,
                        LocalDateTime.of(2021, 5, 4, 12, 12, 12),
                        LocalDateTime.of(2022, 2, 3, 14,15,16)),
                        new CertificateDto(1L, "a", BigDecimal.valueOf(44.3),  Set.of(new Tag("#like")))),
                Arguments.of(new GiftCertificate(2L, "b", "bbbb", BigDecimal.valueOf(333.3), 5,
                        LocalDateTime.of(2020, 5, 4, 12, 12, 12),
                        LocalDateTime.of(2020, 2, 3, 14,15,16)),
                        new CertificateDto(2L, "b", BigDecimal.valueOf(333.3),  Set.of(new Tag("#sea")))),
                Arguments.of(new GiftCertificate(3L, "c", "cccc", BigDecimal.valueOf(666.3), 5,
                        LocalDateTime.of(2021, 5, 4, 12, 12, 12),
                        LocalDateTime.of(2022, 2, 3, 18,15,16)),
                        new CertificateDto(3L, "c", BigDecimal.valueOf(666.3),  Set.of(new Tag("#cream"))))
        );
    }

    private static Stream<Arguments> dataSortSource() {
        return Stream.of(
                Arguments.of(List.of(new GiftCertificate(1L, "a", "aaaa", BigDecimal.valueOf(44.3), 5,
                                LocalDateTime.of(2021, 5, 4, 12, 12, 12),
                                LocalDateTime.of(2022, 2, 3, 14,15,16)),
                                new GiftCertificate(2L, "b", "bbbb", BigDecimal.valueOf(333.3), 5,
                                        LocalDateTime.of(2020, 5, 4, 12, 12, 12),
                                        LocalDateTime.of(2020, 2, 3, 14,15,16)),
                                new GiftCertificate(3L, "c", "cccc", BigDecimal.valueOf(666.3), 5,
                                        LocalDateTime.of(2019, 5, 4, 12, 12, 12),
                                        LocalDateTime.of(2022, 2, 3, 18,15,16)))
                ));
    }
}