package com.projectBackend.project.crawling.crawlStock;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "crawlStock")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CrawlStockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String stockName;
    private String stockPrice;
    private String stockUpDown;
    private String stockRate;

    @Builder
    public CrawlStockEntity(String stockName, String stockPrice, String stockUpDown, String stockRate) {
        this.stockName = stockName;
        this.stockPrice = stockPrice;
        this.stockUpDown = stockUpDown;
        this.stockRate = stockRate;
    }
}
