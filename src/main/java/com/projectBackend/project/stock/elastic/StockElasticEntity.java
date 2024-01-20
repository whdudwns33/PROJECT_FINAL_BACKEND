package com.projectBackend.project.stock.elastic;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;

@Getter
@Setter
@Document(indexName = "stock_index")
public class StockElasticEntity {

    @Id
    private String id;
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