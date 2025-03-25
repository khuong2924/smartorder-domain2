package khuong.com.smartorder_domain2.table.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import khuong.com.smartorder_domain2.table.enums.TableStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTableRequest {
    @Size(max = 20, message = "Mã bàn không được vượt quá 20 ký tự")
    private String tableNumber;
    
    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private String notes;
    
    @Min(value = 1, message = "Sức chứa phải lớn hơn 0")
    private Integer capacity;
    
    private TableStatus status;
    
    private Boolean active;
}