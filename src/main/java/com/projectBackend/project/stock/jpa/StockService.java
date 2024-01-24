package com.projectBackend.project.stock.jpa;

import com.projectBackend.project.stock.StockDto;
import com.projectBackend.project.stock.jdbc.StockJdbcBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
//@Transactional
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final StockJdbcBatchService stockJdbcBatchService;

    public List<StockEntity> getStockByName(String stockName) {
        return stockRepository.findByStockNameContainingIgnoreCase(stockName);
    }
    @Transactional
    public void batchInsertOrUpdate(Map<String, List<StockDto>> stockDataMap) {
        for (String yearMonth : stockDataMap.keySet()) {
            // 해당 년월에 해당하는 데이터가 있는지 체크
            boolean dataExists = existsDataForYearMonth(yearMonth);

            if (dataExists) {
//                // 데이터가 이미 존재하면 Batch Update 수행
                List<StockDto> stockDtoList = stockDataMap.get(yearMonth);
                stockJdbcBatchService.batchUpdate(stockDtoList);
            } else {
//                // 데이터가 없으면 Batch Insert 수행
                List<StockDto> stockDtoList = stockDataMap.get(yearMonth);
                stockJdbcBatchService.batchInsert(stockDtoList);
            }
        }
    }

    private boolean existsDataForYearMonth(String yearMonth) {
        // 해당 년월에 해당하는 데이터가 존재하는지 여부를 체크하는 쿼리
        return stockRepository.existsByStockDateContaining(yearMonth);
    }
}
