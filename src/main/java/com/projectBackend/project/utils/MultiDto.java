package com.projectBackend.project.utils;

import com.projectBackend.project.crawling.crawlAgr.CrawlArgDto;
import com.projectBackend.project.crawling.crawlEnergy.CrawlEnergyDto;
import com.projectBackend.project.crawling.crawlExchange.CrawlExchangeDto;
import com.projectBackend.project.crawling.crawlGold.CrawlGoldDto;
import com.projectBackend.project.crawling.crawlMarket.CrawlMarketDto;
import com.projectBackend.project.crawling.crawlMetal.CrawlMetalDto;
import com.projectBackend.project.crawling.crawlOil.CrawlOilDto;
import com.projectBackend.project.crawling.crawlStock.CrawlStockDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiDto {
    private CrawlEnergyDto crawlEnergyDto;
    private CrawlArgDto crawlArgDto;
    private CrawlExchangeDto crawlExchangeDto;
    private CrawlMarketDto crawlMarketDto;
    private CrawlMetalDto crawlMetalDto;
    private CrawlStockDto crawlStockDto;
    private CrawlOilDto crawlOilDto;
    private CrawlGoldDto crawlGoldDto;
}
