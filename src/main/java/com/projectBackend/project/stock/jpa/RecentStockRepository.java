package com.projectBackend.project.stock.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface RecentStockRepository extends JpaRepository<RecentStockEntity, Long> {

    List<StockEntity> findByStockNameContainingIgnoreCase(String stockName);
    boolean existsByStockDateStartingWith(String yearMonth);

    // 고가
    @Query("SELECT e FROM RecentStockEntity e WHERE e.stockDate = (SELECT MAX(e2.stockDate) FROM RecentStockEntity e2 " +
            "WHERE e2.stockDate = :date) ORDER BY e.stockHigh DESC")
    List<RecentStockEntity> findTop200ByOrderByStockHighDesc(@Param("date") String date);

    // eps
    @Query("SELECT e FROM RecentStockEntity e WHERE e.stockDate = (SELECT MAX(e2.stockDate) FROM RecentStockEntity e2 " +
            "WHERE e2.stockDate = :date) ORDER BY e.stockEps DESC")
    List<RecentStockEntity> findTop200ByOrderByStockEpsDesc(@Param("date") String date);

    // per
    @Query("SELECT e FROM RecentStockEntity e WHERE e.stockDate = (SELECT MAX(e2.stockDate) FROM RecentStockEntity e2 " +
            "WHERE e2.stockDate = :date) ORDER BY e.stockPer ASC")
    List<RecentStockEntity> findTop200ByOrderByStockPerAsc(@Param("date") String date);

    // div
    @Query("SELECT e FROM RecentStockEntity e WHERE e.stockDate = (SELECT MAX(e2.stockDate) FROM RecentStockEntity e2 " +
            "WHERE e2.stockDate = :date) ORDER BY e.stockDiv DESC")
    List<RecentStockEntity> findTop200ByOrderByStockDivDesc(@Param("date") String date);





}