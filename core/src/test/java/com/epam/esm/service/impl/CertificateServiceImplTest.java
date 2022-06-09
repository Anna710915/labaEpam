package com.epam.esm.service.impl;

import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.MessageLanguageUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CertificateService.class)
class CertificateServiceImplTest {

    private CertificateService certificateService;
    private final CertificateRepository mockCertificateRepository = Mockito.mock(CertificateRepository.class);
    private final TagRepository mockTagRepository = Mockito.mock(TagRepository.class);
    private final MessageLanguageUtil messageLanguageUtil = Mockito.mock(MessageLanguageUtil.class);

    @BeforeEach
    public void setUp(){
        certificateService = new CertificateServiceImpl(mockCertificateRepository, mockTagRepository, messageLanguageUtil);
    }
//
//    @Test
//    void create() {
//        Mockito.when(mockCertificateRepository.create(Mockito.any(GiftCertificate.class))).thenReturn(3L);
//        Mockito.when(mockCertificateRepository.showByName(Mockito.anyString())).thenReturn(Optional.empty());
//        CertificateDto certificateDto = new CertificateDto();
//        certificateDto.setName("Some");
//        certificateDto.setDescription("Something");
//        certificateDto.setPrice(BigDecimal.valueOf(130));
//        certificateDto.setDuration(60);
//        certificateDto.setTags(List.of(new Tag(1, "#like"), new Tag("#dislike")));
//        certificateService.create(certificateDto);
//        Mockito.verify(mockCertificateRepository, Mockito.times(1)).showByName(Mockito.anyString());
//    }
//
//    @Test
//    void showAll() {
//        GiftCertificate giftCertificate = new GiftCertificate(1, "sss", "aaaa", BigDecimal.valueOf(44.3), 5,
//                LocalDateTime.of(2021, 5, 4, 12, 12, 12, 12), LocalDateTime.of(2022, 2, 3, 14,15,16),
//                List.of(new Tag("#like")));
//        CertificateDto certificateDto = new CertificateDto(1, "sss", BigDecimal.valueOf(44.3),  List.of(new Tag("#like")));
//        Mockito.when(mockCertificateRepository.show(1,0)).thenReturn(List.of(giftCertificate));
//        List<CertificateDto> actual = certificateService.showAll(1,0);
//        List<CertificateDto> expected = List.of(certificateDto);
//        assertEquals(expected, actual);
//        Mockito.verify(mockCertificateRepository).show(1,0);
//    }
//
//    @ParameterizedTest
//    @MethodSource("dataSource")
//    void showCertificateWithTags(GiftCertificate giftCertificate, CertificateDto certificateDto){
//        Mockito.when(mockCertificateRepository.showById(Mockito.anyLong())).thenReturn(giftCertificate);
//        CertificateDto actual = certificateService.showCertificateWithTags(giftCertificate.getId());
//        assertEquals(actual.getName(), certificateDto.getName());
//        Mockito.verify(mockCertificateRepository).showById(Mockito.anyLong());
//    }
//
//    @Test
//    void update() {
//        GiftCertificate giftCertificate = new GiftCertificate(1L, "sss", "aaaa", BigDecimal.valueOf(44.3), 5,
//                LocalDateTime.of(2021, 5, 4, 12, 12, 12),
//                LocalDateTime.of(2022, 2, 3, 14,15,16));
//        Mockito.when(mockCertificateRepository.showById(Mockito.anyLong())).thenReturn(giftCertificate);
//        Mockito.when(mockCertificateRepository.showByName(Mockito.anyString())).thenReturn(Optional.empty());
//        Mockito.when(messageLanguageUtil.getMessage(Mockito.anyString())).thenReturn("плохо");
//        CertificateDto certificateDto = new CertificateDto();
//        certificateDto.setName("Some");
//        certificateDto.setDescription("Something");
//        certificateDto.setPrice(BigDecimal.valueOf(100.00));
//        certificateDto.setDuration(60);
//        certificateDto.setTags(List.of(new Tag(1L, "#like"), new Tag("#dislike")));
//        certificateService.update(certificateDto, 1L);
//        Mockito.verify(mockCertificateRepository, Mockito.times(1)).showById(Mockito.anyLong());
//        Mockito.verify(mockCertificateRepository).showByName(Mockito.anyString());
//    }
//
//    @Test
//    void showByTagName() {
//        List<CertificateDto> expected = List.of(new CertificateDto(2L, "b", BigDecimal.valueOf(2), List.of(new Tag("#dislike"), new Tag("sea"))));
//        GiftCertificate giftCertificate = new GiftCertificate(2L, "b", "bb", BigDecimal.valueOf(2), 5,
//                LocalDateTime.of(2021, 5, 4, 12, 12, 12),
//                LocalDateTime.of(2022, 2, 3, 14,15,16),  List.of(new Tag("#dislike"), new Tag("sea")));
//        Mockito.when(mockCertificateRepository.showByTagName(1, 1,"#dislike")).thenReturn(List.of(giftCertificate));
//        List<CertificateDto> actual = certificateService.showByTagName(1,1, "#dislike");
//        assertEquals(expected, actual);
//        Mockito.verify(mockCertificateRepository).showByTagName(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString());
//    }
//
//    @Test
//    void showByPartWord() {
//        List<CertificateDto> expected = List.of(new CertificateDto(2L, "ba", BigDecimal.valueOf(2), List.of(new Tag("#dislike"), new Tag("sea"))));
//        GiftCertificate giftCertificate = new GiftCertificate(2L, "ba", "bb", BigDecimal.valueOf(2), 5,
//                LocalDateTime.of(2021, 5, 4, 12, 12, 12),
//                LocalDateTime.of(2022, 2, 3, 14,15,16), List.of(new Tag("#dislike"), new Tag("sea")));
//        Mockito.when(mockCertificateRepository.showByPartNameOrDescription(1,2,"b")).thenReturn(List.of(giftCertificate));
//        List<CertificateDto> actual = certificateService.showByPartWord(1,2,"b");
//        assertEquals(expected, actual);
//    }
//
//    @ParameterizedTest
//    @MethodSource("dataSortSource")
//    void sortByName(List<GiftCertificate> giftCertificateList) {
//        Mockito.when(mockCertificateRepository.sortByNameAsc(1,1)).thenReturn(giftCertificateList);
//        List<CertificateDto> list = certificateService.sortByName(1,1);
//        List<Long> actual = new ArrayList<>();
//        for(CertificateDto certificateDto : list){
//            actual.add(certificateDto.getId());
//        }
//        List<Long> expected = List.of(1L, 2L, 3L);
//        assertEquals(expected, actual);
//        Mockito.verify(mockCertificateRepository).sortByNameAsc(1,1);
//    }
//
//    @ParameterizedTest
//    @MethodSource("dataSortSource")
//    void sortByDate(List<GiftCertificate> giftCertificateList) {
//        Mockito.when(mockCertificateRepository.sortByDateAsc(1,1)).thenReturn(giftCertificateList);
//        certificateService.sortByDate(1,1);
//        Mockito.verify(mockCertificateRepository).sortByDateAsc(1,1);
//    }
//
//    @AfterEach
//    public void end(){
//        certificateService = null;
//    }
//
//    private static Stream<Arguments> dataSource() {
//        return Stream.of(
//                Arguments.of(new GiftCertificate(1L, "a", "aaaa", BigDecimal.valueOf(44.3), 5,
//                        LocalDateTime.of(2021, 5, 4, 12, 12, 12),
//                        LocalDateTime.of(2022, 2, 3, 14,15,16)),
//                        new CertificateDto(1L, "a", BigDecimal.valueOf(44.3),  List.of(new Tag("#like")))),
//                Arguments.of(new GiftCertificate(2L, "b", "bbbb", BigDecimal.valueOf(333.3), 5,
//                        LocalDateTime.of(2020, 5, 4, 12, 12, 12),
//                        LocalDateTime.of(2020, 2, 3, 14,15,16)),
//                        new CertificateDto(2L, "b", BigDecimal.valueOf(333.3),  List.of(new Tag("#sea")))),
//                Arguments.of(new GiftCertificate(3L, "c", "cccc", BigDecimal.valueOf(666.3), 5,
//                        LocalDateTime.of(2021, 5, 4, 12, 12, 12),
//                        LocalDateTime.of(2022, 2, 3, 18,15,16)),
//                        new CertificateDto(3L, "c", BigDecimal.valueOf(666.3),  List.of(new Tag("#cream"))))
//        );
//    }
//
//    private static Stream<Arguments> dataSortSource() {
//        return Stream.of(
//                Arguments.of(List.of(new GiftCertificate(1L, "a", "aaaa", BigDecimal.valueOf(44.3), 5,
//                                LocalDateTime.of(2021, 5, 4, 12, 12, 12),
//                                LocalDateTime.of(2022, 2, 3, 14,15,16)),
//                                new GiftCertificate(2L, "b", "bbbb", BigDecimal.valueOf(333.3), 5,
//                                        LocalDateTime.of(2020, 5, 4, 12, 12, 12),
//                                        LocalDateTime.of(2020, 2, 3, 14,15,16)),
//                                new GiftCertificate(3L, "c", "cccc", BigDecimal.valueOf(666.3), 5,
//                                        LocalDateTime.of(2019, 5, 4, 12, 12, 12),
//                                        LocalDateTime.of(2022, 2, 3, 18,15,16)))
//                ));
//    }
}