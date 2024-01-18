package com.projectBackend.project.crawling.crawlMarket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrawlMarketDto {
    String name;
    String symbol;
    String current;
    String before;
    String rate;
}
