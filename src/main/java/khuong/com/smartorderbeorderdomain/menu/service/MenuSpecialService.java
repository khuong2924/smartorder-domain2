package khuong.com.smartorderbeorderdomain.menu.service;

import jakarta.transaction.Transactional;
import khuong.com.smartorderbeorderdomain.menu.dto.MenuSpecialDTO;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuSpecial;
import khuong.com.smartorderbeorderdomain.menu.repository.MenuItemRepository;
import khuong.com.smartorderbeorderdomain.menu.repository.MenuSpecialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MenuSpecialService {
    private final MenuSpecialRepository specialRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuSpecialMapper specialMapper;

    public MenuSpecialDTO createSpecial(CreateMenuSpecialRequest request) {
        Set<MenuItem> items = menuItemRepository.findAllById(request.getMenuItemIds())
                .stream()
                .collect(Collectors.toSet());

        if (items.size() != request.getMenuItemIds().size()) {
            throw new ResourceNotFoundException("Some menu items were not found");
        }

        MenuSpecial special = new MenuSpecial();
        special.setName(request.getName());
        special.setDescription(request.getDescription());
        special.setItems(items);
        special.setSpecialPrice(request.getSpecialPrice());
        special.setStartDate(request.getStartDate());
        special.setEndDate(request.getEndDate());
        special.setCreatedBy(SecurityUtils.getCurrentUserId());

        MenuSpecial savedSpecial = specialRepository.save(special);
        log.info("Created new menu special: {}", savedSpecial.getName());
        return specialMapper.toDTO(savedSpecial);
    }

    public List<MenuSpecialDTO> getActiveSpecials() {
        return specialRepository.findActiveSpecials(LocalDateTime.now()).stream()
                .map(specialMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deactivateExpiredSpecials() {
        List<MenuSpecial> expiredSpecials = specialRepository
                .findByActiveTrueAndEndDateAfter(LocalDateTime.now());

        expiredSpecials.forEach(special -> {
            special.setActive(false);
            log.info("Deactivated expired menu special: {}", special.getName());
        });

        specialRepository.saveAll(expiredSpecials);
    }
}