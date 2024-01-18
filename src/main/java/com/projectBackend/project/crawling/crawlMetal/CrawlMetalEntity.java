package com.projectBackend.project.crawling.crawlMetal;


import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity
@Table(name = "crawlMetal")
@NoArgsConstructor
public class CrawlMetalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String CrawlMetalName;
    private String CrawlMetalUnits;
    private String CrawlMetalPrice;
    private String CrawlMetalYesterday;
    private String CrawlMetalRate;
    private String CrawlMetalDate;
    private String CrawlMetalExchange;

    @Builder
    public CrawlMetalEntity(Long id, String crawlMetalName,
                            String crawlMetalUnits, String crawlMetalPrice,
                            String crawlMetalYesterday, String crawlMetalRate,
                            String crawlMetalDate, String crawlMetalExchange) {
        this.id = id;
        this.CrawlMetalName = crawlMetalName;
        this.CrawlMetalUnits = crawlMetalUnits;
        this.CrawlMetalPrice = crawlMetalPrice;
        this.CrawlMetalYesterday = crawlMetalYesterday;
        this.CrawlMetalRate = crawlMetalRate;
        this.CrawlMetalDate = crawlMetalDate;
        this.CrawlMetalExchange = crawlMetalExchange;
    }
}
