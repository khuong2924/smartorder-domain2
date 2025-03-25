package khuong.com.smartorder_domain2.order.dto.response;

import khuong.com.smartorder_domain2.order.entity.Order;
import khuong.com.smartorder_domain2.table.dto.response.TableResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private TableResponse table;
    private String waiterId;
    private String status;
    private String note;
    private BigDecimal totalAmount;
    private List<OrderItemResponse> items;

    public static OrderResponse fromEntity(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .table(TableResponse.fromEntity(order.getTable()))
                .waiterId(order.getWaiterId())
                .status(order.getStatus().name())
                .note(order.getNote())
                .totalAmount(order.getTotalAmount())
                .items(order.getItems().stream()
                        .map(OrderItemResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}