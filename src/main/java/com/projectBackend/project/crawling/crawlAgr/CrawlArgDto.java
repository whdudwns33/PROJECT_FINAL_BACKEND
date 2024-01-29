package com.projectBackend.project.crawling.crawlAgr;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CrawlArgDto {
    String name;
    String month;
    String units;
    String price;
    String yesterday;
    String rate;
    String date;
    String exchange;

    public static CrawlArgDto of(CrawlArgEntity crawlArgEntity) {
        return CrawlArgDto.builder()
                .name(crawlArgEntity.getCrawlExchangeName())
                .month(crawlArgEntity.getCrawlExchangeMonth())
                .units(crawlArgEntity.getCrawlExchangeUnits())
                .price(crawlArgEntity.getCrawlExchangePrice())
                .yesterday(crawlArgEntity.getCrawlExchangeYesterday())
                .rate(crawlArgEntity.getCrawlExchangeRate())
                .date(crawlArgEntity.getCrawlExchangeDate())
                .exchange(crawlArgEntity.getCrawlExchangeExchange())
                .build();
    }

    public CrawlArgEntity toEntity(CrawlArgDto dto) {
        return CrawlArgEntity.builder()
                .crawlExchangeName(dto.getName())
                .crawlExchangeMonth(dto.getMonth())
                .crawlExchangeUnits(dto.getUnits())
                .crawlExchangePrice(dto.getPrice())
                .crawlExchangeYesterday(dto.getYesterday())
                .crawlExchangeRate(dto.getRate())
                .crawlExchangeDate(dto.getDate())
                .crawlExchangeExchange(dto.getExchange())
                .build();
    }
}
