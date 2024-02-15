package com.projectBackend.project.crawling.crawlOverseasIndicators;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "crawlOverseasIndicators")
public class CrawlOverseasIndicatorsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String crawlOverseasIndicatorsName;
    private String crawlOverseasIndicatorsPrice;
    private String crawlOverseasIndicatorsChange;

    @Builder
    public CrawlOverseasIndicatorsEntity(Long id, String crawlOverseasIndicatorsName, String crawlOverseasIndicatorsPrice,
                                         String crawlOverseasIndicatorsChange) {
        this.id = id;
        this.crawlOverseasIndicatorsName = crawlOverseasIndicatorsName;
        this.crawlOverseasIndicatorsPrice = crawlOverseasIndicatorsPrice;
        this.crawlOverseasIndicatorsChange = crawlOverseasIndicatorsChange;
    }
}
