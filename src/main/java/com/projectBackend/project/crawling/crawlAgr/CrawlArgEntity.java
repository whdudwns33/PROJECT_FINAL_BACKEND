package com.projectBackend.project.crawling.crawlAgr;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity
@Table(name = "crawlAgr")
@NoArgsConstructor
public class CrawlArgEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String crawlExchangeName;
    private String crawlExchangeMonth;
    private String crawlExchangeUnits;
    private String crawlExchangePrice;
    private String crawlExchangeYesterday;
    private String crawlExchangeRate;
    private String crawlExchangeDate;
    private String crawlExchangeExchange;

    @Builder
    public CrawlArgEntity(String crawlExchangeName, String crawlExchangeMonth, String crawlExchangeUnits, String crawlExchangePrice, String crawlExchangeYesterday, String crawlExchangeRate, String crawlExchangeDate, String crawlExchangeExchange) {
        this.crawlExchangeName = crawlExchangeName;
        this.crawlExchangeMonth = crawlExchangeMonth;
        this.crawlExchangeUnits = crawlExchangeUnits;
        this.crawlExchangePrice = crawlExchangePrice;
        this.crawlExchangeYesterday = crawlExchangeYesterday;
        this.crawlExchangeRate = crawlExchangeRate;
        this.crawlExchangeDate = crawlExchangeDate;
        this.crawlExchangeExchange = crawlExchangeExchange;
    }
}
