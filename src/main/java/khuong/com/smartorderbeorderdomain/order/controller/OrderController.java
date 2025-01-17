package khuong.com.smartorderbeorderdomain.order.controller;

import khuong.com.smartorderbeorderdomain.order.dto.OrderDTO;
import khuong.com.smartorderbeorderdomain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDTO>> createOrder(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        OrderDTO order = orderService.createOrder(request);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrder(@PathVariable Long id) {
        OrderDTO order = orderService.getOrder(id);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<ApiResponse<OrderDTO>> addItems(
            @PathVariable Long id,
            @Valid @RequestBody List<OrderItemRequest> items
    ) {
        OrderDTO order = orderService.addItems(id, items);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderDTO>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request
    ) {
        OrderDTO order = orderService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success(order));
    }
}