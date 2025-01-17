package khuong.com.smartorderbeorderdomain.order.clients;

@FeignClient(name = "menu-service", fallback = MenuServiceFallback.class)
public interface MenuServiceClient {
    @GetMapping("/api/menu-items/{id}")
    MenuItemDTO getMenuItem(@PathVariable Long id);

    @GetMapping("/api/menu-items/{id}/availability")
    boolean checkAvailability(@PathVariable Long id);
}
