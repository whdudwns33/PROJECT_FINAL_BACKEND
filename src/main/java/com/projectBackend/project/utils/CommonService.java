package com.projectBackend.project.utils;

import com.projectBackend.project.crawling.crawlAgr.CrawlArgService;
import com.projectBackend.project.crawling.crawlDomesticIndicators.CrawlDomesticIndicatorsService;
import com.projectBackend.project.crawling.crawlEnergy.CrawlEnergyService;
import com.projectBackend.project.crawling.crawlExchange.CrawlExchangeService;
import com.projectBackend.project.crawling.crawlGold.CrawlGoldService;
import com.projectBackend.project.crawling.crawlMajorNews.CrawlMajorNewsService;
import com.projectBackend.project.crawling.crawlMarket.CrawlMarketService;
import com.projectBackend.project.crawling.crawlMetal.CrawlMetalService;
import com.projectBackend.project.crawling.crawlOil.CrawlOilService;
import com.projectBackend.project.crawling.crawlOverseasIndicators.CrawlOverseasIndicatorsService;
import com.projectBackend.project.crawling.crawlRate.CrawlRateService;
import com.projectBackend.project.crawling.crawlSearch.CrawlSearchService;
import com.projectBackend.project.crawling.crawlStock.CrawlStockService;
import com.projectBackend.project.stock.StockDto;
import com.projectBackend.project.utils.jwt.TokenProvider;
import com.projectBackend.project.utils.websocket.WebSocketHandler;
import com.projectBackend.project.utils.websocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CommonService {
    private final CrawlStockService crawlStockService;
    private final CrawlEnergyService crawlEnergyService;
    private final CrawlArgService crawlArgService;
    private final CrawlOilService crawlOilService;
    private final CrawlExchangeService crawlExchangeService;
    private final CrawlMetalService crawlMetalService;
    private final CrawlMarketService crawlMarketService;
    private final CrawlGoldService crawlGoldService;
    private final CrawlSearchService crawlSearchService;
    private final CrawlMajorNewsService crawlMajorNewsService;
    private final CrawlDomesticIndicatorsService crawlDomesticIndicatorsService;
    private final CrawlOverseasIndicatorsService crawlOverseasIndicatorsService;
    private final CrawlRateService crawlRateService;
    private final TokenProvider tokenProvider;

    // 회원의 엑세스 토큰에서 이메일값 파싱
    public String returnEmail(MultiDto multiDto) {
        return tokenProvider.getUserEmail(multiDto.getAccessToken());
    }

    public MultiDto getIndex() {
        try {
            MultiDto multiDto = new MultiDto();
            // 농업
            multiDto.setCrawlArgDtoList(crawlArgService.getCrawlArgs());
            // 에너지
            multiDto.setCrawlEnergyDtoList(crawlEnergyService.getCrawlEnergyList());
            // 거래 상위
            multiDto.setCrawlStockDtoList(crawlStockService.getCrawlStockList());
            // 유가
            multiDto.setCrawlOilDtoList(crawlOilService.getCrawlOilList());
            // 철강
            multiDto.setCrawlMetalDtoList(crawlMetalService.getCrawlMetalList());
            // 시장 환율
            multiDto.setCrawlMarketDtoList(crawlMarketService.getCrawlMarketList());
            // 금시세
            multiDto.setCrawlGoldDtoList(crawlGoldService.getCrawlGoldList());
            // 환율
            multiDto.setCrawlExchangeDtoList(crawlExchangeService.getCrawlExchangeList());
            // 검색 상위
            multiDto.setCrawlSearchDtos(crawlSearchService.getCrawlSearchList());
            // 코스닥
            multiDto.setCrawlDomesticIndicatorsDtoList(crawlDomesticIndicatorsService.getCrawlDomesticIndicatorsList());

            return multiDto;
        }
       catch (Exception e) {
            e.printStackTrace();
            return null;
       }
    }
    public MultiDto getMainData() {
        try {
            MultiDto multiDto = new MultiDto();
            // 주요뉴스
            multiDto.setCrawlMajorNewsDtoList(crawlMajorNewsService.getCrawlMajorNewsList());
            // 해외지수
            multiDto.setCrawlOverseasIndicatorsDtoList(crawlOverseasIndicatorsService.getCrawlOverseasIndicatorsList());
            // 거래 상위
            multiDto.setCrawlStockDtoList(crawlStockService.getCrawlStockList());
            // 국내지수
            multiDto.setCrawlDomesticIndicatorsDtoList(crawlDomesticIndicatorsService.getCrawlDomesticIndicatorsList());
            // 금리
            multiDto.setCrawlRateDtoList(crawlRateService.getCrawlRateList());
            // 검색 상위
            multiDto.setCrawlSearchDtos(crawlSearchService.getCrawlSearchList());

            return multiDto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
