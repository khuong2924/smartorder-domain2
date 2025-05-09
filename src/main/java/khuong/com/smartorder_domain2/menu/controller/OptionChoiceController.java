package khuong.com.smartorder_domain2.menu.controller;

import jakarta.validation.Valid;
import khuong.com.smartorder_domain2.menu.dto.request.OptionChoiceRequest;
import khuong.com.smartorder_domain2.menu.dto.response.OptionChoiceResponse;
import khuong.com.smartorder_domain2.menu.service.OptionChoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/optionChoices")
@RequiredArgsConstructor
@Validated
public class OptionChoiceController {
    private final OptionChoiceService optionChoiceService;

    @PostMapping
    public ResponseEntity<OptionChoiceResponse> createOptionChoice(@Valid @RequestBody OptionChoiceRequest request) {
        OptionChoiceResponse response = optionChoiceService.createOptionChoice(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OptionChoiceResponse> updateOptionChoice(
            @PathVariable Long id,
            @Valid @RequestBody OptionChoiceRequest request) {
        OptionChoiceResponse response = optionChoiceService.updateOptionChoice(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OptionChoiceResponse> getOptionChoiceById(@PathVariable Long id) {
        OptionChoiceResponse response = optionChoiceService.getOptionChoiceById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OptionChoiceResponse>> getAllOptionChoices() {
        List<OptionChoiceResponse> responses = optionChoiceService.getAllOptionChoices();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOptionChoice(@PathVariable Long id) {
        optionChoiceService.deleteOptionChoice(id);
    }
}