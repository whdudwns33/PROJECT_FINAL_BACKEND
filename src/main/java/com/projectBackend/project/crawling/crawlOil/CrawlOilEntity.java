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

    private String crawlOilName;
    private String crawlOilUnit;
    private String crawlOilPrice;
    private String crawlOilYesterday;
    private String crawlOilRate;

    @Builder
    public CrawlOilEntity(String crawlOilName, String crawlOilUnit, String crawlOilPrice, String crawlOilYesterday, String crawlOilRate) {
        this.crawlOilName = crawlOilName;
        this.crawlOilUnit = crawlOilUnit;
        this.crawlOilPrice = crawlOilPrice;
        this.crawlOilYesterday = crawlOilYesterday;
        this.crawlOilRate = crawlOilRate;
    }
}
