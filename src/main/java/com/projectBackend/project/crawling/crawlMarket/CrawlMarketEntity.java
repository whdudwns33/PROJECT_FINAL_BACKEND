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
    private String crawlMarketName;
    private String crawlMarketSymbol;
    private String crawlMarketCurrent;
    private String crawlMarketBefore;
    private String crawlMarketRate;

    @Builder
    public CrawlMarketEntity(String crawlMarketName, String crawlMarketSymbol, String crawlMarketCurrent, String crawlMarketBefore, String crawlMarketRate) {
        this.crawlMarketName = crawlMarketName;
        this.crawlMarketSymbol = crawlMarketSymbol;
        this.crawlMarketCurrent = crawlMarketCurrent;
        this.crawlMarketBefore = crawlMarketBefore;
        this.crawlMarketRate = crawlMarketRate;
    }
}
