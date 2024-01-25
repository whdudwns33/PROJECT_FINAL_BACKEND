package com.projectBackend.project.crawling.crawlEnergy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrawlEnergyDto {
    private String name;
    private String month;
    private String units;
    private String price;
//    private String yesterday;
    private String rate;
    private String date;
    private String exchange;
}
