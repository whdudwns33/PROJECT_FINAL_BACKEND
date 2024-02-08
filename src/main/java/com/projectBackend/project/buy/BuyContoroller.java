package com.projectBackend.project.buy;

import com.projectBackend.project.utils.MultiDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/buyAndSell")
@RequiredArgsConstructor
public class BuyContoroller {
    private final BuyService buyService;

    // 구매 이력 조회
    @PostMapping("/getInfo")
    public ResponseEntity<MultiDto> getInfo(@RequestBody MultiDto multiDto) {
        return ResponseEntity.ok(buyService.getData(multiDto));
    }


    // 구매
    @PostMapping("/buy")
    public ResponseEntity<Boolean> postBuy(@RequestBody MultiDto multiDto) {
        return ResponseEntity.ok(buyService.buy(multiDto));
    }


    // 판매
    @PostMapping("/sell")
    public ResponseEntity<Boolean> postSell(@RequestBody MultiDto multiDto) {
        return ResponseEntity.ok(buyService.sell(multiDto));
    }
}
