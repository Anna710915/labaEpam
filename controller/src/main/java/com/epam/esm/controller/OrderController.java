package com.epam.esm.controller;

import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.exception.CustomExternalException;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.security.JwtUtil;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.MessageLanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * The type Order controller.
 */
@RestController
@RequestMapping("/certificates")
public class OrderController {

    private static final String GET_ORDER = "get_order";
    private static final String GET_ORDERS = "get_orders";
    private static final int START_PAGE = 1;
    private static final int START_SIZE = 2;

    private final JwtUtil jwtUtil;
    private final OrderService orderService;
    private final UserService userService;
    private final MessageLanguageUtil messageLanguageUtil;

    @Autowired
    public OrderController(OrderService orderService,
                           UserService userService,
                           MessageLanguageUtil messageLanguageUtil,
                           JwtUtil jwtUtil){
        this.orderService = orderService;
        this.messageLanguageUtil = messageLanguageUtil;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * Create order response entity.
     *
     * @param orderDto the order dto
     * @return the response entity
     */
    @PostMapping(value = "/order", consumes = "application/json")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto, @RequestHeader("Authorization") String authHeader){
        String username = jwtUtil.getUsername(authHeader);
        UserDto userDto = userService.findUserByName(username);
        orderDto.setUserId(userDto.getUserId());
        long orderId = orderService.createOrder(orderDto);
        HttpStatus httpStatus = orderId < 1 ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.CREATED;
        if(httpStatus == HttpStatus.INTERNAL_SERVER_ERROR){
            throw new CustomExternalException(messageLanguageUtil.getMessage("internal_error.create_order") + orderDto);
        }
        orderDto.add(linkTo(methodOn(CertificateController.class)
                .showAll(START_PAGE, START_SIZE)).withRel("get_certificates")
                .withType(HttpMethod.GET.name()));
        return new ResponseEntity<>(orderDto, httpStatus);
    }

    /**
     * Find paginated user orders collection model.
     *
     * @param page   the page
     * @param size   the size
     * @return the collection model
     */
    @GetMapping(value = "/orders/user", produces = "application/json")
    public CollectionModel<OrderDto> findPaginatedUserOrders(@RequestParam(value = "page") int page,
                                                             @RequestParam(value = "size") int size,
                                                             @RequestHeader("Authorization") String authHeader){
        String username = jwtUtil.getUsername(authHeader);
        UserDto userDto = userService.findUserByName(username);
        long userId = userDto.getUserId();
        int offset = Pagination.offset(page, size);
        int totalRecords = orderService.findTotalRecords(userId);
        int pages = Pagination.findPages(totalRecords, size);
        int lastPage = Pagination.findLastPage(pages, size, totalRecords);
        List<OrderDto> orderDtos = orderService.findPaginatedUserOrders(userId, size, offset);
        addUserOrderLinks(orderDtos);
        Link linkPrevPage = linkTo(methodOn(OrderController.class)
                .findPaginatedUserOrders(Pagination.findPrevPage(page), size, authHeader))
                .withRel("prev").withType(HttpMethod.GET.name());
        Link linkNextPage = linkTo(methodOn(OrderController.class)
                .findPaginatedUserOrders(Pagination.findNextPage(page, lastPage), size, authHeader))
                .withRel("next").withType(HttpMethod.GET.name());
        return CollectionModel.of(orderDtos, linkPrevPage, linkNextPage);
    }

    /**
     * Find user order order dto.
     *
     * @param orderId the order id
     * @return the order dto
     */
    @GetMapping(value = "/orders/user/{id}", produces = "application/json")
    public OrderDto findUserOrder(@PathVariable("id") long orderId, @RequestHeader("Authorization") String authHeader){
        String username = jwtUtil.getUsername(authHeader);
        UserDto userDto = userService.findUserByName(username);
        long userId = userDto.getUserId();
        OrderDto orderDto = orderService.findUserOrder(orderId);
        orderDto.add(linkTo(methodOn(OrderController.class)
                .findPaginatedUserOrders(START_PAGE, START_SIZE, authHeader))
                .withRel(GET_ORDERS).withType(HttpMethod.GET.name()));
        return orderDto;
    }

    private void addUserOrderLinks(List<OrderDto> orderDtos){
        for(OrderDto orderDto : orderDtos){
            orderDto.add(linkTo(methodOn(OrderController.class)
                    .findUserOrder(orderDto.getOrderId(), orderDto.getUserId()))
                    .withRel(GET_ORDER).withType(HttpMethod.GET.name()));
        }
    }
}
