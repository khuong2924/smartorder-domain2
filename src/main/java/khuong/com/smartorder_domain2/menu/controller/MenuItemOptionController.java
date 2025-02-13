package khuong.com.smartorder_domain2.menu.controller;

import jakarta.validation.Valid;
import khuong.com.smartorder_domain2.menu.dto.request.CreateMenuItemOptionRequest;
import khuong.com.smartorder_domain2.menu.dto.request.UpdateMenuItemOptionRequest;
import khuong.com.smartorder_domain2.menu.dto.response.MenuItemOptionResponse;
import khuong.com.smartorder_domain2.menu.service.MenuItemOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("menu-items/{menuItemId}/options")
@RequiredArgsConstructor
@Validated
public class MenuItemOptionController {
    private final MenuItemOptionService optionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MenuItemOptionResponse> createOption(
            @PathVariable Long menuItemId,
            @Valid @RequestBody CreateMenuItemOptionRequest request) {
        return ResponseEntity.ok(optionService.createOption(menuItemId, request));
    }

    @PutMapping("/{optionId}")
    public ResponseEntity<MenuItemOptionResponse> updateOption(
            @PathVariable Long menuItemId,
            @PathVariable Long optionId,
            @Valid @RequestBody UpdateMenuItemOptionRequest request) {
        return ResponseEntity.ok(optionService.updateOption(menuItemId, optionId, request));
    }

    @GetMapping("/{optionId}")
    public ResponseEntity<MenuItemOptionResponse> getOptionById(
            @PathVariable Long menuItemId,
            @PathVariable Long optionId) {
        return ResponseEntity.ok(optionService.getOptionById(menuItemId, optionId));
    }

    @GetMapping
    public ResponseEntity<List<MenuItemOptionResponse>> getOptionsByMenuItem(
            @PathVariable Long menuItemId) {
        return ResponseEntity.ok(optionService.getOptionsByMenuItem(menuItemId));
    }

    @PatchMapping("/{optionId}/availability")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOptionAvailability(
            @PathVariable Long menuItemId,
            @PathVariable Long optionId,
            @RequestParam boolean available) {
        optionService.updateOptionAvailability(menuItemId, optionId, available);
    }
}
