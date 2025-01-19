package khuong.com.smartorderbeorderdomain.menu.service;

import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;
import khuong.com.smartorderbeorderdomain.menu.entity.PriceHistory;
import khuong.com.smartorderbeorderdomain.menu.repository.PriceHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PriceHistoryService {
    private final PriceHistoryRepository priceHistoryRepository;

    @Transactional
    public void saveInitialPrice(MenuItem menuItem) {
        savePriceHistory(menuItem, null, menuItem.getPrice(), "Initial price");
    }

    @Transactional
    public void updatePrice(MenuItem menuItem, BigDecimal newPrice, String reason) {
        if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        BigDecimal oldPrice = menuItem.getPrice();
        menuItem.setPrice(newPrice);

        savePriceHistory(menuItem, oldPrice, newPrice, reason);
    }

    public List<PriceHistory> getPriceHistory(Long menuItemId) {
        return priceHistoryRepository.findByMenuItemIdOrderByChangedAtDesc(menuItemId);
    }

    private void savePriceHistory(MenuItem menuItem, BigDecimal oldPrice,
                                  BigDecimal newPrice, String reason) {
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
}