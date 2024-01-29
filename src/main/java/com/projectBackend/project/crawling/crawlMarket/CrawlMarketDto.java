package com.projectBackend.project.crawling.crawlMarket;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CrawlMarketDto {
    String name;
    String symbol;
    String current;
    String before;
    String rate;

    public static CrawlMarketDto of(CrawlMarketEntity crawlMarketEntity) {
        return CrawlMarketDto.builder()
                .name(crawlMarketEntity.getCrawlMarketName())
                .symbol(crawlMarketEntity.getCrawlMarketSymbol())
                .current(crawlMarketEntity.getCrawlMarketCurrent())
                .before(crawlMarketEntity.getCrawlMarketBefore())
                .rate(crawlMarketEntity.getCrawlMarketRate())
                .build();
    }

    public CrawlMarketEntity toEntity(CrawlMarketDto dto) {
        return CrawlMarketEntity.builder()
                .crawlMarketName(dto.getName())
                .crawlMarketSymbol(dto.getSymbol())
                .crawlMarketCurrent(dto.getCurrent())
                .crawlMarketBefore(dto.getBefore())
                .crawlMarketRate(dto.getRate())
                .build();
    }
}
