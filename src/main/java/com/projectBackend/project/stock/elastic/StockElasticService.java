package com.projectBackend.project.stock.elastic;

import com.projectBackend.project.stock.StockDto;
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
        for (Map.Entry<String, List<StockDto>> entry : stockDataMap.entrySet()) {
            String yearMonth = entry.getKey();
            log.info("save yearMonth {}", yearMonth);
            List<StockDto> stockDtoList = entry.getValue();

            // Elasticsearch에서 해당 날짜의 데이터 삭제
            deleteDataByYearMonth(yearMonth);

            // Bulk Insert
            List<StockElasticEntity> elasticEntities = convertToElasticEntities(stockDtoList);
            bulkInsert(elasticEntities);
        }
    }

    private void deleteDataByYearMonth(String yearMonth) {
        // StockElasticRepository를 통한 삭제
//        stockElasticRepository.deleteByStockDateStartingWith(yearMonth);
    }

    private void bulkInsert(List<StockElasticEntity> elasticEntities) {
        List<IndexQuery> indexQueries = new ArrayList<>();
        for (StockElasticEntity elasticEntity : elasticEntities) {
            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(elasticEntity.getId())
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
            elasticEntity.setId(stockDto.getStockCode() + "_" + stockDto.getDate());
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

            elasticEntities.add(elasticEntity);
        }

        return elasticEntities;
    }
}