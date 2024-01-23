package com.projectBackend.project.stock.jpa;

import lombok.*;

import javax.persistence.*;
@Entity
@Table(name = "stock")
@Getter
@Setter
@ToString
@Builder
//@NoArgsConstructor
@AllArgsConstructor
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(nullable = false)
    private Long stockId;
    private String stockOpen;
    private String stockHigh;
    private String stockLow;
    private String stockClose;
    private String stockVolume;
    private String stockTradingValue;
    private String stockFluctuationRate;
    private String stockDate;
    private String stockCode;
    private String stockName;
    private String stockBps;
    private String stockPer;
    private String stockPbr;
    private String stockEps;
    private String stockDiv;
    private String stockDps;

    // 기본 생성자
    public StockEntity() {
    }
}

