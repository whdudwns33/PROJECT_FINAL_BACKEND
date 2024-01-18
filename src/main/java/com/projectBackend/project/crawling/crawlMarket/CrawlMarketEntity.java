package com.projectBackend.project.crawling.crawlMarket;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity
@Table(name = "crawlMarket")
@NoArgsConstructor
public class CrawlMarketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String CrawlMarketName;
    private String CrawlMarketSymbol;
    private String CrawlMarketCurrent;
    private String CrawlMarketBefore;
    private String CrawlMarketRate;

    @Builder
    public CrawlMarketEntity(String CrawlMarketName, String CrawlMarketSymbol, String CrawlMarketCurrent, String CrawlMarketBefore, String CrawlMarketRate) {
        this.CrawlMarketName = CrawlMarketName;
        this.CrawlMarketSymbol = CrawlMarketSymbol;
        this.CrawlMarketCurrent = CrawlMarketCurrent;
        this.CrawlMarketBefore = CrawlMarketBefore;
        this.CrawlMarketRate = CrawlMarketRate;
    }
}
