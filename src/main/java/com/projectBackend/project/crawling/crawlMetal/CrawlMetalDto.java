package com.projectBackend.project.crawling.crawlMetal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CrawlMetalDto {
    private String name;
    private String units;
    private String price;
    private String yesterday;
    private String rate;
    private String date;
    private String exchange;

    public static CrawlMetalDto of(CrawlMetalEntity crawlMetalEntity) {
        return CrawlMetalDto.builder()
                .name(crawlMetalEntity.getCrawlMetalName())
                .units(crawlMetalEntity.getCrawlMetalUnits())
                .price(crawlMetalEntity.getCrawlMetalPrice())
                .yesterday(crawlMetalEntity.getCrawlMetalYesterday())
                .rate(crawlMetalEntity.getCrawlMetalRate())
                .date(crawlMetalEntity.getCrawlMetalDate())
                .exchange(crawlMetalEntity.getCrawlMetalExchange())
                .build();
    }

    public CrawlMetalEntity toEntity(CrawlMetalDto dto) {
        return CrawlMetalEntity.builder()
                .crawlMetalName(dto.getName())
                .crawlMetalUnits(dto.getUnits())
                .crawlMetalPrice(dto.getPrice())
                .crawlMetalYesterday(dto.getYesterday())
                .crawlMetalRate(dto.getRate())
                .crawlMetalDate(dto.getDate())
                .crawlMetalExchange(dto.getExchange())
                .build();
    }
}
