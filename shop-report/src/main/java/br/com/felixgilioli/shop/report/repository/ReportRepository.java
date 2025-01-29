package br.com.felixgilioli.shop.report.repository;

import br.com.felixgilioli.shop.report.model.ShopReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<ShopReport, Long> {

    @Modifying
    @Query(value = "update shop_report set amount = amount + 1 where identifier = :shopStatus",
            nativeQuery = true)
    void incrementShopStatus(String shopStatus);
}
