package com.projectBackend.project.utils.configration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Random;

@Setter
@Getter
@ToString
@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsConfig {
    private final DefaultMessageService messageService;
    private String api = "NCSX0MYODTVI4R8T";
    private String secretApi = "U35KK751OXT6FH57ZHJZLNAQITFEMUUX";
    public String authNum;


    @Autowired
    public SmsConfig() {
        this.messageService = NurigoApp.INSTANCE.initialize(api, secretApi, "https://api.coolsms.co.kr");
    }

    // 인증 번호를 문자로 전송
    @GetMapping("/send-mms")
    public SingleMessageSentResponse sendMmsByResourcePath(@RequestParam String tel) throws IOException {
        System.out.println(tel);
        // 인증 번호 생성
        authNum = generateAuthCode();
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력
        message.setFrom("01091881544");
        // 리액트에서 전달 받은 전화 번호
        message.setTo(tel);
        message.setText("인증번호 입니다. " + authNum);

        // 여러 건 메시지 발송일 경우 send many 예제와 동일하게 구성하여 발송할 수 있습니다.
        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return response;
    }

    // 인증번호 동일 여부 체크
    @GetMapping("/check")
    public ResponseEntity<Boolean> isCertificationNumber (@RequestParam String cnum) {
        System.out.println("cnum : " + cnum);
        System.out.println("authNum : " + authNum);
        if (cnum != null) {
            if (authNum.equals(cnum)) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.ok(false);
            }
        }
        else {
            System.out.println("입력 받은 cnum 값이 없습니다.");
            return ResponseEntity.ok(false);
        }
    }

    // 인증 번호 생성.
    // 인증 번호 생성 메서드
    // 추가할 것. 유효 시간
    public String generateAuthCode() {
        // 인증 번호에 사용할 문자열
        String chars = "0123456789";

        // Random 객체 생성
        Random random = new Random();

        // StringBuilder를 사용하여 인증 번호 생성
        StringBuilder authCodeBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(chars.length());
            char randomChar = chars.charAt(index);
            authCodeBuilder.append(randomChar);
        }

        String cn = authCodeBuilder.toString();
        System.out.println("문자인증 인증번호 : " + cn);
        // 생성된 인증 번호 반환
        return cn;
    }
}
