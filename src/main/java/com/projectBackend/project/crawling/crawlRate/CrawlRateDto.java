package com.projectBackend.project.crawling.crawlRate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CrawlRateDto {
    private String name;
    private String interestRate;
    private String change;

    public static CrawlRateDto of(CrawlRateEntity crawlRateEntity) {
        return CrawlRateDto.builder()
                .name(crawlRateEntity.getCrawlRateName())
                .interestRate(crawlRateEntity.getCrawlRateInterestRate())
                .change(crawlRateEntity.getCrawlRateChange())
                .build();
    }

    public CrawlRateEntity toEntity(CrawlRateDto crawlRateDto) {
        return CrawlRateEntity.builder()
                .CrawlRateName(crawlRateDto.getName())
                .CrawlRateInterestRate(crawlRateDto.getInterestRate())
                .CrawlRateChange(crawlRateDto.getChange())
                .build();
    }
}

