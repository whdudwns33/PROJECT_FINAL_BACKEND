package com.projectBackend.project.crawling.crawlEnergy;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CrawlEnergyDto {
    private String name;
    private String month;
    private String units;
    private String price;
    private String rate;
    private String date;
    private String exchange;

    public static CrawlEnergyDto of(CrawlEnergyEntity crawlEnergyEntity) {
        return CrawlEnergyDto.builder()
                .name(crawlEnergyEntity.getCrawlEnergyName())
                .month(crawlEnergyEntity.getCrawlEnergyMonth())
                .units(crawlEnergyEntity.getCrawlEnergyUnits())
                .price(crawlEnergyEntity.getCrawlEnergyPrice())
                .rate(crawlEnergyEntity.getCrawlEnergyRate())
                .date(crawlEnergyEntity.getCrawlEnergyDate())
                .exchange(crawlEnergyEntity.getCrawlEnergyExchange())
                .build();
    }

    public CrawlEnergyEntity toEntity(CrawlEnergyDto dto) {
        return CrawlEnergyEntity.builder()
                .crawlEnergyName(dto.getName())
                .crawlEnergyMonth(dto.getMonth())
                .crawlEnergyUnits(dto.getUnits())
                .crawlEnergyPrice(dto.getPrice())
                .crawlEnergyRate(dto.getRate())
                .crawlEnergyDate(dto.getDate())
                .crawlEnergyExchange(dto.getExchange())
                .build();
    }
}
