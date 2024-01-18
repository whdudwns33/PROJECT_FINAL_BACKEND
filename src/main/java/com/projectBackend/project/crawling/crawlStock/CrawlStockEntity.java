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
    private String StockName;
    private String StockPrice;
    private String StockUpDown;
    private String StockRate;

    @Builder
    public CrawlStockEntity(String StockName,String StockPrice,String StockUpDown,String StockRate ) {
        this.StockName = StockName;
        this.StockPrice = StockPrice;
        this.StockUpDown = StockUpDown;
        this.StockRate = StockRate;
    }
}
