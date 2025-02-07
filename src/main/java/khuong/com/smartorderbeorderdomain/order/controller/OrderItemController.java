
package khuong.com.smartorderbeorderdomain.order.controller;

import khuong.com.smartorderbeorderdomain.order.dto.response.OrderItemResponse;
import khuong.com.smartorderbeorderdomain.order.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order-items")
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderItemService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponse> getOrderItem(@PathVariable String id) {
        OrderItemResponse orderItemResponse = orderItemService.getOrderItem(id);
        return ResponseEntity.ok(orderItemResponse);
    }

    // Add other endpoints as needed
}