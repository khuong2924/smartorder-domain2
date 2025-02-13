package khuong.com.smartorder_domain2.menu.service;

import khuong.com.smartorder_domain2.menu.dto.exception.InvalidPriceException;
import khuong.com.smartorder_domain2.menu.dto.response.PriceHistoryResponse;
import khuong.com.smartorder_domain2.menu.entity.MenuItem;
import khuong.com.smartorder_domain2.menu.entity.PriceHistory;
import khuong.com.smartorder_domain2.menu.repository.PriceHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PriceHistoryService {
    private final PriceHistoryRepository priceHistoryRepository;

    @Transactional
    public void saveInitialPrice(MenuItem menuItem) {
        PriceHistory history = PriceHistory.builder()
                .menuItem(menuItem)
                .oldPrice(null)
                .newPrice(menuItem.getPrice())
                .reason("Initial price")
                .changedBy("SYSTEM")
                .changedAt(LocalDateTime.now())
                .build();

        priceHistoryRepository.save(history);
    }

    @Transactional
    public void updatePrice(MenuItem menuItem, BigDecimal newPrice, String reason) {
        validatePrice(newPrice);

        BigDecimal oldPrice = menuItem.getPrice();
        menuItem.setPrice(newPrice);

        PriceHistory history = PriceHistory.builder()
                .menuItem(menuItem)
                .oldPrice(oldPrice)
                .newPrice(newPrice)
                .reason(reason)
                .changedBy("SYSTEM")
                .changedAt(LocalDateTime.now())
                .build();

        priceHistoryRepository.save(history);
    }

    public List<PriceHistoryResponse> getPriceHistory(Long menuItemId) {
        return priceHistoryRepository.findByMenuItemIdOrderByChangedAtDesc(menuItemId)
                .stream()
                .map(PriceHistoryResponse::fromEntity)
                .collect(Collectors.toList());
    }



    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPriceException("Price must be greater than zero");
        }
    }
}