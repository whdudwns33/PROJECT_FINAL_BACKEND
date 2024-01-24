package com.projectBackend.project.crawling.crawlDomesticIndicators;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity
@Table(name = "crawlGold")
@NoArgsConstructor
public class CrawlDomesticIndicatorsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String CrawlDomesticIndicatorsName;
    private String CrawlDomesticIndicatorsPrice;
    private String CrawlDomesticIndicatorsChange;

    @Builder
    public CrawlDomesticIndicatorsEntity(Long id, String CrawlDomesticIndicatorsName, String CrawlDomesticIndicatorsPrice,
                                         String CrawlDomesticIndicatorsChange) {
        this.id = id;
        this.CrawlDomesticIndicatorsName = CrawlDomesticIndicatorsName;
        this.CrawlDomesticIndicatorsPrice = CrawlDomesticIndicatorsPrice;
        this.CrawlDomesticIndicatorsChange = CrawlDomesticIndicatorsChange;
    }
}
