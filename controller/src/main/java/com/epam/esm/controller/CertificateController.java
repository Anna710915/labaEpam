package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.exception.CustomExternalException;
import com.epam.esm.exception.CustomNotFoundException;
import com.epam.esm.exception.CustomNotValidArgumentException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.CertificateDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * The type Controller is a rest controller which operates requests from clients and
 * generates response in representational forms. Information exchanging are in a JSON
 * forms.
 * @author Anna Merkul
 */
@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private static final String START_PATH = "/certificates/";
    private static final String EXCEPTION_MASSAGE_CERTIFICATE = "Gift Certificate is not found by id = ";
    private final CertificateService certificateService;
    private final TagService tagService;
    private final CertificateDtoValidator certificateDtoValidator;

    /**
     * Instantiates a new Controller.
     *
     * @param certificateService      the certificate service
     * @param tagService              the tag service
     * @param certificateDtoValidator the certificate dto validator
     */
    @Autowired
    public CertificateController(CertificateService certificateService, TagService tagService, CertificateDtoValidator certificateDtoValidator){
        this.certificateService = certificateService;
        this.tagService = tagService;
        this.certificateDtoValidator = certificateDtoValidator;
    }

    /**
     * Create response entity which contains certificate dto, the location of the created
     * certificate and http status.
     *
     * @param builder        the builder
     * @param certificateDto the certificate dto
     * @param bindingResult  the binding result
     * @return the response entity
     */
    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<CertificateDto> create(UriComponentsBuilder builder,
                                                 @RequestBody CertificateDto certificateDto,
                                                 BindingResult bindingResult){
        certificateDtoValidator.validate(certificateDto, bindingResult);
        if(bindingResult.hasErrors()){
            throw new CustomNotValidArgumentException(bindingResult.toString());
        }
        long createdId = certificateService.create(certificateDto);
        HttpStatus status = createdId < 0 ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.CREATED;
        if(status == HttpStatus.INTERNAL_SERVER_ERROR){
            throw new CustomExternalException(certificateDto.toString());
        }
        HttpHeaders headers = new HttpHeaders();
        URI locationUri = builder.path(START_PATH)
                        .path(String.valueOf(createdId))
                        .build()
                        .toUri();
        headers.setLocation(locationUri);
        return new ResponseEntity<>(certificateDto, headers, HttpStatus.CREATED);
    }

    /**
     * Show all list of certificates dto.
     *
     * @return the list
     */
    @GetMapping(value = "/certificate", produces = "application/json")
    public List<CertificateDto> showAll(){
        return certificateService.showAll();
    }



    /**
     * Delete certificate.
     *
     * @param id the id
     * @return the list
     */
    @DeleteMapping(value = "/certificate/{id}")
    public List<CertificateDto> deleteCertificate(@PathVariable long id){
        if(!certificateService.deleteCertificate(id)){
            throw new CustomNotFoundException( EXCEPTION_MASSAGE_CERTIFICATE + id);
        }
        return certificateService.showAll();
    }

    /**
     * Get certificate with tags certificate dto.
     *
     * @param id the id
     * @return the certificate dto
     */
    @GetMapping(value = "/certificate/{id}")
    public CertificateDto getCertificateWithTags(@PathVariable long id){
        CertificateDto certificateDto = certificateService.showCertificateWithTags(id);
        if(certificateDto == null) {
            throw  new CustomNotFoundException(EXCEPTION_MASSAGE_CERTIFICATE + id);
        }
        return certificateDto;
    }

    /**
     * Gets certificates by tag name.
     *
     * @param name the name
     * @return the by tag name
     */
    @GetMapping(value = "/certificate", params = "tag_name")
    public List<CertificateDto> getByTagName(@RequestParam("tag_name") String name){
        return certificateService.showByTagName(name);
    }

    /**
     * Search certificates list.
     *
     * @param part the part
     * @return the list
     */
    @GetMapping(value = "/certificate/search")
    public List<CertificateDto> searchCertificates(@RequestParam("part") String part){
        return certificateService.showByPartWord(part);
    }

    /**
     * Sort list certificate dto.
     *
     * @param name the name
     * @param date the date
     * @return the list
     */
    @GetMapping(value = "/certificate/sort")
    public List<CertificateDto> sort(@RequestParam(value = "param_1", required = false) String name,
                                     @RequestParam(value = "param_2", required = false) String date){
        List<CertificateDto> certificateDtoList;
        if(name.equals("name") && date.equals("date")){
            certificateDtoList = certificateService.bothSort();
        }else if(name.equals("name")){
            certificateDtoList = certificateService.sortByName();
        }else if(date.equals("date")){
            certificateDtoList = certificateService.sortByDate();
        }else{
            certificateDtoList = certificateService.showAll();
        }
        return certificateDtoList;
    }

    /**
     * Update certificate certificate dto.
     *
     * @param id                the id
     * @param updateCertificate the update certificate
     * @param bindingResult     the binding result
     * @return the certificate dto
     */
    @PutMapping(value = "/certificate/{id}/update", consumes = "application/json")
    public CertificateDto updateCertificate(@PathVariable long id,
                                            @RequestBody CertificateDto updateCertificate,
                                            BindingResult bindingResult){
        certificateDtoValidator.validate(updateCertificate, bindingResult);
        if(bindingResult.hasErrors()){
            throw new CustomNotValidArgumentException(bindingResult.toString());
        }
        CertificateDto certificateDto = certificateService.update(updateCertificate, id);
        if(certificateDto == null){
            throw new CustomNotFoundException(EXCEPTION_MASSAGE_CERTIFICATE + id);
        }
        return certificateDto;
    }

}
