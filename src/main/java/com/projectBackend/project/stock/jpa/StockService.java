package com.projectBackend.project.stock.jpa;

import com.projectBackend.project.stock.StockDto;
import com.projectBackend.project.stock.jdbc.StockJdbcBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.projectBackend.project.utils.Common.date;

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
                if (existsData()) {
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
    }

    private boolean existsData() {
        // 해당 년월에 해당하는 데이터가 존재하는지 여부를 체크하는 쿼리
        return recentStockRepository.count() > 0;
    }


    // 조영준 : 주식 리스트 출력
//    public List<StockDto> getStockList(String type) {
//        log.info("today_date : {}", date);
//        log.info("type : {}", type);
//        List<StockDto> stockDtoList = new ArrayList<>();
//        List<RecentStockEntity> stockEntities;
//        switch (type) {
//            case "고가":
//                stockEntities = recentStockRepository.findTop200ByOrderByStockHighDesc(date);
//                break;
//            case "EPS":
//                stockEntities = recentStockRepository.findTop200ByOrderByStockEpsDesc(date);
//                break;
//            case "PER":
//                stockEntities = recentStockRepository.findTop200ByOrderByStockPerAsc(date);
//                break;
//            case "DIV":
//                stockEntities = recentStockRepository.findTop200ByOrderByStockDivDesc(date);
//                break;
//            default:
//                log.info("잘못된 접근입니다.");
//                return stockDtoList;
//        }
//
//        for (RecentStockEntity recentStockEntity : stockEntities) {
//            StockDto stockDto = createStockDtoFromEntity(recentStockEntity);
//            stockDtoList.add(stockDto);
//        }
//
//        return stockDtoList;
//    }

    // Entity to Dto
    private StockDto createStockDtoFromEntity(RecentStockEntity recentStockEntity) {
        StockDto stockDto = new StockDto();
        stockDto.setOpen(recentStockEntity.getStockOpen());
        stockDto.setHigh(recentStockEntity.getStockHigh());
        stockDto.setLow(recentStockEntity.getStockLow());
        stockDto.setClose(recentStockEntity.getStockClose());
        stockDto.setVolume(recentStockEntity.getStockVolume());
        stockDto.setTradingValue(recentStockEntity.getStockTradingValue());
        stockDto.setFluctuationRate(recentStockEntity.getStockFluctuationRate());
        stockDto.setDate(recentStockEntity.getStockDate());
        stockDto.setStockCode(recentStockEntity.getStockCode());
        stockDto.setStockName(recentStockEntity.getStockName());
        stockDto.setBps(recentStockEntity.getStockBps());
        stockDto.setPer(recentStockEntity.getStockPer());
        stockDto.setPbr(recentStockEntity.getStockPbr());
        stockDto.setEps(recentStockEntity.getStockEps());
        stockDto.setDiv(recentStockEntity.getStockDiv());
        stockDto.setDps(recentStockEntity.getStockDps());

        return stockDto;
    }
}