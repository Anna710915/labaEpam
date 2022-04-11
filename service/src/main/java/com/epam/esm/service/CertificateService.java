package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;

import java.util.List;

public interface CertificateService {
    long create(CertificateDto certificateDto);
    List<CertificateDto> showAll();
    CertificateDto showCertificateWithTags(long id);
    CertificateDto update(CertificateDto newDto, long id);
    List<CertificateDto> showByTagName(String tagName);
    List<CertificateDto> showByPartWord(String partWord);
    List<CertificateDto> sortByName();
    List<CertificateDto> sortByDate();
    List<CertificateDto> bothSort();
    boolean deleteCertificate(long id);
}
