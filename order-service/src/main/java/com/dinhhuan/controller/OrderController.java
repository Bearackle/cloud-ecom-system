package com.dinhhuan.controller;

import com.dinhhuan.dto.request.OrderRequest;
import com.dinhhuan.dto.request.OrderStatusRequest;
import com.dinhhuan.dto.response.OrderHistoryDto;
import com.dinhhuan.dto.response.OrderResponse;
import com.dinhhuan.dto.response.OrderResponseDetails;
import com.dinhhuan.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @RequestMapping(name = "", method = RequestMethod.POST)
    public ResponseEntity<Map<String,String>> create(@RequestHeader("X-User-Id") String userId, @RequestBody OrderRequest request){
        request.setUserId(Long.parseLong(userId));
       String url =  orderService.createOrder(request);
       if(url == null) {
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
       return ResponseEntity.ok(Map.of("payUrl", url));
    }
    @RequestMapping(name = "", method = RequestMethod.GET)
    public ResponseEntity<List<OrderResponse>> getAllOrders(
            @RequestParam(name = "_page", defaultValue = "1") int page,
            @RequestParam(name = "_perPage", defaultValue = "10") int perPage,
            @RequestParam(name = "_sort", defaultValue = "id") String sort,
            @RequestParam(name = "_order", defaultValue = "ASC") String order
    )
    {
        Pageable pageable = PageRequest.of(
                page - 1,
                perPage,
                order.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC,
                sort
        );
        Page<OrderResponse> orderList = orderService.getAllOrders(pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range", String.format("orders %d-%d/%d",
                (page - 1) * perPage,
                Math.min(page * perPage - 1, (int) orderList.getTotalElements() - 1),
                orderList.getTotalElements()
        ));
        return ResponseEntity.ok()
                .headers(headers)
                .body(orderList.getContent());
    }
//    @RequestMapping(value = "/user", method = RequestMethod.GET)
//    public ResponseEntity<List<OrderResponse>> getAllUserOrder(@RequestHeader("X-User-Id") String userId) {
//        var orderList = orderService.getAllUserOrder(Long.parseLong(userId));
//        if (orderList == null) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        return new ResponseEntity<>(orderList, HttpStatus.OK);
//    }
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<List<OrderHistoryDto>> getAllUserOrder(@RequestHeader("X-User-Id") String userId) {
        var orderList = orderService.getAllHistoryUserOrder(Long.parseLong(userId));
        if (orderList == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }
    @RequestMapping(value = "/{id}/status", method = RequestMethod.PUT)
    public ResponseEntity<OrderResponse> updageStatus(@PathVariable Long id, @RequestBody OrderStatusRequest request){
         var order = orderService.changeStatus(id, request.getStatus().ordinal());
         if (order == null) {
             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
         }
         return new ResponseEntity<>(order, HttpStatus.OK);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<OrderResponseDetails> getOrder(@PathVariable Long id){
        var order = orderService.getOrderByIdIncluded(id);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
