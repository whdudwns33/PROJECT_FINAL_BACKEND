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
    private String CrawlExchangeName;
    private String CrawlExchangeTbRate;
    private String CrawlExchangeBuy;
    private String CrawlExchangeSell;
    private String CrawlExchangeSend;
    private String CrawlExchangeReceive;
    private String CrawlExchangeExchange;

    @Builder
    public CrawlExchangeEntity(Long id, String CrawlExchangeName, String CrawlExchangeTbRate,
                               String CrawlExchangeBuy, String CrawlExchangeSell, String CrawlExchangeSend,
                               String CrawlExchangeReceive, String CrawlExchangeExchange) {
        this.id = id;
        this.CrawlExchangeName = CrawlExchangeName;
        this.CrawlExchangeTbRate = CrawlExchangeTbRate;
        this.CrawlExchangeBuy = CrawlExchangeBuy;
        this.CrawlExchangeSell = CrawlExchangeSell;
        this.CrawlExchangeSend = CrawlExchangeSend;
        this.CrawlExchangeReceive = CrawlExchangeReceive;
        this.CrawlExchangeExchange = CrawlExchangeExchange;
    }
}
