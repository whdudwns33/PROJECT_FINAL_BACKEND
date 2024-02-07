package com.projectBackend.project.crawling.crawlDomesticIndicators;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor  // 기본 생성자 추가
@Entity
@Table(name = "crawl_domestic_indicators")
public class CrawlDomesticIndicatorsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String crawlDomesticIndicatorsName;
    private String crawlDomesticIndicatorsPrice;
    private String crawlDomesticIndicatorsChange;

    @Builder
    public CrawlDomesticIndicatorsEntity(Long id, String crawlDomesticIndicatorsName, String crawlDomesticIndicatorsPrice,
                                         String crawlDomesticIndicatorsChange) {
        this.id = id;
        this.crawlDomesticIndicatorsName = crawlDomesticIndicatorsName;
        this.crawlDomesticIndicatorsPrice = crawlDomesticIndicatorsPrice;
        this.crawlDomesticIndicatorsChange = crawlDomesticIndicatorsChange;
    }
}
