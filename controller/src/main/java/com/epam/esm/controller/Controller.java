package com.epam.esm.controller;

import com.epam.esm.domain.Tag;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.exception.CustomError;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/certificates")
public class Controller{

    private static final String START_PATH = "/certificates/";
    private final CertificateService certificateService;
    private final TagService tagService;
    private final CertificateDtoValidator certificateDtoValidator;

    @Autowired
    public Controller(CertificateService certificateService, TagService tagService, CertificateDtoValidator certificateDtoValidator){
        this.certificateService = certificateService;
        this.tagService = tagService;
        this.certificateDtoValidator = certificateDtoValidator;
    }

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

    @GetMapping(value = "/certificate", produces = "application/json")
    public List<CertificateDto> showAll(){
        return certificateService.showAll();
    }

    @GetMapping(value = "/tag/{id}")
    public Tag getTag(@PathVariable long id){
        Tag tag = tagService.showById(id);
        if(tag == null) {
            throw  new CustomNotFoundException("Tag is not found by id = " + id);
        }
        return tag;
    }

    @GetMapping(value = "/tag")
    public List<Tag> getTag(){
        return new ArrayList<>(tagService.showAll());
    }

    @DeleteMapping(value = "/tag/{id}")
    public List<Tag> deleteTag(@PathVariable long id){
        if(!tagService.delete(id)){
            throw new CustomNotFoundException("Tag is not found by id = " + id);
        }
        return new ArrayList<>(tagService.showAll());
    }

    @DeleteMapping(value = "/certificate/{id}")
    public List<CertificateDto> deleteCertificate(@PathVariable long id){
        if(!certificateService.deleteCertificate(id)){
            throw new CustomNotFoundException("Gift Certificate is not found by id = " + id);
        }
        return certificateService.showAll();
    }

    @GetMapping(value = "/certificate/{id}")
    public CertificateDto getCertificateWithTags(@PathVariable long id){
        CertificateDto certificateDto = certificateService.showCertificateWithTags(id);
        if(certificateDto == null) {
            throw  new CustomNotFoundException("Gift Certificate is not found by id = " + id);
        }
        return certificateDto;
    }

    @GetMapping(value = "/certificate", params = "tag_name")
    public List<CertificateDto> getByTagName(@RequestParam("tag_name") String name){
        return certificateService.showByTagName(name);
    }

    @GetMapping(value = "/certificate/search")
    public List<CertificateDto> searchCertificates(@RequestParam("part") String part){
        return certificateService.showByPartWord(part);
    }

    @GetMapping(value = "/certificate/sort")
    public List<CertificateDto> sort(@RequestParam(value = "param_1", required = false) String name,
                                     @RequestParam(value = "param_2", required = false) String date){
        List<CertificateDto> certificateDtoList;
        if(name != null && date != null){
            certificateDtoList = certificateService.bothSort();
        }else if(name != null){
            certificateDtoList = certificateService.sortByName();
        }else if(date != null){
            certificateDtoList = certificateService.sortByDate();
        }else{
            certificateDtoList = certificateService.showAll();
        }
        return certificateDtoList;
    }

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
            throw new CustomNotFoundException("Gift Certificate is not found by id = " + id);
        }
        return certificateDto;
    }

    @ExceptionHandler(CustomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody CustomError notFound(CustomNotFoundException e) {
        return new CustomError(404, e.getMessage());
    }

    @ExceptionHandler(CustomNotValidArgumentException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public @ResponseBody CustomError externalError(CustomNotValidArgumentException e) {
        return new CustomError(422, e.getMessage());
    }

    @ExceptionHandler(CustomExternalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody CustomError externalError(CustomExternalException e) {
        return new CustomError(500, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody CustomError externalError(RuntimeException e) {
        return new CustomError(400, e.getMessage());
    }
}
