package com.projectBackend.project.stock.jpa;

// RecentStockEntity.java
import lombok.*;

import javax.persistence.*;
import java.util.Date;
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
    @Column(name = "recentStock_id")
    private Long recentStockId;

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
