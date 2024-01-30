package com.projectBackend.project.stock.elastic;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@Document(indexName = "stock_index")
public class StockElasticEntity {

    @Id
    private String id;
    private Long stockOpen;
    private Long stockHigh;
    private Long stockLow;
    private Long stockClose;
    private Long stockVolume;
    private Long stockTradingValue;
    private Double stockFluctuationRate;

    @Temporal(TemporalType.DATE)
    private Date stockDate;
    private String stockCode;
    private String stockName;
    private Double stockBps;
    private Double stockPer;
    private Double stockPbr;
    private Double stockEps;
    private Double stockDiv;
    private Double stockDps;

    public void generateId() {
        this.id = this.stockCode + "_" + this.stockDate.getTime();
    }
}