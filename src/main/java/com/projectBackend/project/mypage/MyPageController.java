package com.projectBackend.project.mypage;

import com.projectBackend.project.utils.MultiDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "MyPage Controller", description = "마이 페이지 관련 API")
public class MyPageController {
    private final MyPageService myPageService;

    @PostMapping("/getData")
    @ApiOperation(value = "회원 정보 및 구매 이력 가져오기", notes = "회원 정보 및 구매 이력 가져오기 API")
    public ResponseEntity<MultiDto> getData(@RequestBody MultiDto multiDto) {
        return ResponseEntity.ok(myPageService.getMemberAndBuy(multiDto));
    }

    // 금액 충전
    @PostMapping("/savePoint")
    @ApiOperation(value = "금액 충전", notes = "금액 충전 API")
    public ResponseEntity<Boolean> savePoint(@RequestBody MultiDto multiDto) {
        return ResponseEntity.ok(myPageService.savePointMethod(multiDto));
    }
}
