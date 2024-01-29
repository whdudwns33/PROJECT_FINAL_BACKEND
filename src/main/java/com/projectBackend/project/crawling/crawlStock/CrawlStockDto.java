package com.projectBackend.project.crawling.crawlStock;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CrawlStockDto {
    private String name;
    private String price;
    private String upDown;
    private String rate;

    public static CrawlStockDto of(CrawlStockEntity crawlStockEntity) {
        return CrawlStockDto.builder()
                .name(crawlStockEntity.getStockName())
                .price(crawlStockEntity.getStockPrice())
                .upDown(crawlStockEntity.getStockUpDown())
                .rate(crawlStockEntity.getStockRate())
                .build();
    }

    public CrawlStockEntity toEntity(CrawlStockDto dto) {
        return CrawlStockEntity.builder()
                .stockName(dto.getName())
                .stockPrice(dto.getPrice())
                .stockUpDown(dto.getUpDown())
                .stockRate(dto.getRate())
                .build();
    }
}
