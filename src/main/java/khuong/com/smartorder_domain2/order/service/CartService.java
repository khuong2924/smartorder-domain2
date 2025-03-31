package khuong.com.smartorder_domain2.order.service;

import khuong.com.smartorder_domain2.menu.repository.MenuItemRepository;
import khuong.com.smartorder_domain2.order.dto.request.CreateCartRequest;
import khuong.com.smartorder_domain2.order.entity.Cart;
import khuong.com.smartorder_domain2.order.entity.CartItem;
import khuong.com.smartorder_domain2.order.entity.Order;
import khuong.com.smartorder_domain2.order.entity.OrderItem;
import khuong.com.smartorder_domain2.order.enums.OrderItemStatus;
import khuong.com.smartorder_domain2.order.enums.OrderStatus;
import khuong.com.smartorder_domain2.order.repository.CartRepository;
import khuong.com.smartorder_domain2.order.repository.OrderRepository;
import khuong.com.smartorder_domain2.table.entity.Table;
import khuong.com.smartorder_domain2.table.enums.TableStatus;
import khuong.com.smartorder_domain2.table.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    @Autowired
    private final CartRepository cartRepository;
    
    @Autowired
    private final OrderRepository orderRepository;
    
    @Autowired
    private final MenuItemRepository menuItemRepository;
    
    @Autowired
    private final TableRepository tableRepository;

    @Transactional(readOnly = true)
    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Cart> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        carts.forEach(cart -> cart.getItems().size());
        return carts;
    }

    public Cart createCart(CreateCartRequest request) {
        // Check if table exists and is available
        Table table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + request.getTableId()));
        
        if (table.getStatus() != TableStatus.AVAILABLE) {
            throw new RuntimeException("Table is not available");
        }
        
        // Create new cart
        Cart cart = new Cart();
        cart.setUserId(request.getUserId());
        cart.setTotalAmount(BigDecimal.ZERO);
        cart.setItems(new ArrayList<>());
        
        return cartRepository.save(cart);
    }

    public Cart addItemToCart(Long cartId, CartItem item) {
        Cart cart = getCartById(cartId);
        
        // Kiểm tra sự tồn tại và tính khả dụng của món ăn
        var menuItem = menuItemRepository.findById(item.getMenuItemId())
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + item.getMenuItemId()));
        
        if (!menuItem.isAvailable()) {
            throw new RuntimeException("Menu item is not available: " + menuItem.getName());
        }
        
        // Đảm bảo giá trong CartItem phản ánh đúng giá hiện tại của MenuItem
        item.setUnitPrice(menuItem.getPrice());
        item.setCart(cart);
        
        // Kiểm tra xem món này đã có trong giỏ hàng chưa
        boolean itemExists = false;
        for (CartItem existingItem : cart.getItems()) {
            if (existingItem.getMenuItemId().equals(item.getMenuItemId())) {
                // Nếu đã có, tăng số lượng
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                itemExists = true;
                break;
            }
        }
        
        
        // Nếu chưa có, thêm mới
        if (!itemExists) {
            if (cart.getItems() == null) {
                cart.setItems(new ArrayList<>());
            }
            cart.getItems().add(item);
        }
        
        // Cập nhật tổng tiền
        updateCartTotal(cart);
        
        return cartRepository.save(cart);
    }

    public Cart removeItemFromCart(Long cartId, Long itemId) {
        Cart cart = getCartById(cartId);
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        
        // Cập nhật tổng tiền
        updateCartTotal(cart);
        
        return cartRepository.save(cart);
    }

    private void updateCartTotal(Cart cart) {
        BigDecimal total = BigDecimal.ZERO;
        if (cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
                total = total.add(item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())));
            }
        }
        cart.setTotalAmount(total);
    }

    @Transactional
    public Order checkoutCart(Long cartId, Long tableId, String waiterId) {
        Cart cart = getCartById(cartId);
        
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot checkout an empty cart");
        }
        
        // Tìm bàn và cập nhật trạng thái
        Table table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + tableId));
        
        if (table.getStatus() != TableStatus.AVAILABLE) {
            throw new RuntimeException("Table is not available");
        }
        
        // Cập nhật trạng thái bàn thành OCCUPIED
        table.setStatus(TableStatus.OCCUPIED);
        tableRepository.save(table);
        
        // Tạo đơn hàng mới
        Order order = new Order();
        order.setTable(table);
        order.setWaiterId(waiterId);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(cart.getTotalAmount());
        
        // Chuyển đổi CartItem thành OrderItem
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> convertToOrderItem(cartItem, order))
                .collect(Collectors.toList());
        
        order.setItems(orderItems);
        
        // Lưu đơn hàng
        Order savedOrder = orderRepository.save(order);
        
        // Xóa giỏ hàng sau khi đã chuyển đổi thành công
        cartRepository.delete(cart);
        
        return savedOrder;
    }
    
    private OrderItem convertToOrderItem(CartItem cartItem, Order order) {
        // Kiểm tra lại sự tồn tại và tính khả dụng của món ăn tại thời điểm chuyển đổi
        var menuItem = menuItemRepository.findById(cartItem.getMenuItemId())
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + cartItem.getMenuItemId()));
        
        if (!menuItem.isAvailable()) {
            throw new RuntimeException("Menu item is no longer available: " + menuItem.getName());
        }
        
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(cartItem.getQuantity());
        
        // Sử dụng giá hiện tại từ MenuItem để đảm bảo tính cập nhật
        orderItem.setUnitPrice(menuItem.getPrice());
        orderItem.setStatus(OrderItemStatus.PENDING);
        orderItem.setSpecialNotes(cartItem.getSpecialNotes());
        
        return orderItem;
    }
}