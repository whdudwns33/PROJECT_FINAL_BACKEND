package com.projectBackend.project.crawling.crawlSearch;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity
@Table(name = "crawlSearch")
@NoArgsConstructor
public class CrawlSearchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String searchRank;
    private String searchName;
    private String searchCount;
    private String searchUpdown;
    private String searchPrice;
    private String searchChangeRate;
    private String searchMarketCap;

    @Builder
    public CrawlSearchEntity(Long id, String searchRank, String searchName,
                             String searchCount, String searchUpdown,
                             String searchPrice, String searchChangeRate,
                             String searchMarketCap) {
        this.id = id;
        this.searchRank = searchRank;
        this.searchName = searchName;
        this.searchCount = searchCount;
        this.searchUpdown = searchUpdown;
        this.searchPrice = searchPrice;
        this.searchChangeRate = searchChangeRate;
        this.searchMarketCap = searchMarketCap;
    }
}
