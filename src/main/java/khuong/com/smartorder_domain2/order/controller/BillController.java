package khuong.com.smartorder_domain2.order.controller;

import khuong.com.smartorder_domain2.order.dto.request.CreateTableBillRequest;
import khuong.com.smartorder_domain2.order.dto.response.TableBillResponse;
import khuong.com.smartorder_domain2.order.service.BillService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/bills")
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;
    
    @PostMapping
    public ResponseEntity<TableBillResponse> createTableBill(@RequestBody CreateTableBillRequest request) {
        TableBillResponse response = billService.createTableBill(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{billId}")
    public ResponseEntity<TableBillResponse> getBill(@PathVariable String billId) {
        TableBillResponse response = billService.getBillById(billId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TableBillResponse>> getAllBills() {
        List<TableBillResponse> response = billService.getAllBills();
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{billId}/payment-status")
    public ResponseEntity<TableBillResponse> updatePaymentStatus(
            @PathVariable String billId,
            @RequestParam String status) {
        TableBillResponse response = billService.updateBillPaymentStatus(billId, status);
        return ResponseEntity.ok(response);
    }
}