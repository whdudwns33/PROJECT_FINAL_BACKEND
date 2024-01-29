package com.projectBackend.project.crawling.crawlGold;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CrawlGoldDto {
    private String name;
    private String unit;
    private String price;
    private String yesterday;
    private String rate;
    private String date;

    public static CrawlGoldDto of(CrawlGoldEntity crawlGoldEntity) {
        return CrawlGoldDto.builder()
                .name(crawlGoldEntity.getCrawlGoldName())
                .unit(crawlGoldEntity.getCrawlGoldUnit())
                .price(crawlGoldEntity.getCrawlGoldPrice())
                .yesterday(crawlGoldEntity.getCrawlGoldYesterday())
                .rate(crawlGoldEntity.getCrawlGoldRate())
                .date(crawlGoldEntity.getCrawlGoldDate())
                .build();
    }

    public CrawlGoldEntity toEntity(CrawlGoldDto dto) {
        return CrawlGoldEntity.builder()
                .crawlGoldName(dto.getName())
                .crawlGoldUnit(dto.getUnit())
                .crawlGoldPrice(dto.getPrice())
                .crawlGoldYesterday(dto.getYesterday())
                .crawlGoldRate(dto.getRate())
                .crawlGoldDate(dto.getDate())
                .build();
    }
}
