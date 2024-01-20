package com.projectBackend.project.stock.elastic;

import com.projectBackend.project.stock.StockDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockElasticService {
    @Autowired
    private StockElasticRepository stockElasticRepository;

    public void saveStocksToElasticsearch(List<StockDto> stockDtoList) {
        List<StockElasticEntity> elasticEntities = convertToElasticEntities(stockDtoList);
        stockElasticRepository.saveAll(elasticEntities);
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