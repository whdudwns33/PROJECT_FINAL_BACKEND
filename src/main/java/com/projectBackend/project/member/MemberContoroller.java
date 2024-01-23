package com.projectBackend.project.member;


import com.projectBackend.project.utils.service.AuthService;
import com.projectBackend.project.utils.service.MailService;
import com.projectBackend.project.utils.token.TokenDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.projectBackend.project.utils.service.MailService.EPW;

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
    private final MailService mailService;

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

    // 회원 가입 시 이메일 인증 + 중복 체크
    @PostMapping("/mailConfirm")
    public ResponseEntity<Boolean> mailConfirm(@RequestParam String email) throws Exception {
        boolean isTrue = mailService.sendSimpleMessage(email);
        return ResponseEntity.ok(isTrue);
    }

    // 인증 번호 체크, post 방식만 허용하는 듯.
    @GetMapping("/ePw")
    public ResponseEntity<Boolean> checkEpw(@RequestParam String epw) {
        System.out.println("프론트에서 날린 epw" + epw);
        if (epw.equals(EPW)) {
            return ResponseEntity.ok(true);
        }
        else {
            return ResponseEntity.ok(false);
        }
    }

    // 닉네임 중복 체크
    @GetMapping("/nickName")
    public ResponseEntity<Boolean> nickName(@RequestParam String nickName) {
        return ResponseEntity.ok(authService.isNickName(nickName));
    }


}
