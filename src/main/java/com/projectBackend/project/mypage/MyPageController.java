package com.projectBackend.project.mypage;

import com.projectBackend.project.utils.MultiDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Setter
@Getter
@ToString
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {
    private final MyPageService myPageService;

    @PostMapping("/getData")
    public ResponseEntity<MultiDto> getData(@RequestBody MultiDto multiDto) {
        return ResponseEntity.ok(myPageService.getMemberAndBuy(multiDto));
    }

    // 금액 충전
    @PostMapping("/savePoint")
    public ResponseEntity<Boolean> savePoint(@RequestBody MultiDto multiDto) {
        return ResponseEntity.ok(myPageService.savePointMethod(multiDto));
    }
}
