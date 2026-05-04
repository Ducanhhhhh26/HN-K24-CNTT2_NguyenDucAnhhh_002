package springboot.dgnl.repository;

import springboot.dgnl.model.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    @Query("SELECT d FROM Device d WHERE " +
           "(:keyword IS NULL OR LOWER(d.deviceName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:brandId IS NULL OR d.brand.id = :brandId)")
    Page<Device> searchAndFilter(@Param("keyword") String keyword, @Param("brandId") Long brandId, Pageable pageable);

    @Modifying
    @Query("UPDATE Device d SET d.brand = null WHERE d.brand.id = :brandId")
    void unlinkDevicesFromBrand(@Param("brandId") Long brandId);
}
