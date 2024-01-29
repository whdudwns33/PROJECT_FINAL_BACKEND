package com.projectBackend.project.crawling.crawlExchange;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CrawlExchangeDto {
    private String name;
    private String TbRate;
    private String buy;
    private String sell;
    private String send;
    private String receive;
    private String exchange;

    public static CrawlExchangeDto of(CrawlExchangeEntity crawlExchangeEntity) {
        return CrawlExchangeDto.builder()
                .name(crawlExchangeEntity.getCrawlExchangeName())
                .TbRate(crawlExchangeEntity.getCrawlExchangeTbRate())
                .buy(crawlExchangeEntity.getCrawlExchangeBuy())
                .sell(crawlExchangeEntity.getCrawlExchangeSell())
                .send(crawlExchangeEntity.getCrawlExchangeSend())
                .receive(crawlExchangeEntity.getCrawlExchangeReceive())
                .exchange(crawlExchangeEntity.getCrawlExchangeExchange())
                .build();
    }

    public CrawlExchangeEntity toEntity(CrawlExchangeDto dto) {
        return CrawlExchangeEntity.builder()
                .crawlExchangeName(dto.getName())
                .crawlExchangeTbRate(dto.getTbRate())
                .crawlExchangeBuy(dto.getBuy())
                .crawlExchangeSell(dto.getSell())
                .crawlExchangeSend(dto.getSend())
                .crawlExchangeReceive(dto.getReceive())
                .crawlExchangeExchange(dto.getExchange())
                .build();
    }
}
