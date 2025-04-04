package khuong.com.smartorder_domain2.table.service;

import khuong.com.smartorder_domain2.menu.dto.exception.DuplicateResourceException;
import khuong.com.smartorder_domain2.menu.dto.exception.ResourceNotFoundException;
import khuong.com.smartorder_domain2.table.dto.request.CreateTableRequest;
import khuong.com.smartorder_domain2.table.dto.request.UpdateTableRequest;
import khuong.com.smartorder_domain2.table.dto.response.TableResponse;
import khuong.com.smartorder_domain2.table.entity.Table;
import khuong.com.smartorder_domain2.table.enums.TableStatus;
import khuong.com.smartorder_domain2.table.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TableService {
    private final TableRepository tableRepository;
    
    @Transactional
    public TableResponse createTable(CreateTableRequest request) {
        if (tableRepository.existsByTableNumber(request.getTableNumber())) {
            throw new DuplicateResourceException("Bàn với mã " + request.getTableNumber() + " đã tồn tại");
        }
        
        Table table = new Table();
        table.setTableNumber(request.getTableNumber());
        table.setNotes(request.getNotes());
        table.setCapacity(request.getCapacity());
        table.setStatus(TableStatus.AVAILABLE);
        table.setActive(true);
        
        table = tableRepository.save(table);
        return TableResponse.fromEntity(table);
    }
    
    public TableResponse getTableById(Long id) {
        Table table = findTableById(id);
        return TableResponse.fromEntity(table);
    }
    
    public List<TableResponse> getAllTables() {
        return tableRepository.findAll().stream()
                .map(TableResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<TableResponse> getAvailableTables() {
        return tableRepository.findByStatusAndActiveTrue(TableStatus.AVAILABLE).stream()
                .map(TableResponse::fromEntity)
                .collect(Collectors.toList());
    }
    

    
    @Transactional
    public void deleteTable(Long id) {
        Table table = findTableById(id);
        table.setActive(false);
        tableRepository.save(table);
    }
    
    @Transactional
    public void hardDeleteTable(Long id) {
        Table table = findTableById(id);
        tableRepository.delete(table);
    }
    
    
    
    @Transactional
    public void bulkUpdateStatus(List<Long> ids, TableStatus status) {
        ids.forEach(id -> {
            Table table = findTableById(id);
            table.setStatus(status);
            tableRepository.save(table);
        });
    }
    
    private Table findTableById(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bàn với id: " + id));
    }
}