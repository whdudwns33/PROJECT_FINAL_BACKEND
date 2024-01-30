package com.projectBackend.project.stock;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// @JsonProperty 애노테이션은
// Jackson 라이브러리에서 사용되며, 객체를 JSON으로 직렬화(serialize)하거나
// JSON을 객체로 역직렬화(deserialize)할 때 필드의 이름을 지정하는데 사용됩니다.

// 여러분의 코드에서 @JsonProperty는 클래스의 각 필드에 사용되었습니다.
// 이 애노테이션은 해당 필드가 객체를 JSON으로 변환할 때 사용될 때의 키 값을 나타냅니다.
// 각 필드에 대한 JSON의 키를 명시적으로 지정하고자 할 때 사용됩니다.

// 예를 들어, "시가"라는 필드가 있을 때, 이 필드가 JSON으로 변환될 때 키를 "open"이 아니라
// "시가"로 하고 싶다면 @JsonProperty("시가")를 사용합니다.
// 이것은 주로 JSON과 Java 객체 간의 이름 차이로 발생하는 문제를 해결하는 데 사용됩니다.


// 따라서 @JsonProperty를 사용함으로써 Jackson은 해당 필드를 JSON으로 직렬화할 때,
// 혹은 '*** JSON에서 객체로 역직렬화할 때 사용되는 키의 이름을 명시적으로 설정 ***'  할 수 있습니다.

@Slf4j
@Getter
@Setter
@ToString
public class StockDto {
    @JsonProperty("시가")
    private Long open;

    @JsonProperty("고가")
    private Long high;

    @JsonProperty("저가")
    private Long low;

    @JsonProperty("종가")
    private Long close;

    @JsonProperty("거래량")
    private Long volume;

    @JsonProperty("거래대금")
    private Long tradingValue;

    @JsonProperty("등락률")
    private Double fluctuationRate;

    @JsonProperty("날짜")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private Date date;

    @JsonProperty("종목코드")
    private String stockCode;

    @JsonProperty("종목명")
    private String stockName;

    @JsonProperty("BPS")
    private Double bps;

    @JsonProperty("PER")
    private Double per;

    @JsonProperty("PBR")
    private Double pbr;

    @JsonProperty("EPS")
    private Double eps;

    @JsonProperty("DIV")
    private Double div;

    @JsonProperty("DPS")
    private Double dps;
}
