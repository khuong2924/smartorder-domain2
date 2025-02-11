package khuong.com.smartorderbeorderdomain.order.controller;

import khuong.com.smartorderbeorderdomain.order.entity.Cart;
import khuong.com.smartorderbeorderdomain.order.entity.CartItem;
import khuong.com.smartorderbeorderdomain.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {
    @Autowired
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestParam String tableNumber) {
        return ResponseEntity.ok(cartService.createCart(tableNumber));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<Cart> addItemToCart(@PathVariable Long cartId, @RequestBody CartItem item) {
        return ResponseEntity.ok(cartService.addItemToCart(cartId, item));
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        cartService.removeItemFromCart(cartId, itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
}