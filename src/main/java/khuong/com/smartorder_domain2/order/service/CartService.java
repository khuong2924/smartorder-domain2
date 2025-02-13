package khuong.com.smartorder_domain2.order.service;

import khuong.com.smartorder_domain2.order.entity.Cart;
import khuong.com.smartorder_domain2.order.entity.CartItem;
import khuong.com.smartorder_domain2.order.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public Cart createCart(String tableNumber) {
        Cart cart = new Cart();
        cart.setTableNumber(tableNumber);
        cart.setSubtotal(BigDecimal.ZERO);
        return cartRepository.save(cart);
    }

    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public Cart addItemToCart(Long cartId, CartItem item) {
        Cart cart = getCartById(cartId);
        item.setCart(cart);
        cart.getItems().add(item);
        updateCartSubtotal(cart);
        return cartRepository.save(cart);
    }

    public void removeItemFromCart(Long cartId, Long itemId) {
        Cart cart = getCartById(cartId);
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        updateCartSubtotal(cart);
        cartRepository.save(cart);
    }

    public void clearCart(Long cartId) {
        Cart cart = getCartById(cartId);
        cart.getItems().clear();
        cart.setSubtotal(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    private void updateCartSubtotal(Cart cart) {
        BigDecimal subtotal = cart.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setSubtotal(subtotal);
    }
}