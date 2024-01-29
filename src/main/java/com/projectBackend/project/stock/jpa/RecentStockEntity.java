package com.projectBackend.project.stock.jpa;

// RecentStockEntity.java
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "recent_stock")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentStockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recentStockId;

    private String stockDate;
    private String stockCode;
    private String stockOpen;
    private String stockHigh;
    private String stockLow;
    private String stockClose;
    private String stockVolume;
    private String stockTradingValue;
    private String stockFluctuationRate;
    private String stockName;
    private String stockBps;
    private String stockPer;
    private String stockPbr;
    private String stockEps;
    private String stockDiv;
    private String stockDps;
}
