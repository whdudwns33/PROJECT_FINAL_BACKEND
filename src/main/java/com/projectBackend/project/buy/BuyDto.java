package com.projectBackend.project.buy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyDto {
    // 구매 가격
    int buyPrice;
    // 구매 수량
    int buyCount;
}
