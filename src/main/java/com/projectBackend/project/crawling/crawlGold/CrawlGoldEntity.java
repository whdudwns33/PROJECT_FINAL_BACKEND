package com.projectBackend.project.crawling.crawlGold;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity
@Table(name = "crawlGold")
@NoArgsConstructor
public class CrawlGoldEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String crawlGoldName;
    private String crawlGoldUnit;
    private String crawlGoldPrice;
    private String crawlGoldYesterday;
    private String crawlGoldRate;
    private String crawlGoldDate;

    @Builder
    public CrawlGoldEntity(Long id, String crawlGoldName, String crawlGoldUnit,
                           String crawlGoldPrice, String crawlGoldYesterday,
                           String crawlGoldRate, String crawlGoldDate) {
        this.id = id;
        this.crawlGoldName = crawlGoldName;
        this.crawlGoldUnit = crawlGoldUnit;
        this.crawlGoldPrice = crawlGoldPrice;
        this.crawlGoldYesterday = crawlGoldYesterday;
        this.crawlGoldRate = crawlGoldRate;
        this.crawlGoldDate = crawlGoldDate;
    }
}
