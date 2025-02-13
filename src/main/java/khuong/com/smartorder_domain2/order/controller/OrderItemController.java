
package khuong.com.smartorder_domain2.order.controller;

import khuong.com.smartorder_domain2.order.dto.request.OrderItemRequest;
import khuong.com.smartorder_domain2.order.dto.response.OrderItemResponse;
import khuong.com.smartorder_domain2.order.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderItems")
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderItemService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponse> getOrderItem(@PathVariable String id) {
        OrderItemResponse orderItemResponse = orderItemService.getOrderItem(id);
        return ResponseEntity.ok(orderItemResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrderItemResponse>> getAllOrderItems() {
        List<OrderItemResponse> orderItemResponses = orderItemService.getAllOrderItems();
        return ResponseEntity.ok(orderItemResponses);
    }

    @PostMapping
    public ResponseEntity<OrderItemResponse> createOrderItem(@RequestBody OrderItemRequest request) {
        OrderItemResponse orderItemResponse = orderItemService.createOrderItem(request);
        return ResponseEntity.ok(orderItemResponse);
    }
}