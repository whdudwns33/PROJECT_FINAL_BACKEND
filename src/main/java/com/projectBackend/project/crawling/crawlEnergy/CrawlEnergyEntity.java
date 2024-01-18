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
    private String CrawlEnergyName;
    private String CrawlEnergyMonth;
    private String CrawlEnergyUnits;
    private String CrawlEnergyPrice;
    private String CrawlEnergyYesterday;
    private String CrawlEnergyRate;
    private String CrawlEnergyDate;
    private String CrawlEnergyExchange;

    @Builder
    public CrawlEnergyEntity(Long id, String CrawlEnergyName, String CrawlEnergyMonth,
                             String CrawlEnergyUnits, String CrawlEnergyPrice,
                             String CrawlEnergyYesterday, String CrawlEnergyRate,
                             String CrawlEnergyDate, String CrawlEnergyExchange) {
        this.id = id;
        this.CrawlEnergyName = CrawlEnergyName;
        this.CrawlEnergyMonth = CrawlEnergyMonth;
        this.CrawlEnergyUnits = CrawlEnergyUnits;
        this.CrawlEnergyPrice = CrawlEnergyPrice;
        this.CrawlEnergyYesterday = CrawlEnergyYesterday;
        this.CrawlEnergyRate = CrawlEnergyRate;
        this.CrawlEnergyDate = CrawlEnergyDate;
        this.CrawlEnergyExchange = CrawlEnergyExchange;
    }
}
