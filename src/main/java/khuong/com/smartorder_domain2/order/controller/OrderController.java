package khuong.com.smartorder_domain2.order.controller;

import khuong.com.smartorder_domain2.order.dto.request.CreateOrderRequest;
import khuong.com.smartorder_domain2.order.dto.response.OrderResponse;
import khuong.com.smartorder_domain2.order.enums.OrderStatus;
import khuong.com.smartorder_domain2.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody CreateOrderRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        OrderResponse orderResponse = orderService.createOrder(request, authHeader);
        return ResponseEntity.ok(orderResponse);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable String orderId, @RequestParam OrderStatus status) {
        OrderResponse orderResponse = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrder();
    }
    
    @PostMapping("/{id}/close-table")
    public ResponseEntity<Void> closeTable(@PathVariable Long id) {
        orderService.closeTable(id);
        return ResponseEntity.noContent().build();
    }
}