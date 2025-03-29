package khuong.com.smartorder_domain2.table.controller;

import jakarta.validation.Valid;
import khuong.com.smartorder_domain2.table.dto.request.CreateTableRequest;
import khuong.com.smartorder_domain2.table.dto.request.UpdateTableRequest;
import khuong.com.smartorder_domain2.table.dto.response.TableResponse;
import khuong.com.smartorder_domain2.table.enums.TableStatus;
import khuong.com.smartorder_domain2.table.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
public class TableController {
    private final TableService tableService;
    
    @PostMapping
    public ResponseEntity<TableResponse> createTable(@Valid @RequestBody CreateTableRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tableService.createTable(request));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TableResponse> getTableById(@PathVariable Long id) {
        return ResponseEntity.ok(tableService.getTableById(id));
    }
    
    @GetMapping
    public ResponseEntity<List<TableResponse>> getAllTables() {
        return ResponseEntity.ok(tableService.getAllTables());
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<TableResponse>> getAvailableTables() {
        return ResponseEntity.ok(tableService.getAvailableTables());
    }
    

    

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }



    @PatchMapping("/bulk-status")
    public ResponseEntity<Void> bulkUpdateStatus(@RequestBody List<Long> ids, @RequestParam TableStatus status) {
        tableService.bulkUpdateStatus(ids, status);
        return ResponseEntity.noContent().build();
    }
}