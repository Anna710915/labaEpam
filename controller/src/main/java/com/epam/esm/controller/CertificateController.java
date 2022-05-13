package com.epam.esm.controller;

import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.exception.CustomExternalException;
import com.epam.esm.exception.CustomNotFoundException;
import com.epam.esm.exception.CustomNotValidArgumentException;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.MessageLanguageUtil;
import com.epam.esm.validator.CertificateDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


/**
 * The type Controller is a rest controller which operates requests from clients and
 * generates response in representational forms. Information exchanging are in a JSON
 * forms.
 *
 * @author Anna Merkul
 */
@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private static final String GET_CERTIFICATE = "get_certificate";
    private static final String DELETE_CERTIFICATE = "delete_certificate";
    private static final String GET_CERTIFICATES = "get_certificates";
    private static final String START_PATH = "/certificates/";
    private static final String SORT_NAME = "name";
    private static final String SORT_DATE = "date";
    private static final int START_PAGE = 1;
    private static final int START_SIZE = 2;
    private final CertificateService certificateService;
    private final CertificateDtoValidator certificateDtoValidator;
    private final MessageLanguageUtil messageLanguageUtil;

    /**
     * Instantiates a new Controller.
     *
     * @param certificateService      the certificate service
     * @param certificateDtoValidator the certificate dto validator
     * @param messageLanguageUtil     the message language util
     */
    @Autowired
    public CertificateController(CertificateService certificateService,
                                 CertificateDtoValidator certificateDtoValidator,
                                 MessageLanguageUtil messageLanguageUtil){
        this.certificateService = certificateService;
        this.certificateDtoValidator = certificateDtoValidator;
        this.messageLanguageUtil = messageLanguageUtil;
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
            throw new CustomNotValidArgumentException(messageLanguageUtil.getMessage("not_valid.certificate_argument") + bindingResult);
        }
        long createdId = certificateService.create(certificateDto);
        HttpStatus status = createdId < 1 ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.CREATED;
        if(status == HttpStatus.INTERNAL_SERVER_ERROR){
            throw new CustomExternalException(messageLanguageUtil.getMessage("internal_error.create_certificate") + certificateDto);
        }
        HttpHeaders headers = new HttpHeaders();
        URI locationUri = builder.path(START_PATH).path(String.valueOf(createdId))
                        .build().toUri();
        headers.setLocation(locationUri);
        addAllCertificatesLinks(certificateDto);
        return new ResponseEntity<>(certificateDto, headers, status);
    }

    /**
     * Show all list of certificates dto.
     *
     * @param page the page
     * @param size the size
     * @return the list
     */
    @GetMapping(value = "/certificate", produces = "application/json")
    public CollectionModel<CertificateDto> showAll(@RequestParam(value = "page") int page,
                                                        @RequestParam(value = "size") int size){
        int offset = Pagination.offset(page, size);
        List<CertificateDto> certificateDtos = certificateService.showAll(size, offset);
        addLinksForCertificatesList(certificateDtos);
        int totalRecords = certificateService.findCountCertificateRecords();
        Link prev = findPrevShowAllLink(page, size);
        Link next = findNextShowAllLink(page, size, totalRecords);
        return CollectionModel.of(certificateDtos, prev, next);
    }


    /**
     * Delete certificate.
     *
     * @param id the id
     * @return the list
     */
    @DeleteMapping(value = "/certificate/{id}")
    public ResponseEntity<CertificateDto> deleteCertificate(@PathVariable(value = "id") long id){
        if(!certificateService.deleteCertificate(id)){
            throw new CustomNotFoundException(messageLanguageUtil.getMessage("not_found.certificate") + id);
        }
        CertificateDto certificateDtoEmpty = new CertificateDto(new ArrayList<>());
        addAllCertificatesLinks(certificateDtoEmpty);
        return new ResponseEntity<>(certificateDtoEmpty, HttpStatus.OK);
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
        addLinksForCertificate(certificateDto);
        return certificateDto;
    }

    /**
     * Gets certificates by tag name.
     *
     * @param name the name
     * @param page the page
     * @param size the size
     * @return the by tag name
     */
    @GetMapping(value = "/certificate", params = {"tag_name", "page", "size"})
    public CollectionModel<CertificateDto> getByTagName(@RequestParam("tag_name") String name,
                                             @RequestParam(value = "page") int page,
                                             @RequestParam(value = "size") int size){
        int offset = Pagination.offset(page, size);
        int totalRecords = certificateService.findCountCertificatesByTagName(name);
        int pages = Pagination.findPages(totalRecords, size);
        int lastPage = Pagination.findLastPage(pages, size, totalRecords);
        List<CertificateDto> certificateDtos = certificateService.showByTagName(size, offset, name);
        addLinksForCertificatesList(certificateDtos);
        Link prevLink = linkTo(methodOn(CertificateController.class)
                .getByTagName(name, Pagination.findPrevPage(page), size))
                .withRel("prev").withType(HttpMethod.GET.name());
        Link nextLink = linkTo(methodOn(CertificateController.class)
                .getByTagName(name, Pagination.findNextPage(page, lastPage), size))
                .withRel("next").withType(HttpMethod.GET.name());
        return CollectionModel.of(certificateDtos, prevLink, nextLink);
    }

    /**
     * Search certificates list.
     *
     * @param part the part
     * @param page the page
     * @param size the size
     * @return the list
     */
    @GetMapping(value = "/certificate/search", params = {"part", "page", "size"})
    public CollectionModel<CertificateDto> searchCertificates(@RequestParam("part") String part,
                                                   @RequestParam(value = "page") int page,
                                                   @RequestParam(value = "size") int size){
        int offset = Pagination.offset(page, size);
        int totalRecords = certificateService.findCountByPartNameOrDescription(part);
        int pages = Pagination.findPages(totalRecords, size);
        int lastPage = Pagination.findLastPage(pages, size, totalRecords);
        List<CertificateDto> certificateDtos =  certificateService.showByPartWord(size, offset, part);
        addLinksForCertificatesList(certificateDtos);
        Link prevLink = linkTo(methodOn(CertificateController.class)
                .searchCertificates(part, Pagination.findPrevPage(page), size))
                .withRel("prev").withType(HttpMethod.GET.name());
        Link nextLink = linkTo(methodOn(CertificateController.class)
                .searchCertificates(part, Pagination.findNextPage(page, lastPage), size))
                .withRel("next").withType(HttpMethod.GET.name());
        return CollectionModel.of(certificateDtos, prevLink, nextLink);
    }

    /**
     * Sort list certificate dto.
     *
     * @param name the name
     * @param date the date
     * @param page the page
     * @param size the size
     * @return the list
     */
    @GetMapping(value = "/certificate/sort")
    public CollectionModel<CertificateDto> sortAll(@RequestParam(value = "param_1", required = false) String name,
                                     @RequestParam(value = "param_2", required = false) String date,
                                     @RequestParam(value = "page") int page,
                                     @RequestParam(value = "size") int size){
        int offset = Pagination.offset(page, size);
        int totalRecords = certificateService.findCountCertificateRecords();
        List<CertificateDto> certificateDtos;
        if(SORT_NAME.equals(name) && SORT_DATE.equals(date)){
            certificateDtos = certificateService.bothSort(size, offset);
        }else if(SORT_NAME.equals(name)){
            certificateDtos = certificateService.sortByName(size, offset);
        }else if(SORT_DATE.equals(date)){
            certificateDtos = certificateService.sortByDate(size, offset);
        }else{
            certificateDtos = certificateService.showAll(size, offset);
        }
        int pages = Pagination.findPages(totalRecords, size);
        int lastPage = Pagination.findLastPage(pages, size, totalRecords);
        addLinksForCertificatesList(certificateDtos);
        Link prevLink = linkTo(methodOn(CertificateController.class)
                .sortAll(name, date, Pagination.findPrevPage(page), size))
                .withRel("Prev").withType(HttpMethod.GET.name());
        Link nextLink = linkTo(methodOn(CertificateController.class)
                .sortAll(name, date, Pagination.findNextPage(page, lastPage), size))
                .withRel("next").withType(HttpMethod.GET.name());
        return CollectionModel.of(certificateDtos, prevLink, nextLink);
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
            throw new CustomNotValidArgumentException(messageLanguageUtil.getMessage("not_valid.certificate_argument") + bindingResult);
        }
        certificateService.update(updateCertificate, id);
        CertificateDto certificateDto = certificateService.showCertificateWithTags(id);
        addLinksForCertificate(certificateDto);
        return certificateDto;
    }

    /**
     * Update certificate price certificate dto.
     *
     * @param id    the id
     * @param price the price
     * @return the certificate dto
     */
    @PatchMapping(value = "/certificate/{id}/price", produces = "application/json")
    public CertificateDto updateCertificatePrice(@PathVariable long id, @RequestBody BigDecimal price){
        certificateService.updateCertificatePrice(id, price);
        CertificateDto certificateDto = certificateService.showCertificateWithTags(id);
        addLinksForCertificate(certificateDto);
        return certificateDto;
    }

    /**
     * Update certificate duration certificate dto.
     *
     * @param id       the id
     * @param duration the duration
     * @return the certificate dto
     */
    @PatchMapping(value = "/certificate/{id}/duration", produces = "application/json")
    public CertificateDto updateCertificateDuration(@PathVariable long id, @RequestBody int duration){
        certificateService.updateCertificateDuration(id, duration);
        CertificateDto certificateDto = certificateService.showCertificateWithTags(id);
        addLinksForCertificate(certificateDto);
        return certificateDto;
    }

    /**
     * Find certificates by tags collection model.
     *
     * @param query the query
     * @param page  the page
     * @param size  the size
     * @return the collection model
     */
    @GetMapping(value = "/certificate/tags",  params = {"query", "page", "size"}, produces = "application/json")
    public CollectionModel<CertificateDto> findCertificatesByTags(@RequestParam(value= "query") String query,
                                                       @RequestParam(value = "page") int page,
                                                       @RequestParam(value = "size") int size){
        int offset = Pagination.offset(page, size);
        int totalRecords = certificateService.findCountCertificatesByTags(query);
        int pages = Pagination.findPages(totalRecords, size);
        int lastPage = Pagination.findLastPage(pages, size, totalRecords);
        List<CertificateDto> certificateDtos =  certificateService.findCertificatesByQuery(size, offset, query);
        addLinksForCertificatesList(certificateDtos);
        Link prevLink = linkTo(methodOn(CertificateController.class)
                .findCertificatesByTags(query, Pagination.findPrevPage(page), size))
                .withRel("prev").withType(HttpMethod.GET.name());
        Link nextLink = linkTo(methodOn(CertificateController.class)
                .findCertificatesByTags(query, Pagination.findNextPage(page, lastPage), size))
                .withRel("next").withType(HttpMethod.GET.name());
        return CollectionModel.of(certificateDtos, prevLink, nextLink);
    }

    private void addLinksForCertificate(CertificateDto certificateDto){
        addCertificatesByTagsLinks(certificateDto);
        addAllCertificatesLinks(certificateDto);
    }

    private void addLinksForCertificatesList(List<CertificateDto> certificateDtos){
        for(CertificateDto certificateDto : certificateDtos){
            addCertificateWithTagsLink(certificateDto);
            addCertificatesByTagsLinks(certificateDto);
            addCertificateDeleteLink(certificateDto);
        }
    }

    private void addCertificatesByTagsLinks(CertificateDto certificateDto){
        for(Tag tag : certificateDto.getTags()){
            certificateDto.add(linkTo(methodOn(CertificateController.class)
                    .getByTagName(tag.getName(), START_PAGE, START_SIZE)).withRel(GET_CERTIFICATES)
                    .withType(HttpMethod.GET.name()));
        }
    }

    private void addAllCertificatesLinks(CertificateDto certificateDto){
        certificateDto.add(linkTo(methodOn(CertificateController.class)
                .showAll(START_PAGE, START_SIZE)).withRel(GET_CERTIFICATES).withType(HttpMethod.GET.name()));
    }

    private void addCertificateWithTagsLink(CertificateDto certificateDto){
        certificateDto.add(linkTo(methodOn(CertificateController.class)
                .getCertificateWithTags(certificateDto.getId()))
                .withRel(GET_CERTIFICATE).withType(HttpMethod.GET.name()));
    }

    private void addCertificateDeleteLink(CertificateDto certificateDto){
        certificateDto.add(linkTo(methodOn(CertificateController.class)
                .deleteCertificate(certificateDto.getId())).withRel(DELETE_CERTIFICATE)
                .withType(HttpMethod.DELETE.name()));
    }

    private Link findPrevShowAllLink(int page, int size){
        return linkTo(methodOn(CertificateController.class)
                .showAll(Pagination.findPrevPage(page), size))
                .withRel("prev").withType(HttpMethod.GET.name());
    }

    private Link findNextShowAllLink(int page, int size, int totalRecords){
        int pages = Pagination.findPages(totalRecords, size);
        int lastPage = Pagination.findLastPage(pages, size, totalRecords);
        return linkTo(methodOn(CertificateController.class)
                .showAll(Pagination.findNextPage(page, lastPage), size))
                .withRel("next").withType(HttpMethod.GET.name());
    }
}
