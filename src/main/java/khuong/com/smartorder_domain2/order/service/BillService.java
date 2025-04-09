package khuong.com.smartorder_domain2.order.service;

import khuong.com.smartorder_domain2.order.dto.request.CreateTableBillRequest;
import khuong.com.smartorder_domain2.order.dto.response.OrderResponse;
import khuong.com.smartorder_domain2.order.dto.response.TableBillResponse;
import khuong.com.smartorder_domain2.order.entity.Bill;
import khuong.com.smartorder_domain2.order.entity.Order;
import khuong.com.smartorder_domain2.order.enums.OrderStatus;
import khuong.com.smartorder_domain2.order.enums.PaymentStatus;
import khuong.com.smartorder_domain2.order.repository.BillRepository;
import khuong.com.smartorder_domain2.order.repository.OrderRepository;
import khuong.com.smartorder_domain2.table.entity.Table;
import khuong.com.smartorder_domain2.table.enums.TableStatus;
import khuong.com.smartorder_domain2.table.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BillService {
    private final BillRepository billRepository;
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    
    @Transactional
    public TableBillResponse createTableBill(CreateTableBillRequest request) {
        log.info("Đang tạo hóa đơn cho bàn: {}", request.getTableNumber());
        
        // Tìm bàn
        Table table = tableRepository.findByTableNumber(request.getTableNumber())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn với số: " + request.getTableNumber()));
        
        // Tìm tất cả đơn hàng đang hoạt động của bàn chưa được đưa vào hóa đơn nào
        List<Order> tableOrders = orderRepository.findByTableIdAndBillIsNullAndStatusNot(
                table.getId(), OrderStatus.CANCELLED);
        
        if (tableOrders.isEmpty()) {
            throw new RuntimeException("Không tìm thấy đơn hàng đang hoạt động cho bàn: " + request.getTableNumber());
        }
        
        // Tính tổng số tiền
        BigDecimal totalAmount = tableOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Tạo hóa đơn mới
        Bill bill = Bill.builder()
                .table(table)
                .totalAmount(totalAmount)
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .note(request.getNote())
                .waiterId(request.getWaiterId())
                .build();
        
        // Lưu hóa đơn
        bill = billRepository.save(bill);
        
        // Cập nhật các đơn hàng để tham chiếu đến hóa đơn này
        for (Order order : tableOrders) {
            order.setBill(bill);
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);
        }
        
        // Cập nhật trạng thái bàn
        table.setStatus(TableStatus.AVAILABLE);
        tableRepository.save(table);
        
        // Tạo phản hồi
        List<OrderResponse> orderResponses = tableOrders.stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
        
        return TableBillResponse.builder()
                .billId(bill.getId())
                .tableNumber(table.getTableNumber())
                .includedOrders(orderResponses)
                .totalAmount(totalAmount)
                .paymentMethod(bill.getPaymentMethod())
                .paymentStatus(bill.getPaymentStatus().toString())
                .createdAt(bill.getCreatedAt())
                .note(bill.getNote())
                .build();
    }
    
    public TableBillResponse getBillById(String billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với id: " + billId));
        
        List<OrderResponse> orderResponses = bill.getOrders().stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
        
        return TableBillResponse.builder()
                .billId(bill.getId())
                .tableNumber(bill.getTable().getTableNumber())
                .includedOrders(orderResponses)
                .totalAmount(bill.getTotalAmount())
                .paymentMethod(bill.getPaymentMethod())
                .paymentStatus(bill.getPaymentStatus().toString())
                .createdAt(bill.getCreatedAt())
                .note(bill.getNote())
                .build();
    }
    
    @Transactional
    public TableBillResponse updateBillPaymentStatus(String billId, String paymentStatus) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với id: " + billId));
        
        bill.setPaymentStatus(PaymentStatus.valueOf(paymentStatus));
        bill = billRepository.save(bill);
        
        return getBillById(billId);
    }
    
    public List<TableBillResponse> getAllBills() {
        log.info("Lấy danh sách tất cả hóa đơn");
        List<Bill> bills = billRepository.findAll();
        
        return bills.stream()
                .map(bill -> {
                    List<OrderResponse> orderResponses = bill.getOrders().stream()
                            .map(OrderResponse::fromEntity)
                            .collect(Collectors.toList());
                    
                    return TableBillResponse.builder()
                            .billId(bill.getId())
                            .tableNumber(bill.getTable().getTableNumber())
                            .includedOrders(orderResponses)
                            .totalAmount(bill.getTotalAmount())
                            .paymentMethod(bill.getPaymentMethod())
                            .paymentStatus(bill.getPaymentStatus().toString())
                            .createdAt(bill.getCreatedAt())
                            .note(bill.getNote())
                            .build();
                })
                .collect(Collectors.toList());
    }

}