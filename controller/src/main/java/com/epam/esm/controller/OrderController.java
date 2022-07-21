package com.epam.esm.controller;

import com.epam.esm.exception.CustomForbiddenException;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.exception.CustomExternalException;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.permission.UserPermission;
import com.epam.esm.security.JwtUser;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.MessageLanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * The type Order controller.
 */
@RestController
@RequestMapping("/certificates")
@CrossOrigin(maxAge = 3600)
public class OrderController {

    private static final String GET_ORDER = "get_order";
    private static final String GET_ORDERS = "get_orders";
    private static final int START_PAGE = 1;
    private static final int START_SIZE = 10;

    private final OrderService orderService;
    private final MessageLanguageUtil messageLanguageUtil;

    /**
     * Instantiates a new Order controller.
     *
     * @param orderService        the order service
     * @param messageLanguageUtil the message language util
     */
    @Autowired
    public OrderController(OrderService orderService,
                           MessageLanguageUtil messageLanguageUtil){
        this.orderService = orderService;
        this.messageLanguageUtil = messageLanguageUtil;
    }

    /**
     * Create order response entity.
     *
     * @param orderDto the order dto
     * @param jwtUser  the jwt user
     * @return the response entity
     */
    @PostMapping(value = "/order")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto, @AuthenticationPrincipal JwtUser jwtUser){
        orderDto.setUserId(jwtUser.getUserId());
        long orderId = orderService.createOrder(orderDto);
        HttpStatus httpStatus = orderId < 1 ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.CREATED;
        if(httpStatus == HttpStatus.INTERNAL_SERVER_ERROR){
            throw new CustomExternalException(messageLanguageUtil.getMessage("internal_error.create_order") + orderDto);
        }
        orderDto.add(linkTo(methodOn(CertificateController.class)
                .findAll(null, null, null, null, START_PAGE, START_SIZE)).withRel("get_certificates")
                        .expand().withType(HttpMethod.GET.name()));
        return new ResponseEntity<>(orderDto, httpStatus);
    }

    /**
     * Find paginated user orders collection model.
     *
     * @param page    the page
     * @param size    the size
     * @param jwtUser the jwt user
     * @return the collection model
     */
    @GetMapping(value = "/orders/user", produces = "application/json")
    public CollectionModel<OrderDto> findPaginatedUserOrders(@RequestParam(value = "page") int page,
                                                             @RequestParam(value = "size") int size,
                                                             @AuthenticationPrincipal JwtUser jwtUser){
        long userId = jwtUser.getUserId();
        int totalRecords = orderService.findTotalRecords(userId);
        int pages = Pagination.findPages(totalRecords, size);
        int lastPage = Pagination.findLastPage(pages, size, totalRecords);
        List<OrderDto> orderDtos = orderService.findPaginatedUserOrders(userId, page, size);
        addUserOrderLinks(orderDtos, jwtUser);
        Link linkPrevPage = linkTo(methodOn(OrderController.class)
                .findPaginatedUserOrders(Pagination.findPrevPage(page), size, jwtUser))
                .withRel("prev").withType(HttpMethod.GET.name());
        Link linkNextPage = linkTo(methodOn(OrderController.class)
                .findPaginatedUserOrders(Pagination.findNextPage(page, lastPage), size, jwtUser))
                .withRel("next").withType(HttpMethod.GET.name());
        return CollectionModel.of(orderDtos, linkPrevPage, linkNextPage);
    }

    /**
     * Find user order dto.
     *
     * @param orderId the order id
     * @param jwtUser the jwt user
     * @return the order dto
     */
    @GetMapping(value = "/orders/user/{id}", produces = "application/json")
    public OrderDto findUserOrder(@PathVariable("id") long orderId, @AuthenticationPrincipal JwtUser jwtUser){
        OrderDto orderDto = orderService.findUserOrder(orderId);
        checkOrderAccessByUserIdOrRole(orderDto.getUserId(), jwtUser);
        orderDto.add(linkTo(methodOn(OrderController.class)
                .findPaginatedUserOrders(START_PAGE, START_SIZE, jwtUser))
                .withRel(GET_ORDERS).withType(HttpMethod.GET.name()));
        return orderDto;
    }

    private void addUserOrderLinks(List<OrderDto> orderDtos, JwtUser jwtUser){
        for(OrderDto orderDto : orderDtos){
            orderDto.add(linkTo(methodOn(OrderController.class)
                    .findUserOrder(orderDto.getOrderId(), jwtUser))
                    .withRel(GET_ORDER).withType(HttpMethod.GET.name()));
        }
    }

    private void checkOrderAccessByUserIdOrRole(long userId, JwtUser jwtUser){
        if(userId != jwtUser.getUserId()){
            checkUserRole(jwtUser);
        }
    }

    private void checkUserRole(JwtUser jwtUser){
        Optional<? extends GrantedAuthority>  authority = jwtUser.getAuthorities().stream()
                .filter(grantedAuthority -> grantedAuthority.getAuthority().equals(UserPermission.ADMIN.name()))
                .findAny();
        if(authority.isEmpty()) {
            throw new CustomForbiddenException(messageLanguageUtil.getMessage("forbidden.access_denied"));
        }
    }
}
