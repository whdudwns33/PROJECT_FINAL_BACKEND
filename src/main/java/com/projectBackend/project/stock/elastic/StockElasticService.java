package com.projectBackend.project.stock.elastic;

import com.projectBackend.project.stock.StockDto;
import com.projectBackend.project.stock.jpa.RecentStockEntity;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StockElasticService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private StockElasticRepository stockElasticRepository;

    @Transactional
    public void saveStocksToElasticsearch(Map<String, List<StockDto>> stockDataMap) {
        // Get the current year and month
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        String currentYearMonth = currentDate.format(formatter);

        for (Map.Entry<String, List<StockDto>> entry : stockDataMap.entrySet()) {
            String yearMonth = entry.getKey();

            // 들어온 데이터의 key가 현재 년월인지?
            if (currentYearMonth.equals(yearMonth)) {
                log.info("save yearMonth {}", yearMonth);
                List<StockDto> stockDtoList = entry.getValue();

                // Elasticsearch에서 데이터 삭제
                deleteData();

                // Bulk Insert
                List<StockElasticEntity> elasticEntities = convertToElasticEntities(stockDtoList);
                bulkInsert(elasticEntities);
            }
        }
    }

    private void deleteData() {
        // 해당 년월에 해당하는 데이터가 존재하는지 여부를 체크하는 쿼리
        if (stockElasticRepository.count() > 0) {
            // index truncate
            stockElasticRepository.deleteAll();
        }

    }

    private void bulkInsert(List<StockElasticEntity> elasticEntities) {
        List<IndexQuery> indexQueries = new ArrayList<>();
        for (StockElasticEntity elasticEntity : elasticEntities) {
            IndexQuery indexQuery = new IndexQueryBuilder()
//                    .withId(elasticEntity.getId())
                    .withObject(elasticEntity)
                    .build();
            indexQueries.add(indexQuery);
        }

        if (!indexQueries.isEmpty()) {
            elasticsearchRestTemplate.bulkIndex(indexQueries, elasticsearchRestTemplate.getIndexCoordinatesFor(StockElasticEntity.class));

        }
    }

    private List<StockElasticEntity> convertToElasticEntities(List<StockDto> stockDtoList) {
        List<StockElasticEntity> elasticEntities = new ArrayList<>();

        for (StockDto stockDto : stockDtoList) {
            StockElasticEntity elasticEntity = new StockElasticEntity();

            elasticEntity.setStockOpen(stockDto.getOpen());
            elasticEntity.setStockHigh(stockDto.getHigh());
            elasticEntity.setStockLow(stockDto.getLow());
            elasticEntity.setStockClose(stockDto.getClose());
            elasticEntity.setStockVolume(stockDto.getVolume());
            elasticEntity.setStockTradingValue(stockDto.getTradingValue());
            elasticEntity.setStockFluctuationRate(stockDto.getFluctuationRate());
            elasticEntity.setStockDate(stockDto.getDate());
            elasticEntity.setStockCode(stockDto.getStockCode());
            elasticEntity.setStockName(stockDto.getStockName());
            elasticEntity.setStockBps(stockDto.getBps());
            elasticEntity.setStockPer(stockDto.getPer());
            elasticEntity.setStockPbr(stockDto.getPbr());
            elasticEntity.setStockEps(stockDto.getEps());
            elasticEntity.setStockDiv(stockDto.getDiv());
            elasticEntity.setStockDps(stockDto.getDps());

            elasticEntity.generateId(); // ID 생성 메서드 호출

            elasticEntities.add(elasticEntity);
        }

        return elasticEntities;
    }
}