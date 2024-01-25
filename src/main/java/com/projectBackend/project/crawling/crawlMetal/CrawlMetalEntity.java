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

    private String crawlMetalName;
    private String crawlMetalUnits;
    private String crawlMetalPrice;
    private String crawlMetalYesterday;
    private String crawlMetalRate;
    private String crawlMetalDate;
    private String crawlMetalExchange;

    @Builder
    public CrawlMetalEntity(Long id, String crawlMetalName,
                            String crawlMetalUnits, String crawlMetalPrice,
                            String crawlMetalYesterday, String crawlMetalRate,
                            String crawlMetalDate, String crawlMetalExchange) {
        this.id = id;
        this.crawlMetalName = crawlMetalName;
        this.crawlMetalUnits = crawlMetalUnits;
        this.crawlMetalPrice = crawlMetalPrice;
        this.crawlMetalYesterday = crawlMetalYesterday;
        this.crawlMetalRate = crawlMetalRate;
        this.crawlMetalDate = crawlMetalDate;
        this.crawlMetalExchange = crawlMetalExchange;
    }
}
