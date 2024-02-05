package com.projectBackend.project.utils;

import com.projectBackend.project.crawling.crawlAgr.CrawlArgService;
import com.projectBackend.project.crawling.crawlEnergy.CrawlEnergyService;
import com.projectBackend.project.crawling.crawlExchange.CrawlExchangeService;
import com.projectBackend.project.crawling.crawlGold.CrawlGoldService;
import com.projectBackend.project.crawling.crawlMarket.CrawlMarketService;
import com.projectBackend.project.crawling.crawlMetal.CrawlMetalService;
import com.projectBackend.project.crawling.crawlOil.CrawlOilService;
import com.projectBackend.project.crawling.crawlRate.CrawlRateService;
import com.projectBackend.project.crawling.crawlStock.CrawlStockService;
import com.projectBackend.project.stock.StockDto;
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


    public MultiDto getIndex() {
        try {
            MultiDto multiDto = new MultiDto();
            multiDto.setCrawlArgDtoList(crawlArgService.getCrawlArgs());
            multiDto.setCrawlEnergyDtoList(crawlEnergyService.getCrawlEnergyList());
            multiDto.setCrawlStockDtoList(crawlStockService.getCrawlStockList());
            multiDto.setCrawlOilDtoList(crawlOilService.getCrawlOilList());
            multiDto.setCrawlMetalDtoList(crawlMetalService.getCrawlMetalList());
            multiDto.setCrawlMarketDtoList(crawlMarketService.getCrawlMarketList());
            multiDto.setCrawlGoldDtoList(crawlGoldService.getCrawlGoldList());
            multiDto.setCrawlExchangeDtoList(crawlExchangeService.getCrawlExchangeList());
            return multiDto;
        }
       catch (Exception e) {
            e.printStackTrace();
            return null;
       }
    }
}
