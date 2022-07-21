package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDurationDto;
import com.epam.esm.dto.CertificatePriceDto;
import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.exception.CustomExternalException;
import com.epam.esm.exception.CustomNotFoundException;
import com.epam.esm.model.dto.SortDto;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.MessageLanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
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
@CrossOrigin(maxAge = 3600)
@RequestMapping("/certificates")
public class CertificateController {

    private static final String GET_CERTIFICATE = "get_certificate";
    private static final String DELETE_CERTIFICATE = "delete_certificate";
    private static final String GET_CERTIFICATES = "get_certificates";
    private static final String START_PATH = "/certificates/";
    private static final int START_PAGE = 1;
    private static final int START_SIZE = 10;
    private final CertificateService certificateService;
    private final MessageLanguageUtil messageLanguageUtil;

    /**
     * Instantiates a new Controller.
     *
     * @param certificateService      the certificate service
     * @param messageLanguageUtil     the message language util
     */
    @Autowired
    public CertificateController(CertificateService certificateService,
                                 MessageLanguageUtil messageLanguageUtil){
        this.certificateService = certificateService;
        this.messageLanguageUtil = messageLanguageUtil;
    }

    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<CertificateDto> create(UriComponentsBuilder builder,
                                                  @Valid @RequestBody CertificateDto certificateDto){
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

    @GetMapping(value = "/all", produces = "application/json")
    public CollectionModel<CertificateDto> findAll(@RequestParam(value = "part", required = false) String part,
                                                   @RequestParam(value = "tags", required = false) String tags,
                                                   @RequestParam(value = "name", required = false) String name,
                                                   @RequestParam(value = "date", required = false) String date,
                                                   @RequestParam(value = "page") int page,
                                                   @RequestParam(value = "size") int size){
        SortDto sort = new SortDto(date, name);
        List<CertificateDto> certificateDtos = certificateService.findAll(part, tags, sort, page, size);
        addLinksForCertificatesList(certificateDtos);
        int totalRecords = certificateService.findCountCertificateRecords(part, tags, sort);
        Link prev = findPrevShowAllLink(page, size, part, tags, name, date);
        Link next = findNextShowAllLink(page, size, totalRecords, part, tags, name, date);
        Link first = findFirstPage(page, size, part, tags, name, date);
        Link last = findLastPage(page, size, totalRecords, part, tags, name, date);
        return CollectionModel.of(certificateDtos,  first, prev, next, last);
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
        int totalRecords = certificateService.findCountCertificatesByTagName(name);
        int pages = Pagination.findPages(totalRecords, size);
        int lastPage = Pagination.findLastPage(pages, size, totalRecords);
        List<CertificateDto> certificateDtos = certificateService.showByTagName(page, size, name);
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
        int totalRecords = certificateService.findCountByPartNameOrDescription(part);
        int pages = Pagination.findPages(totalRecords, size);
        int lastPage = Pagination.findLastPage(pages, size, totalRecords);
        List<CertificateDto> certificateDtos =  certificateService.showByPartWord(page, size, part);
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
     * Update certificate certificate dto.
     *
     * @param id                the id
     * @param updateCertificate the update certificate
     * @return the certificate dto
     */
    @PutMapping(value = "/certificate/{id}/update", consumes = "application/json")
    public CertificateDto updateCertificate(@PathVariable long id,
                                            @Valid @RequestBody CertificateDto updateCertificate){
        certificateService.update(updateCertificate, id);
        CertificateDto certificateDto = certificateService.showCertificateWithTags(id);
        addLinksForCertificate(certificateDto);
        return certificateDto;
    }

    /**
     * Update certificate price certificate dto.
     *
     * @param id    the id
     * @param priceDto the price
     * @return the certificate dto
     */
    @PatchMapping(value = "/certificate/{id}/price", produces = "application/json")
    public CertificateDto updateCertificatePrice(@PathVariable long id, @Valid @RequestBody CertificatePriceDto priceDto){
        certificateService.updateCertificatePrice(id, priceDto.getPrice());
        CertificateDto certificateDto = certificateService.showCertificateWithTags(id);
        addLinksForCertificate(certificateDto);
        return certificateDto;
    }

    /**
     * Update certificate duration certificate dto.
     *
     * @param id       the id
     * @param durationDto the duration
     * @return the certificate dto
     */
    @PatchMapping(value = "/certificate/{id}/duration", produces = "application/json")
    public CertificateDto updateCertificateDuration(@PathVariable long id, @Valid @RequestBody CertificateDurationDto durationDto){
        certificateService.updateCertificateDuration(id, durationDto.getDuration());
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
                .findAll(null, null, null, null, START_PAGE, START_SIZE))
                .withRel(GET_CERTIFICATES).expand().withType(HttpMethod.GET.name()));
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

    private Link findPrevShowAllLink(int page, int size, String part, String tags, String name, String date){
        return linkTo(methodOn(CertificateController.class)
                .findAll(part, tags, name, date, Pagination.findPrevPage(page), size))
                .withRel("prev").withType(HttpMethod.GET.name()).expand().withName(String.valueOf(page));

    }

    private Link findNextShowAllLink(int page, int size, int totalRecords, String part, String tags, String name, String date){
        int pages = Pagination.findPages(totalRecords, size);
        int lastPage = Pagination.findLastPage(pages, size, totalRecords);
        int nextPage = Pagination.findNextPage(page, lastPage);
        return linkTo(methodOn(CertificateController.class)
                .findAll(part, tags, name, date, nextPage, size))
                .withRel("next").withType(HttpMethod.GET.name())
                .expand().withName(String.valueOf(nextPage));
    }

    private Link findFirstPage(int page, int size, String part, String tags, String name, String date){
        return linkTo(methodOn(CertificateController.class)
                .findAll(part, tags, name, date, 1, size))
                .withRel("first").withType(HttpMethod.GET.name()).expand().withName(String.valueOf(page));
    }

    private Link findLastPage(int page, int size, int totalRecords, String part, String tags, String name, String date){
        int pages = Pagination.findPages(totalRecords, size);
        int lastPage = Pagination.findLastPage(pages, size, totalRecords);
        return linkTo(methodOn(CertificateController.class)
                .findAll(part, tags, name, date, lastPage, size))
                .withRel("last").withType(HttpMethod.GET.name()).expand().withName(String.valueOf(page));
    }
}
