package com.projectBackend.project.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
public class StockChartReqDto {
    private String stockName;
    private int months;
    private String columnType;
    private int futureDays;
}
