package com.projectBackend.project.crawling.crawlSearch;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CrawlSearchDto {
    private String searchRank;
    private String searchName;
    private String searchCount;
    private String searchUpdown;
    private String searchPrice;
    private String searchChangeRate;
    private String searchMarketCap;

    public static CrawlSearchDto of(CrawlSearchEntity crawlSearchEntity) {
        return CrawlSearchDto.builder()
                .searchRank(crawlSearchEntity.getSearchRank())
                .searchName(crawlSearchEntity.getSearchName())
                .searchCount(crawlSearchEntity.getSearchCount())
                .searchUpdown(crawlSearchEntity.getSearchUpdown())
                .searchPrice(crawlSearchEntity.getSearchPrice())
                .searchChangeRate(crawlSearchEntity.getSearchChangeRate())
                .searchMarketCap(crawlSearchEntity.getSearchMarketCap())
                .build();
    }

    public CrawlSearchEntity toEntity(CrawlSearchDto dto) {
        return CrawlSearchEntity.builder()
                .searchRank(dto.getSearchRank())
                .searchName(dto.getSearchName())
                .searchCount(dto.getSearchCount())
                .searchUpdown(dto.getSearchUpdown())
                .searchPrice(dto.getSearchPrice())
                .searchChangeRate(dto.getSearchChangeRate())
                .searchMarketCap(dto.getSearchMarketCap())
                .build();
    }
}
