package khuong.com.smartorder_domain2.configs;

import khuong.com.smartorder_domain2.table.entity.Table;
import khuong.com.smartorder_domain2.table.enums.TableStatus;
import khuong.com.smartorder_domain2.table.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TableDataInitializer {
    private final TableRepository tableRepository;
    
    @Bean
    public CommandLineRunner initTables() {
        return args -> {
            // Only initialize if no tables exist
            if (tableRepository.count() == 0) {
                for (int i = 1; i <= 10; i++) {
                    Table table = new Table();
                    table.setTableNumber("T" + i);
                    table.setNotes("Table " + i);
                    table.setCapacity(i % 3 == 0 ? 6 : (i % 2 == 0 ? 4 : 2));
                    table.setStatus(TableStatus.AVAILABLE);
                    table.setActive(true);
                    tableRepository.save(table);
                }
            }
        };
    }
}