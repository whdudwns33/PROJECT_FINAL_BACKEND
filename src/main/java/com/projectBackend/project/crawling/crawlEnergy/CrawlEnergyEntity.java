package com.projectBackend.project.crawling.crawlEnergy;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity
@Table(name = "crawlEnergy")
@NoArgsConstructor
public class CrawlEnergyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String crawlEnergyName;
    private String crawlEnergyMonth;
    private String crawlEnergyUnits;
    private String crawlEnergyPrice;
//    private String crawlEnergyYesterday;
    private String crawlEnergyRate;
    private String crawlEnergyDate;
    private String crawlEnergyExchange;

    @Builder
    public CrawlEnergyEntity(Long id, String crawlEnergyName, String crawlEnergyMonth,
                             String crawlEnergyUnits, String crawlEnergyPrice,
                             String crawlEnergyYesterday, String crawlEnergyRate,
                             String crawlEnergyDate, String crawlEnergyExchange) {
        this.id = id;
        this.crawlEnergyName = crawlEnergyName;
        this.crawlEnergyMonth = crawlEnergyMonth;
        this.crawlEnergyUnits = crawlEnergyUnits;
        this.crawlEnergyPrice = crawlEnergyPrice;
//        this.crawlEnergyYesterday = crawlEnergyYesterday;
        this.crawlEnergyRate = crawlEnergyRate;
        this.crawlEnergyDate = crawlEnergyDate;
        this.crawlEnergyExchange = crawlEnergyExchange;
    }
}
