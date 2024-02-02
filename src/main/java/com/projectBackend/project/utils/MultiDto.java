package com.projectBackend.project.utils;

import com.projectBackend.project.crawling.crawlAgr.CrawlArgDto;
import com.projectBackend.project.crawling.crawlEnergy.CrawlEnergyDto;
import com.projectBackend.project.crawling.crawlExchange.CrawlExchangeDto;
import com.projectBackend.project.crawling.crawlGold.CrawlGoldDto;
import com.projectBackend.project.crawling.crawlMarket.CrawlMarketDto;
import com.projectBackend.project.crawling.crawlMetal.CrawlMetalDto;
import com.projectBackend.project.crawling.crawlOil.CrawlOilDto;
import com.projectBackend.project.crawling.crawlStock.CrawlStockDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
public class MultiDto {
    // 국제 에너지 가격
    private CrawlEnergyDto crawlEnergyDto;
    private List<CrawlEnergyDto> crawlEnergyDtoList;

    // 국제 농작물 가격
    private CrawlArgDto crawlArgDto;
    private List<CrawlArgDto> crawlArgDtoList;

    // 국제 환율
    private CrawlExchangeDto crawlExchangeDto;
    private List<CrawlExchangeDto> crawlExchangeDtoList;

    // 국제 시장 환율
    private CrawlMarketDto crawlMarketDto;
    private List<CrawlMarketDto> crawlMarketDtoList;

    // 국제 철강 가격
    private CrawlMetalDto crawlMetalDto;
    private List<CrawlMetalDto> crawlMetalDtoList;

    // 주식 상위 10개 항목
    private CrawlStockDto crawlStockDto;
    private List<CrawlStockDto> crawlStockDtoList;

    // 국제 유가
    private CrawlOilDto crawlOilDto;
    private List<CrawlOilDto> crawlOilDtoList;

    // 국제 금가격
    private CrawlGoldDto crawlGoldDto;
    private List<CrawlGoldDto> crawlGoldDtoList;



}
