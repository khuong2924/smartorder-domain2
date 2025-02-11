package khuong.com.smartorderbeorderdomain.menu.service;


import khuong.com.smartorderbeorderdomain.menu.dto.request.OptionChoiceRequest;
import khuong.com.smartorderbeorderdomain.menu.dto.response.OptionChoiceResponse;
import khuong.com.smartorderbeorderdomain.menu.entity.OptionChoice;
import khuong.com.smartorderbeorderdomain.menu.repository.OptionChoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OptionChoiceService {
    private final OptionChoiceRepository optionChoiceRepository;

    public OptionChoiceResponse createOptionChoice(OptionChoiceRequest request) {
        OptionChoice optionChoice = new OptionChoice();
        optionChoice.setMenuItemOptionId(request.getMenuItemOptionId());
        optionChoice.setName(request.getName());
        optionChoice.setDescription(request.getDescription());
        optionChoice.setAdditionalPrice(request.getAdditionalPrice());
        optionChoice.setDefaultChoice(request.isDefaultChoice());
        optionChoice.setAvailable(request.isAvailable());
        optionChoice.setDisplayOrder(request.getDisplayOrder());

        OptionChoice savedOptionChoice = optionChoiceRepository.save(optionChoice);
        return OptionChoiceResponse.fromEntity(savedOptionChoice);
    }

    public OptionChoiceResponse updateOptionChoice(Long id, OptionChoiceRequest request) {
        OptionChoice optionChoice = optionChoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OptionChoice not found"));

        optionChoice.setMenuItemOptionId(request.getMenuItemOptionId());
        optionChoice.setName(request.getName());
        optionChoice.setDescription(request.getDescription());
        optionChoice.setAdditionalPrice(request.getAdditionalPrice());
        optionChoice.setDefaultChoice(request.isDefaultChoice());
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
