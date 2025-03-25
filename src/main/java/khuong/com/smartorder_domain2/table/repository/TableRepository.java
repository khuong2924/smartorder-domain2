package khuong.com.smartorder_domain2.table.repository;

import khuong.com.smartorder_domain2.table.entity.Table;
import khuong.com.smartorder_domain2.table.enums.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {
    List<Table> findByStatus(TableStatus status);
    Optional<Table> findByTableNumber(String tableNumber);
    List<Table> findByStatusAndActiveTrue(TableStatus status);
    boolean existsByTableNumber(String tableNumber);
}