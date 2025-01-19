package khuong.com.smartorderbeorderdomain.menu.service;

import khuong.com.smartorderbeorderdomain.configs.CacheConstants;
import khuong.com.smartorderbeorderdomain.menu.dto.request.CreateMenuItemOptionRequest;
import khuong.com.smartorderbeorderdomain.menu.dto.request.CreateOptionChoiceRequest;
import khuong.com.smartorderbeorderdomain.menu.dto.request.UpdateMenuItemOptionRequest;
import khuong.com.smartorderbeorderdomain.menu.dto.request.UpdateOptionChoiceRequest;
import khuong.com.smartorderbeorderdomain.menu.dto.response.MenuItemOptionResponse;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItemOption;
import khuong.com.smartorderbeorderdomain.menu.entity.OptionChoice;
import khuong.com.smartorderbeorderdomain.menu.enums.OptionType;
import khuong.com.smartorderbeorderdomain.menu.repository.MenuItemOptionRepository;
import khuong.com.smartorderbeorderdomain.menu.repository.MenuItemRepository;
import khuong.com.smartorderbeorderdomain.menu.repository.OptionChoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuItemOptionService {
    @Autowired
    private final MenuItemOptionRepository optionRepository;
    @Autowired
    private final OptionChoiceRepository choiceRepository;
    @Autowired
    private final MenuItemRepository menuItemRepository;

    @Transactional
    public MenuItemOptionResponse createOption(Long menuItemId, CreateMenuItemOptionRequest request) {
        log.info("Creating new option for menu item id: {}", menuItemId);

        MenuItem menuItem = findMenuItemById(menuItemId);
        validateOptionRequest(request);

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

        if (request.getChoices() != null && !request.getChoices().isEmpty()) {
            List<OptionChoice> choices = createChoices(option, request.getChoices());
            option.setChoices(choices);
        }

        return MenuItemOptionResponse.fromEntity(option);
    }

    @Transactional
    public MenuItemOptionResponse updateOption(Long menuItemId, Long optionId,
                                               UpdateMenuItemOptionRequest request) {
        log.info("Updating option id: {} for menu item id: {}", optionId, menuItemId);

        MenuItemOption option = findOptionByIdAndMenuItemId(optionId, menuItemId);
        validateUpdateOptionRequest(request);

        updateOptionFields(option, request);

//        if (request.getChoices() != null) {
//            updateChoices(option, request.getChoices());
//        }

        option = optionRepository.save(option);
        return MenuItemOptionResponse.fromEntity(option);
    }

    @Cacheable(cacheNames = CacheConstants.MENU_OPTION_CACHE,
            condition = "#menuItemId != null && #optionId != null")
    public MenuItemOptionResponse getOptionById(Long menuItemId, Long optionId) {
        return MenuItemOptionResponse.fromEntity(
                findOptionByIdAndMenuItemId(optionId, menuItemId));
    }

    @Cacheable(cacheNames = CacheConstants.MENU_OPTION_CACHE,
            condition = "#menuItemId != null")
    public List<MenuItemOptionResponse> getOptionsByMenuItem(Long menuItemId) {
        findMenuItemById(menuItemId); // Validate menuItem exists
        return optionRepository.findByMenuItemId(menuItemId).stream()
                .map(MenuItemOptionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateOptionAvailability(Long menuItemId, Long optionId, boolean available) {
        log.info("Updating availability to {} for option id: {}", available, optionId);

        MenuItemOption option = findOptionByIdAndMenuItemId(optionId, menuItemId);
        option.setAvailable(available);
        optionRepository.save(option);
    }

    @Transactional
    public void deleteOption(Long menuItemId, Long optionId) {
        log.info("Deleting option id: {} from menu item id: {}", optionId, menuItemId);

        MenuItemOption option = findOptionByIdAndMenuItemId(optionId, menuItemId);
        choiceRepository.deleteByOptionId(optionId);
        optionRepository.delete(option);
    }

    private List<OptionChoice> createChoices(MenuItemOption option,
                                             List<CreateOptionChoiceRequest> requests) {
        return requests.stream()
                .map(request -> OptionChoice.builder()
                        .option(option)
                        .name(request.getName())
                        .description(request.getDescription())
                        .additionalPrice(request.getAdditionalPrice())
                        .displayOrder(request.getDisplayOrder())
                        .available(true)
                        .build())
                .map(choiceRepository::save)
                .collect(Collectors.toList());
    }

    private void updateChoices(MenuItemOption option, List<UpdateOptionChoiceRequest> requests) {
        // Delete choices not in the update request
        List<Long> updatedChoiceIds = requests.stream()
                .map(UpdateOptionChoiceRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        option.getChoices().stream()
                .filter(choice -> !updatedChoiceIds.contains(choice.getId()))
                .forEach(choiceRepository::delete);

        // Update existing and create new choices
        option.setChoices(requests.stream()
                .map(request -> {
                    if (request.getId() != null) {
                        return updateChoice(option, request);
                    } else {
                        return createChoice(option, request);
                    }
                })
                .collect(Collectors.toList()));
    }

    private OptionChoice updateChoice(MenuItemOption option, UpdateOptionChoiceRequest request) {
        OptionChoice choice = choiceRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Choice not found with id: " + request.getId()));

        choice.setName(request.getName());
        choice.setDescription(request.getDescription());
        choice.setAdditionalPrice(request.getAdditionalPrice());
        choice.setDisplayOrder(request.getDisplayOrder());
        if (request.getAvailable() != null) {
            choice.setAvailable(request.getAvailable());
        }

        return choiceRepository.save(choice);
    }

    private OptionChoice createChoice(MenuItemOption option, UpdateOptionChoiceRequest request) {
        return choiceRepository.save(OptionChoice.builder()
                .option(option)
                .name(request.getName())
                .description(request.getDescription())
                .additionalPrice(request.getAdditionalPrice())
                .displayOrder(request.getDisplayOrder())
                .available(true)
                .build());
    }

    private MenuItem findMenuItemById(Long menuItemId) {
        return menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Menu item not found with id: " + menuItemId));
    }

    private MenuItemOption findOptionByIdAndMenuItemId(Long optionId, Long menuItemId) {
        return optionRepository.findByIdAndMenuItemId(optionId, menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Option not found with id: " + optionId + " for menu item id: " + menuItemId));
    }

    private void validateOptionRequest(CreateMenuItemOptionRequest request) {
        if (request.getMinSelections() != null && request.getMaxSelections() != null) {
            if (request.getMinSelections() < 0) {
                throw new IllegalArgumentException("Minimum selections cannot be negative");
            }
            if (request.getMaxSelections() < request.getMinSelections()) {
                throw new IllegalArgumentException(
                        "Maximum selections cannot be less than minimum selections");
            }
        }

        if (request.getChoices() != null && !request.getChoices().isEmpty()
                && request.getMaxSelections() != null
                && request.getMaxSelections() > request.getChoices().size()) {
            throw new IllegalArgumentException(
                    "Maximum selections cannot be greater than number of choices");
        }
    }

    private void validateUpdateOptionRequest(UpdateMenuItemOptionRequest request) {
        if (request.getMinSelections() != null && request.getMaxSelections() != null) {
            if (request.getMinSelections() < 0) {
                throw new IllegalArgumentException("Minimum selections cannot be negative");
            }
            if (request.getMaxSelections() < request.getMinSelections()) {
                throw new IllegalArgumentException(
                        "Maximum selections cannot be less than minimum selections");
            }
        }
    }

    private void updateOptionFields(MenuItemOption option, UpdateMenuItemOptionRequest request) {
        if (request.getName() != null) option.setName(request.getName());
        if (request.getDescription() != null) option.setDescription(request.getDescription());
        if (request.getAdditionalPrice() != null) option.setAdditionalPrice(request.getAdditionalPrice());
        if (request.getDefaultOption() != null) option.setDefaultOption(request.getDefaultOption());
        if (request.getOptionType() != null) option.setOptionType(OptionType.valueOf(request.getOptionType()));
        if (request.getMinSelections() != null) option.setMinSelections(request.getMinSelections());
        if (request.getMaxSelections() != null) option.setMaxSelections(request.getMaxSelections());
        if (request.getAvailable() != null) option.setAvailable(request.getAvailable());
    }
}