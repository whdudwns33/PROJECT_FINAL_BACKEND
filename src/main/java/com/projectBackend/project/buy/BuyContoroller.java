package com.projectBackend.project.buy;

import com.projectBackend.project.utils.MultiDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/buyAndSell")
@RequiredArgsConstructor
public class BuyContoroller {
    private final BuyService buyService;

    @PostMapping("/buy")
    public ResponseEntity<Boolean> postBuy(@RequestBody MultiDto multiDto) {
        return ResponseEntity.ok(buyService.buy(multiDto));
    }
}
