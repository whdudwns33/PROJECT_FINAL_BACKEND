package com.projectBackend.project.buy;

import com.projectBackend.project.utils.MultiDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/buyAndSell")
@RequiredArgsConstructor
@Api(tags = "Buy Controller", description = "주식 매도/ 매수 관련 API")
public class BuyContoroller {
    private final BuyService buyService;

    // 구매 이력 조회
    @PostMapping("/getInfo")
    @ApiOperation(value = "주식 매수 이력 조회", notes = "사용자의 주식 매수 이력을 조회하는 API")
    public ResponseEntity<MultiDto> getInfo(@RequestBody MultiDto multiDto) {
        return ResponseEntity.ok(buyService.getData(multiDto));
    }


    // 구매
    @PostMapping("/buy")
    @ApiOperation(value = "매수", notes = "사용자의 주식 매수 API")
    public ResponseEntity<Boolean> postBuy(@RequestBody MultiDto multiDto) {
        return ResponseEntity.ok(buyService.buy(multiDto));
    }


    // 판매
    @PostMapping("/sell")
    @ApiOperation(value = "매도", notes = "사용자의 주식 매도 API")
    public ResponseEntity<Boolean> postSell(@RequestBody MultiDto multiDto) {
        return ResponseEntity.ok(buyService.sell(multiDto));
    }
}
