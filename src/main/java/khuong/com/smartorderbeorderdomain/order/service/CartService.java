
package khuong.com.smartorderbeorderdomain.order.service;

import khuong.com.smartorderbeorderdomain.order.dto.request.CreateCartRequest;
import khuong.com.smartorderbeorderdomain.order.dto.response.CartResponse;
import khuong.com.smartorderbeorderdomain.order.entity.Cart;
import khuong.com.smartorderbeorderdomain.order.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public CartResponse createCart(CreateCartRequest request) {
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID().toString());
        cart.setTableNumber(request.getTableNumber());
        cart.setCustomerId(request.getCustomerId());
        cart.setSubtotal(BigDecimal.ZERO);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());

        Cart savedCart = cartRepository.save(cart);
        return CartResponse.fromEntity(savedCart);
    }

    public CartResponse getCart(String id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + id));
        return CartResponse.fromEntity(cart);
    }

    // Add other methods as needed
}