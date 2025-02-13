package khuong.com.smartorder_domain2.menu.service;


import khuong.com.smartorder_domain2.menu.dto.request.OptionChoiceRequest;
import khuong.com.smartorder_domain2.menu.dto.response.OptionChoiceResponse;
import khuong.com.smartorder_domain2.menu.entity.OptionChoice;
import khuong.com.smartorder_domain2.menu.repository.MenuItemOptionRepository;
import khuong.com.smartorder_domain2.menu.repository.OptionChoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OptionChoiceService {
    @Autowired
    private OptionChoiceRepository optionChoiceRepository;
    @Autowired
    private MenuItemOptionRepository menuItemOptionRepository;

    public OptionChoiceResponse createOptionChoice(OptionChoiceRequest request) {
        OptionChoice optionChoice = new OptionChoice();
        optionChoice.setOption(menuItemOptionRepository.findById(request.getMenuItemOptionId())
                .orElseThrow(() -> new RuntimeException("MenuItemOption not found")));
        optionChoice.setName(request.getName());
        optionChoice.setDescription(request.getDescription());
        optionChoice.setAdditionalPrice(request.getAdditionalPrice());

        optionChoice.setAvailable(request.isAvailable());
        optionChoice.setDisplayOrder(request.getDisplayOrder());

        OptionChoice savedOptionChoice = optionChoiceRepository.save(optionChoice);
        return OptionChoiceResponse.fromEntity(savedOptionChoice);
    }

    public OptionChoiceResponse updateOptionChoice(Long id, OptionChoiceRequest request) {
        OptionChoice optionChoice = optionChoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OptionChoice not found"));

        optionChoice.setOption(menuItemOptionRepository.findById(request.getMenuItemOptionId())
                .orElseThrow(() -> new RuntimeException("MenuItemOption not found")));
        optionChoice.setName(request.getName());
        optionChoice.setDescription(request.getDescription());
        optionChoice.setAdditionalPrice(request.getAdditionalPrice());
        optionChoice.setAvailable(request.isAvailable());
        optionChoice.setDisplayOrder(request.getDisplayOrder());

        OptionChoice updatedOptionChoice = optionChoiceRepository.save(optionChoice);
        return OptionChoiceResponse.fromEntity(updatedOptionChoice);
    }

    public OptionChoiceResponse getOptionChoiceById(Long id) {
        OptionChoice optionChoice = optionChoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OptionChoice not found"));
        return OptionChoiceResponse.fromEntity(optionChoice);
    }

    public List<OptionChoiceResponse> getAllOptionChoices() {
        return optionChoiceRepository.findAll().stream()
                .map(OptionChoiceResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteOptionChoice(Long id) {
        optionChoiceRepository.deleteById(id);
    }
}
