package khuong.com.smartorderbeorderdomain.menu.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import khuong.com.smartorderbeorderdomain.menu.dto.MenuSpecialDTO;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;
import khuong.com.smartorderbeorderdomain.menu.repository.MenuItemRepository;
import khuong.com.smartorderbeorderdomain.menu.repository.MenuSpecialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class MenuSpecialService {
    private final MenuSpecialRepository specialRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuSpecialMapper specialMapper;
    private final CacheService cacheService;
    private final WebSocketService webSocketService;

    @Transactional(readOnly = true)
    public List<MenuSpecialDTO> getActiveSpecials() {
        return specialRepository.findByActiveAndEndDateAfter(true, LocalDateTime.now())
                .stream()
                .map(specialMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MenuSpecialDTO createSpecial(CreateMenuSpecialRequest request) {
        validateSpecialRequest(request);

        MenuSpecial special = MenuSpecial.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .discountType(request.getDiscountType())
                .discountValue(request.getDiscountValue())
                .active(true)
                .build();

        // Add menu items to special
        Set<MenuItem> items = menuItemRepository.findAllById(request.getMenuItemIds());
        special.setItems(items);

        MenuSpecial savedSpecial = specialRepository.save(special);
        MenuSpecialDTO dto = specialMapper.toDTO(savedSpecial);

        webSocketService.notifySpecialCreated(dto);
        return dto;
    }

    @Transactional
    public MenuSpecialDTO updateSpecial(Long id, UpdateMenuSpecialRequest request) {
        MenuSpecial special = specialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Special not found"));

        updateSpecialFields(special, request);

        MenuSpecial updatedSpecial = specialRepository.save(special);
        MenuSpecialDTO dto = specialMapper.toDTO(updatedSpecial);

        webSocketService.notifySpecialUpdated(dto);
        return dto;
    }

    @Transactional
    public void deleteSpecial(Long id) {
        MenuSpecial special = specialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Special not found"));

        specialRepository.delete(special);
        webSocketService.notifySpecialDeleted(id);
    }

    private void validateSpecialRequest(CreateMenuSpecialRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new ValidationException("Start date must be before end date");
        }
        if (request.getDiscountValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Discount value must be greater than zero");
        }
        // ... other validations
    }

    private void updateSpecialFields(MenuSpecial special, UpdateMenuSpecialRequest request) {
        if (request.getName() != null) {
            special.setName(request.getName());
        }
        if (request.getDescription() != null) {
            special.setDescription(request.getDescription());
        }
        if (request.getStartDate() != null) {
            special.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            special.setEndDate(request.getEndDate());
        }
        if (request.getDiscountType() != null) {
            special.setDiscountType(request.getDiscountType());
        }
        if (request.getDiscountValue() != null) {
            special.setDiscountValue(request.getDiscountValue());
        }
        if (request.getActive() != null) {
            special.setActive(request.getActive());
        }
    }
}