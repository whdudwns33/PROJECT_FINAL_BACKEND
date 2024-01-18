package com.projectBackend.project.crawling.crawlOil;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "crawlOil")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CrawlOilEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String CrawlOilName;
    private String CrawlOilUnit;
    private String CrawlOilPrice;
    private String CrawlOilYesterday;
    private String CrawlOilRate;

    @Builder
    public CrawlOilEntity(String crawlOilName, String crawlOilUnit, String crawlOilPrice, String crawlOilYesterday, String crawlOilRate) {
        this.CrawlOilName = crawlOilName;
        this.CrawlOilUnit = crawlOilUnit;
        this.CrawlOilPrice = crawlOilPrice;
        this.CrawlOilYesterday = crawlOilYesterday;
        this.CrawlOilRate = crawlOilRate;
    }
}
