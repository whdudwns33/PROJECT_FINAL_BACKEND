package com.projectBackend.project.crawling.crawlOil;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CrawlOilDto {
    private String name;
    private String unit;
    private String price;
    private String yesterday;
    private String rate;

    public static CrawlOilDto of(CrawlOilEntity crawlOilEntity) {
        return CrawlOilDto.builder()
                .name(crawlOilEntity.getCrawlOilName())
                .unit(crawlOilEntity.getCrawlOilUnit())
                .price(crawlOilEntity.getCrawlOilPrice())
                .yesterday(crawlOilEntity.getCrawlOilYesterday())
                .rate(crawlOilEntity.getCrawlOilRate())
                .build();
    }

    public CrawlOilEntity toEntity(CrawlOilDto dto) {
        return CrawlOilEntity.builder()
                .crawlOilName(dto.getName())
                .crawlOilUnit(dto.getUnit())
                .crawlOilPrice(dto.getPrice())
                .crawlOilYesterday(dto.getYesterday())
                .crawlOilRate(dto.getRate())
                .build();
    }
}
