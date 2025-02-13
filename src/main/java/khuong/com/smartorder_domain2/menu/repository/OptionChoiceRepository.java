package khuong.com.smartorder_domain2.menu.repository;

import khuong.com.smartorder_domain2.menu.entity.OptionChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionChoiceRepository extends JpaRepository<OptionChoice, Long> {

    // Lấy danh sách choice theo option
    List<OptionChoice> findByOptionId(Long optionId);

    // Lấy danh sách choice đang available
    List<OptionChoice> findByOptionIdAndAvailableTrue(Long optionId);

    // Cập nhật trạng thái available
    @Modifying
    @Query("UPDATE OptionChoice c SET c.available = :available WHERE c.id = :id")
    void updateAvailability(@Param("id") Long id, @Param("available") boolean available);

    // Xóa tất cả choice của option
    @Modifying
    void deleteByOptionId(Long optionId);
}