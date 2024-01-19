package com.projectBackend.project.member;


import com.projectBackend.project.utils.service.AuthService;
import com.projectBackend.project.utils.token.TokenDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Setter
@Getter
@ToString
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberContoroller {
    // MemberContoroller는 authService와 memberService 사용
    private final AuthService authService;
    private final MemberService memberService;

    // 회원 가입
    @PostMapping("/sign")
    public ResponseEntity<MemberDto> sign(@RequestBody MemberDto memberDto) {
        return ResponseEntity.ok(authService.signup(memberDto));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberDto memberDto) {
        return ResponseEntity.ok(authService.login(memberDto));
    }

    //카카오 로그인
    @GetMapping("/kakao/login")
    public ResponseEntity<TokenDto> kakaoLogin(@RequestParam String email) {
        return ResponseEntity.ok(authService.kakaoLogin(email));
    }

    // 회원 리프레쉬 토큰 조회
    @GetMapping("/getRefresh")
    private ResponseEntity<String> getRefreshToken(@RequestParam String accessToken) {
        return ResponseEntity.ok(authService.returnRefreshToken(accessToken));
    }

    // accessToken 재발급
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestBody String refreshToken) {
        log.info("refreshToken: {}", refreshToken);
        return ResponseEntity.ok(authService.createAccessToken(refreshToken));
    }


}
