package khuong.com.smartorderbeorderdomain.menu.service;

import khuong.com.smartorderbeorderdomain.menu.dto.request.CreateMenuItemOptionRequest;
import khuong.com.smartorderbeorderdomain.menu.dto.request.UpdateMenuItemOptionRequest;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItemOption;
import khuong.com.smartorderbeorderdomain.menu.entity.OptionChoice;
import khuong.com.smartorderbeorderdomain.menu.mapper.MenuMapper;
import khuong.com.smartorderbeorderdomain.menu.repository.MenuItemOptionRepository;
import khuong.com.smartorderbeorderdomain.menu.repository.OptionChoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuItemOptionService {
    private final MenuItemOptionRepository optionRepository;
    private final OptionChoiceRepository choiceRepository;
    private final MenuMapper menuMapper;

    @Transactional
    public List<MenuItemOption> createOptions(MenuItem menuItem,
                                              List<CreateMenuItemOptionRequest> requests) {
        return requests.stream()
                .map(request -> createOption(menuItem, request))
                .collect(Collectors.toList());
    }

    @Transactional
    public MenuItemOption createOption(MenuItem menuItem, CreateMenuItemOptionRequest request) {
        MenuItemOption option = buildOption(menuItem, request);
        option = optionRepository.save(option);

        if (request.getChoices() != null) {
            List<OptionChoice> choices = createChoices(option, request.getChoices());
            option.setChoices(choices);
        }

        return option;
    }

    @Transactional
    public void updateOptions(MenuItem menuItem, List<UpdateMenuItemOptionRequest> requests) {
        // Remove old options not in the update request
        List<Long> updatedOptionIds = requests.stream()
                .map(UpdateMenuItemOptionRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        menuItem.getOptions().stream()
                .filter(option -> !updatedOptionIds.contains(option.getId()))
                .forEach(optionRepository::delete);

        // Update existing and create new options
        requests.forEach(request -> {
            if (request.getId() != null) {
                updateOption(request.getId(), request);
            } else {
                createOption(menuItem, menuMapper.toCreateRequest(request));
            }
        });
    }

    // ... other methods and helpers
}
