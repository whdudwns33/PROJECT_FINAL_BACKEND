package com.projectBackend.project.buy;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class BuyDto {
    // 구매 가격
    private int buyPrice;

    // 구매 수량
    private int buyCount;

    // 총 수량
    private int sellCount;

    // 총 매도 가격
    private int sellPrice;

    // 날짜
    private LocalDate date;

    public static BuyDto of(BuyEntity buyEntity) {
        return BuyDto.builder()
                .buyPrice(buyEntity.getBuyPrice())
                .buyCount(buyEntity.getBuyCount())
                .date(buyEntity.getDate())
                .build();
    }

    public BuyEntity toEntity(BuyDto dto) {
        return BuyEntity.builder()
                .buyPrice(dto.getBuyPrice())
                .buyCount(dto.getBuyCount())
                .build();
    }
}
