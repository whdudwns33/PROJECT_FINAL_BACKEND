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
    private String CrawlExchangeName;
    private String CrawlExchangeMonth;
    private String CrawlExchangeUnits;
    private String CrawlExchangePrice;
    private String CrawlExchangeYesterday;
    private String CrawlExchangeRate;
    private String CrawlExchangeDate;
    private String CrawlExchangeExchange;

    @Builder
    public CrawlArgEntity(String CrawlExchangeName, String CrawlExchangeMonth, String CrawlExchangeUnits, String CrawlExchangePrice, String CrawlExchangeYesterday , String CrawlExchangeRate, String CrawlExchangeDate, String CrawlExchangeExchange) {
        this.CrawlExchangeName = CrawlExchangeName;
        this.CrawlExchangeMonth = CrawlExchangeMonth;
        this.CrawlExchangeUnits = CrawlExchangeUnits;
        this.CrawlExchangePrice = CrawlExchangePrice;
        this.CrawlExchangeYesterday = CrawlExchangeYesterday;
        this.CrawlExchangeRate = CrawlExchangeRate;
        this.CrawlExchangeDate = CrawlExchangeDate;
        this.CrawlExchangeExchange = CrawlExchangeExchange;
    }
}
