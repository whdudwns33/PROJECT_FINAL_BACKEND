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
    // 주식 페이지에 사용
    // 국제 에너지 가격
    private CrawlEnergyDto crawlEnergyDto;
    // 국제 농작물 가격
    private CrawlArgDto crawlArgDto;
    // 국제 환율
    private CrawlExchangeDto crawlExchangeDto;
    // 국제 시장 환율
    private CrawlMarketDto crawlMarketDto;
    // 국제 철강 가격
    private CrawlMetalDto crawlMetalDto;
    // 주식 상위 10개 항목
    private CrawlStockDto crawlStockDto;
    // 국제 유가
    private CrawlOilDto crawlOilDto;
    // 국제 금가격
    private CrawlGoldDto crawlGoldDto;
}
