package khuong.com.smartorder_domain2.order.controller;

import khuong.com.smartorder_domain2.order.dto.request.CheckoutRequest;
import khuong.com.smartorder_domain2.order.dto.response.OrderResponse;
import khuong.com.smartorder_domain2.order.entity.Cart;
import khuong.com.smartorder_domain2.order.entity.CartItem;
import khuong.com.smartorder_domain2.order.entity.Order;
import khuong.com.smartorder_domain2.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }

    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
        return ResponseEntity.ok(cartService.createCart(cart));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<Cart> addItemToCart(@PathVariable Long cartId, @RequestBody CartItem item) {
        return ResponseEntity.ok(cartService.addItemToCart(cartId, item));
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(cartId, itemId));
    }

    @PostMapping("/{cartId}/checkout")
    public ResponseEntity<OrderResponse> checkoutCart(
            @PathVariable Long cartId,
            @RequestBody CheckoutRequest request,
            @RequestHeader("Authorization") String authHeader) {
        Order order = cartService.checkoutCart(cartId, request.getTableId(), request.getWaiterId());
        return ResponseEntity.ok(OrderResponse.fromEntity(order));
    }
}