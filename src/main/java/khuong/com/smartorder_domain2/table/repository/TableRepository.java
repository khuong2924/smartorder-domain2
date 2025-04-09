package khuong.com.smartorder_domain2.table.repository;

import khuong.com.smartorder_domain2.table.entity.Table;
import khuong.com.smartorder_domain2.table.enums.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {
    List<Table> findByStatus(TableStatus status);

    List<Table> findByStatusAndActiveTrue(TableStatus status);
    boolean existsByTableNumber(String tableNumber);
    void deleteByIdAndActiveTrue(Long id);
    List<Table> findByActiveFalse();

    Optional<Table> findByTableNumber(String tableNumber);
  
}