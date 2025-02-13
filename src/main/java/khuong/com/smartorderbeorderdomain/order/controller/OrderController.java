
package khuong.com.smartorderbeorderdomain.order.controller;

import khuong.com.smartorderbeorderdomain.order.dto.request.CreateOrderRequest;
import khuong.com.smartorderbeorderdomain.order.dto.response.OrderResponse;
import khuong.com.smartorderbeorderdomain.order.enums.OrderStatus;
import khuong.com.smartorderbeorderdomain.order.service.OrderService;
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
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        OrderResponse orderResponse = orderService.createOrder(request);
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
}