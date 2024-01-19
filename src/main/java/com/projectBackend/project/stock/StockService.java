package com.projectBackend.project.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    public List<StockEntity> convertToEntities(List<StockDto> stockDtoList) {
        return stockDtoList.stream()
                .map(stockDto -> StockEntity.builder()
                        .stockOpen(stockDto.getOpen())
                        .stockHigh(stockDto.getHigh())
                        .stockLow(stockDto.getLow())
                        .stockClose(stockDto.getClose())
                        .stockVolume(stockDto.getVolume())
                        .stockTradingValue(stockDto.getTradingValue())
                        .stockFluctuationRate(stockDto.getFluctuationRate())
                        .stockDate(stockDto.getDate())
                        .stockCode(stockDto.getStockCode())
                        .stockName(stockDto.getStockName())
                        .stockBps(stockDto.getBps())
                        .stockPer(stockDto.getPer())
                        .stockPbr(stockDto.getPbr())
                        .stockEps(stockDto.getEps())
                        .stockDiv(stockDto.getDiv())
                        .stockDps(stockDto.getDps())
                        .build())
                .collect(Collectors.toList());
    }

    public void saveStocks(List<StockEntity> stockEntityList) {
        stockRepository.saveAll(stockEntityList);
    }
}
