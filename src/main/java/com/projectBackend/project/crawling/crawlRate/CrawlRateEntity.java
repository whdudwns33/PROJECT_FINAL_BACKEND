package com.projectBackend.project.crawling.crawlRate;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity
@Table(name = "crawlRate")
@NoArgsConstructor
public class CrawlRateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String CrawlRateName;
    private String CrawlRateInterestRate;
    private String CrawlRateChange;

    @Builder
    public CrawlRateEntity(Long id, String CrawlRateName, String CrawlRateInterestRate,
                           String CrawlRateChange) {
        this.id = id;
        this.CrawlRateName = CrawlRateName;
        this.CrawlRateInterestRate = CrawlRateInterestRate;
        this.CrawlRateChange = CrawlRateChange;
    }
}
