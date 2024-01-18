package com.projectBackend.project.crawling.crawlMetal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrawlMetalDto {
    private String name;
    private String units;
    private String price;
    private String yesterday;
    private String rate;
    private String date;
    private String exchange;
}
