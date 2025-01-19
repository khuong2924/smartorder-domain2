package khuong.com.smartorderbeorderdomain.menu.service;

import khuong.com.smartorderbeorderdomain.menu.dto.request.CreateMenuItemOptionRequest;
import khuong.com.smartorderbeorderdomain.menu.dto.request.CreateOptionChoiceRequest;
import khuong.com.smartorderbeorderdomain.menu.dto.response.MenuItemOptionResponse;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItemOption;
import khuong.com.smartorderbeorderdomain.menu.entity.OptionChoice;
import khuong.com.smartorderbeorderdomain.menu.repository.MenuItemOptionRepository;
import khuong.com.smartorderbeorderdomain.menu.repository.OptionChoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuItemOptionService {
    private final MenuItemOptionRepository optionRepository;
    private final OptionChoiceRepository choiceRepository;
    private final MenuItemService menuItemService;

    @Transactional
    public MenuItemOptionResponse createOption(Long menuItemId, CreateMenuItemOptionRequest request) {
        MenuItem menuItem = menuItemService.findMenuItemById(menuItemId);

        MenuItemOption option = MenuItemOption.builder()
                .menuItem(menuItem)
                .name(request.getName())
                .description(request.getDescription())
                .additionalPrice(request.getAdditionalPrice())
                .defaultOption(request.isDefaultOption())
                .optionType(request.getOptionType())
                .minSelections(request.getMinSelections())
                .maxSelections(request.getMaxSelections())
                .available(true)
                .build();

        option = optionRepository.save(option);

        if (request.getChoices() != null) {
            List<OptionChoice> choices = createChoices(option, request.getChoices());
            option.setChoices(choices);
        }

        return MenuItemOptionResponse.fromEntity(option);
    }

    private List<OptionChoice> createChoices(MenuItemOption option, List<CreateOptionChoiceRequest> requests) {
        return requests.stream()
                .map(request -> OptionChoice.builder()
                        .option(option)
                        .name(request.getName())
                        .description(request.getDescription())
                        .additionalPrice(request.getAdditionalPrice())
                        .available(true)
                        .build())
                .map(choiceRepository::save)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateOptionAvailability(Long optionId, boolean available) {
        MenuItemOption option = findOptionById(optionId);
        option.setAvailable(available);
        optionRepository.save(option);
    }

    private MenuItemOption findOptionById(Long id) {
        return optionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Option not found with id: " + id));
    }
}