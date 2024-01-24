package com.projectBackend.project.crawling.crawlOverseasIndicators;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity
@Table(name = "crawlGold")
@NoArgsConstructor
public class CrawlOverseasIndicatorsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String CrawlOverseasIndicatorsName;
    private String CrawlOverseasIndicatorsPrice;
    private String CrawlOverseasIndicatorsChange;

    @Builder
    public CrawlOverseasIndicatorsEntity(Long id, String CrawlOverseasIndicatorsName, String CrawlOverseasIndicatorsPrice,
                                         String CrawlOverseasIndicatorsChange) {
        this.id = id;
        this.CrawlOverseasIndicatorsName = CrawlOverseasIndicatorsName;
        this.CrawlOverseasIndicatorsPrice = CrawlOverseasIndicatorsPrice;
        this.CrawlOverseasIndicatorsChange = CrawlOverseasIndicatorsChange;
    }
}
