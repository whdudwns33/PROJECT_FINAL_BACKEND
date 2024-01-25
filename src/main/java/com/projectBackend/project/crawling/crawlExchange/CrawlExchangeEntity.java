package com.projectBackend.project.crawling.crawlExchange;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity
@Table(name = "crawlExchange")
@NoArgsConstructor
public class CrawlExchangeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String crawlExchangeName;
    private String crawlExchangeTbRate;
    private String crawlExchangeBuy;
    private String crawlExchangeSell;
    private String crawlExchangeSend;
    private String crawlExchangeReceive;
    private String crawlExchangeExchange;

    @Builder
    public CrawlExchangeEntity(Long id, String crawlExchangeName, String crawlExchangeTbRate,
                               String crawlExchangeBuy, String crawlExchangeSell, String crawlExchangeSend,
                               String crawlExchangeReceive, String crawlExchangeExchange) {
        this.id = id;
        this.crawlExchangeName = crawlExchangeName;
        this.crawlExchangeTbRate = crawlExchangeTbRate;
        this.crawlExchangeBuy = crawlExchangeBuy;
        this.crawlExchangeSell = crawlExchangeSell;
        this.crawlExchangeSend = crawlExchangeSend;
        this.crawlExchangeReceive = crawlExchangeReceive;
        this.crawlExchangeExchange = crawlExchangeExchange;
    }
}
