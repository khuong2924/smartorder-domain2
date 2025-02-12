package khuong.com.smartorderbeorderdomain.order.service;

import khuong.com.smartorderbeorderdomain.order.entity.CartItem;
import khuong.com.smartorderbeorderdomain.order.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {
    @Autowired
    private final CartItemRepository cartItemRepository;

    public CartItem getCartItemById(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + id));
    }

    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    public CartItem createCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public CartItem updateCartItem(Long id, CartItem cartItemDetails) {
        CartItem cartItem = getCartItemById(id);
        cartItem.setMenuItemId(cartItemDetails.getMenuItemId());
        cartItem.setQuantity(cartItemDetails.getQuantity());
        cartItem.setUnitPrice(cartItemDetails.getUnitPrice());
        return cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(Long id) {
        CartItem cartItem = getCartItemById(id);
        cartItemRepository.delete(cartItem);
    }
}