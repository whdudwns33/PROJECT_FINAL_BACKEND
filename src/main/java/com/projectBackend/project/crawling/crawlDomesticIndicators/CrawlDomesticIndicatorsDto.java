package com.projectBackend.project.crawling.crawlDomesticIndicators;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CrawlDomesticIndicatorsDto {
    private Long id;
    private String crawlDomesticIndicatorsName;
    private String crawlDomesticIndicatorsPrice;
    private String crawlDomesticIndicatorsChange;

    public static CrawlDomesticIndicatorsDto of(CrawlDomesticIndicatorsEntity entity) {
        return CrawlDomesticIndicatorsDto.builder()
                .id(entity.getId())
                .crawlDomesticIndicatorsName(entity.getCrawlDomesticIndicatorsName())
                .crawlDomesticIndicatorsPrice(entity.getCrawlDomesticIndicatorsPrice())
                .crawlDomesticIndicatorsChange(entity.getCrawlDomesticIndicatorsChange())
                .build();
    }

    public static CrawlDomesticIndicatorsDto toDto(CrawlDomesticIndicatorsEntity entity) {
        return CrawlDomesticIndicatorsDto.builder()
                .id(entity.getId())
                .crawlDomesticIndicatorsName(entity.getCrawlDomesticIndicatorsName())
                .crawlDomesticIndicatorsPrice(entity.getCrawlDomesticIndicatorsPrice())
                .crawlDomesticIndicatorsChange(entity.getCrawlDomesticIndicatorsChange())
                .build();
    }

    public CrawlDomesticIndicatorsEntity toEntity(CrawlDomesticIndicatorsDto dto) {
        return CrawlDomesticIndicatorsEntity.builder()
                .id(dto.getId())
                .crawlDomesticIndicatorsName(dto.getCrawlDomesticIndicatorsName())
                .crawlDomesticIndicatorsPrice(dto.getCrawlDomesticIndicatorsPrice())
                .crawlDomesticIndicatorsChange(dto.getCrawlDomesticIndicatorsChange())
                .build();
    }
}
