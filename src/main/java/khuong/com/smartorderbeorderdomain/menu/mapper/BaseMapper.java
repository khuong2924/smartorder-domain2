package khuong.com.smartorderbeorderdomain.menu.mapper;

import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface BaseMapper {

    default LocalDateTime toLocalDateTime(String dateTimeStr) {
        if (dateTimeStr == null) return null;
        return LocalDateTime.parse(dateTimeStr);
    }

    default String fromLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    default String formatPrice(BigDecimal price) {
        if (price == null) return null;
        return String.format("%,.2f VND", price);
    }

    default BigDecimal parsePriceString(String priceStr) {
        if (priceStr == null) return null;
        try {
            return new BigDecimal(priceStr.replaceAll("[^\\d.]", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}