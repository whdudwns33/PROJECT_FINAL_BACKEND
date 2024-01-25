package com.projectBackend.project.crawling.crawlMajorNews;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrawlMajorNewsDto {
    private String name;
    private String link;
    private String summary;
    private String media;
    private String date;
}

