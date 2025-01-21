package khuong.com.smartorderbeorderdomain.menu.controller;
import
        org.springframework.data.domain.Pageable;
import jakarta.validation.Valid;
import khuong.com.smartorderbeorderdomain.menu.dto.request.CreateMenuItemRequest;
import khuong.com.smartorderbeorderdomain.menu.dto.request.UpdateMenuItemRequest;
import khuong.com.smartorderbeorderdomain.menu.dto.response.MenuItemResponse;
import khuong.com.smartorderbeorderdomain.menu.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/menu-items")
@RequiredArgsConstructor
@Validated
public class MenuItemController {
    private final MenuItemService menuItemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MenuItemResponse> createMenuItem(
            @Valid @RequestBody CreateMenuItemRequest request) throws IOException {
        return ResponseEntity.ok(menuItemService.createMenuItem(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponse> updateMenuItem(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMenuItemRequest request) {
        return ResponseEntity.ok(menuItemService.updateMenuItem(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> getMenuItemById(@PathVariable Long id) {
        return ResponseEntity.ok(menuItemService.getMenuItemById(id));
    }

    @GetMapping
    public ResponseEntity<Page<MenuItemResponse>> getAllMenuItems(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(menuItemService.getAllMenuItems(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MenuItemResponse>> searchMenuItems(
            @RequestParam String keyword) {
        return ResponseEntity.ok(menuItemService.searchMenuItems(keyword));
    }

    @PatchMapping("/{id}/availability")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMenuItemAvailability(
            @PathVariable Long id,
            @RequestParam boolean available) {
        menuItemService.updateMenuItemAvailability(id, available);
    }

//    @GetMapping("/category/{categoryId}")
//    public ResponseEntity<List<MenuItemResponse>> getMenuItemsByCategory(
//            @PathVariable Long categoryId) {
//        return ResponseEntity.ok(menuItemService.getMenuItemsByCategory(categoryId));
//    }
//
//    @GetMapping("/filter")
//    public ResponseEntity<List<MenuItemResponse>> filterMenuItems(
//            @RequestParam(required = false) Boolean vegetarian,
//            @RequestParam(required = false) Boolean spicy,
//            @RequestParam(required = false) BigDecimal minPrice,
//            @RequestParam(required = false) BigDecimal maxPrice) {
//        return ResponseEntity.ok(menuItemService.filterMenuItems(vegetarian, spicy, minPrice, maxPrice));
//    }
}