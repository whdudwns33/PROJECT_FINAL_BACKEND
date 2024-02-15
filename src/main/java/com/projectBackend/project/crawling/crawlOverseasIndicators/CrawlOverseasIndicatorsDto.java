package com.projectBackend.project.crawling.crawlOverseasIndicators;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CrawlOverseasIndicatorsDto {
    private Long id;
    private String name;
    private String price;
    private String change;
    
    public static CrawlOverseasIndicatorsDto of(CrawlOverseasIndicatorsEntity entity) {
        return CrawlOverseasIndicatorsDto.builder()
                .id(entity.getId())
                .name(entity.getCrawlOverseasIndicatorsName())
                .price(entity.getCrawlOverseasIndicatorsPrice())
                .change(entity.getCrawlOverseasIndicatorsChange())
                .build();
    }

    public CrawlOverseasIndicatorsEntity toEntity(CrawlOverseasIndicatorsDto dto) {
        return CrawlOverseasIndicatorsEntity.builder()
                .id(dto.getId())
                .crawlOverseasIndicatorsName(dto.getName())
                .crawlOverseasIndicatorsPrice(dto.getPrice())
                .crawlOverseasIndicatorsChange(dto.getChange())
                .build();
    }
}
