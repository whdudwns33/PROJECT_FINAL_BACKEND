package com.projectBackend.project.stock.jpa;

import com.projectBackend.project.stock.StockDto;
import com.projectBackend.project.stock.jdbc.StockJdbcBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
//@Transactional
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final RecentStockRepository recentStockRepository;
    private final StockJdbcBatchService stockJdbcBatchService;

    public List<StockEntity> getStockByName(String stockName) {
        return stockRepository.findByStockNameContainingIgnoreCase(stockName);
    }
    @Transactional
    public void batchInsertOrUpdate(Map<String, List<StockDto>> stockDataMap) {
        // Get the current year and month
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        String currentYearMonth = currentDate.format(formatter);

        for (Map.Entry<String, List<StockDto>> entry : stockDataMap.entrySet()) {
            String yearMonth = entry.getKey();
            List<StockDto> stockDtoList = entry.getValue();

            // 들어온 데이터의 key가 현재 년월인지?
            if (currentYearMonth.equals(yearMonth)) {
                // DB안에 현재 년월에 해당하는 데이터가 존재하는지?
                if (existsDataForYearMonth(yearMonth)) {
                    // 존재하면 recent_stock에 update
                    stockJdbcBatchService.batchUpdate(stockDtoList, RecentStockEntity.class);
                } else {
                    // 존재하지 않으면 recent_stock에 insert
                    stockJdbcBatchService.batchInsert(stockDtoList, RecentStockEntity.class);
                }
            } else {
                // 과거 년월 데이터이면 stock에 insert
                stockJdbcBatchService.batchInsert(stockDtoList, StockEntity.class);
            }
        }


//        for (String yearMonth : stockDataMap.keySet()) {
//            // 해당 년월에 해당하는 데이터가 있는지 체크
//            boolean dataExists = existsDataForYearMonth(yearMonth);
//
//            if (dataExists) {
////                // 데이터가 이미 존재하면 Batch Update 수행
//                List<StockDto> stockDtoList = stockDataMap.get(yearMonth);
//                stockJdbcBatchService.batchUpdate(stockDtoList);
//            } else {
////                // 데이터가 없으면 Batch Insert 수행
//                List<StockDto> stockDtoList = stockDataMap.get(yearMonth);
//                stockJdbcBatchService.batchInsert(stockDtoList);
//            }
//        }
    }

    private boolean existsDataForYearMonth(String yearMonth) {
        // 해당 년월에 해당하는 데이터가 존재하는지 여부를 체크하는 쿼리
        return recentStockRepository.existsByStockDateStartingWith(yearMonth);
    }
}
