package com.projectBackend.project.stock;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
@Entity
@Table(name = "stock")
@Getter
@Setter
@ToString
@Builder
// dataStock
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
}
