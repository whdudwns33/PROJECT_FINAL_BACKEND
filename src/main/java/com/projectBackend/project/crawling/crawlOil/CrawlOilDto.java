package com.projectBackend.project.crawling.crawlOil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrawlOilDto {
    private String name;
    private String unit;
    private String price;
    private String yesterday;
    private String rate;
}
