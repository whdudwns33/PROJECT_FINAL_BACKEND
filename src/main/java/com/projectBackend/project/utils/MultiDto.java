package com.projectBackend.project.utils;

import com.projectBackend.project.buy.BuyDto;
import com.projectBackend.project.community.CommunityDto;
import com.projectBackend.project.crawling.crawlAgr.CrawlArgDto;
import com.projectBackend.project.crawling.crawlDomesticIndicators.CrawlDomesticIndicatorsDto;
import com.projectBackend.project.crawling.crawlEnergy.CrawlEnergyDto;
import com.projectBackend.project.crawling.crawlExchange.CrawlExchangeDto;
import com.projectBackend.project.crawling.crawlGold.CrawlGoldDto;
import com.projectBackend.project.crawling.crawlMajorNews.CrawlMajorNewsDto;
import com.projectBackend.project.crawling.crawlMarket.CrawlMarketDto;
import com.projectBackend.project.crawling.crawlMetal.CrawlMetalDto;
import com.projectBackend.project.crawling.crawlOil.CrawlOilDto;
import com.projectBackend.project.crawling.crawlOverseasIndicators.CrawlOverseasIndicatorsDto;
import com.projectBackend.project.crawling.crawlRate.CrawlRateDto;
import com.projectBackend.project.crawling.crawlSearch.CrawlSearchDto;
import com.projectBackend.project.crawling.crawlStock.CrawlStockDto;
import com.projectBackend.project.member.MemberDto;
import com.projectBackend.project.stock.StockDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MultiDto {
    // 회원 정보
    private MemberDto memberDto;
    private List<MemberDto> memberDtoList;

    // 토큰
    private String accessToken;

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

    // 인덱스
    private CrawlDomesticIndicatorsDto crawlDomesticIndicatorsDto;
    private List<CrawlDomesticIndicatorsDto> crawlDomesticIndicatorsDtoList;

    // 해외지수
    private CrawlOverseasIndicatorsDto crawlOverseasIndicatorsDto;
    private List<CrawlOverseasIndicatorsDto> crawlOverseasIndicatorsDtoList;

    // 검색 상위
    private CrawlSearchDto crawlSearchDto;
    private List<CrawlSearchDto> crawlSearchDtos;

    // 주식 정보
    private StockDto stockDto;
    private List<StockDto> stockDtoList;

    // 구매 정보
    private BuyDto buyDto;
    private List<BuyDto> buyDtoList;

    // 종목 토론 정보
    private CommunityDto communityDto;
    private List<CommunityDto> communityDtoList;

    // 주요 뉴스
    private CrawlMajorNewsDto crawlMajorNewsDto;
    private List<CrawlMajorNewsDto> crawlMajorNewsDtoList;

    // 금리
    private CrawlRateDto crawlRateDto;
    private List<CrawlRateDto> crawlRateDtoList;

}
