package com.projectBackend.project.stock.jpa;

import com.projectBackend.project.stock.StockDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;

import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RecentStockRepository extends JpaRepository<RecentStockEntity, Long> {

    List<StockEntity> findByStockNameContainingIgnoreCase(String stockName);
    // 전체 레코드 수를 세는 메서드
    long count();

    // 고가
    @Query("SELECT e FROM RecentStockEntity e WHERE e.stockDate = (SELECT MAX(e2.stockDate) FROM RecentStockEntity e2 " +
            "WHERE e2.stockDate = :date) ORDER BY e.stockHigh DESC")
    List<RecentStockEntity> findTop200ByOrderByStockHighDesc(@Param("date") @Temporal(TemporalType.DATE) Date date, Pageable pageable);

    // eps
    @Query("SELECT e FROM RecentStockEntity e WHERE e.stockDate = (SELECT MAX(e2.stockDate) FROM RecentStockEntity e2 " +
            "WHERE e2.stockDate = :date) ORDER BY e.stockEps DESC")
    List<RecentStockEntity> findTop200ByOrderByStockEpsDesc(@Param("date") @Temporal(TemporalType.DATE) Date date, Pageable pageable);

    // per
    @Query("SELECT e FROM RecentStockEntity e WHERE e.stockDate = (SELECT MAX(e2.stockDate) FROM RecentStockEntity e2 " +
            "WHERE e2.stockDate = :date) AND e.stockPer IS NOT NULL AND e.stockPer <> 0 ORDER BY e.stockPer ASC")
    List<RecentStockEntity> findTop200ByOrderByStockPerAsc(@Param("date") @Temporal(TemporalType.DATE) Date date, Pageable pageable);

    // div
    @Query("SELECT e FROM RecentStockEntity e WHERE e.stockDate = (SELECT MAX(e2.stockDate) FROM RecentStockEntity e2 " +
            "WHERE e2.stockDate = :date) ORDER BY e.stockDiv DESC")
    List<RecentStockEntity> findTop200ByOrderByStockDivDesc(@Param("date") @Temporal(TemporalType.DATE) Date date, Pageable pageable);

    // 최신 주식 데이터
    @Query("SELECT e FROM RecentStockEntity e WHERE e.stockDate = :currentDate " +
            "AND e.stockName = :stockName")
    RecentStockEntity findLatestByName(@Param("stockName") String stockName,@Param("currentDate") Date currentDate);

    Optional<RecentStockEntity> findFirstStockByStockNameOrderByStockDateDesc(String stockName);
}

