package com.projectBackend.project.crawling.crawlMajorNews;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CrawlMajorNewsDto {
    private String name;
    private String link;
    private String summary;
    private String media;
    private String date;

    public static CrawlMajorNewsDto of(CrawlMajorNewsEntity entity) {
        return CrawlMajorNewsDto.builder()
                .name(entity.getCrawlMajorNewsName())
                .link(entity.getCrawlMajorNewsLink())
                .summary(entity.getCrawlMajorNewsSummary())
                .media(entity.getCrawlMajorNewsMedia())
                .date(entity.getCrawlMajorNewsDate())
                .build();
    }

    public CrawlMajorNewsEntity toEntity(CrawlMajorNewsDto dto) {
        return CrawlMajorNewsEntity.builder()
                .CrawlMajorNewsName(dto.getName())
                .CrawlMajorNewsLink(dto.getLink())
                .CrawlMajorNewsSummary(dto.getSummary())
                .CrawlMajorNewsMedia(dto.getMedia())
                .CrawlMajorNewsDate(dto.getDate())
                .build();
    }
}

