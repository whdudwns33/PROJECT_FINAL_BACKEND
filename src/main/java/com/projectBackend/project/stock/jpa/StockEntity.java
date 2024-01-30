package com.projectBackend.project.stock.jpa;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "stock")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    @Temporal(TemporalType.DATE)
    private Date stockDate;
    private String stockCode;
    private Long stockOpen;
    private Long stockHigh;
    private Long stockLow;
    private Long stockClose;
    private Long stockVolume;
    private Long stockTradingValue;
    private Double stockFluctuationRate;
    private String stockName;
    private Double stockBps;
    private Double stockPer;
    private Double stockPbr;
    private Double stockEps;
    private Double stockDiv;
    private Double stockDps;

}

