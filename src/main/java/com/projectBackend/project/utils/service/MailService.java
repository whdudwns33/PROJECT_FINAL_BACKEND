package com.projectBackend.project.utils.service;

import com.projectBackend.project.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MailService {
    private final JavaMailSender emailsender;
    public static String EPW;
    private final MemberRepository memberRepository;
    private String ePw; // 인증번호

    // 메일 내용 작성
    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException, javax.mail.MessagingException {
		System.out.println("보내는 대상 : " + to);
		System.out.println("인증 번호 : " + ePw);

        MimeMessage message = emailsender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to);// 보내는 대상
        message.setSubject("GoodJob 회원가입 이메일 인증");// 제목

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<br>";
        msgg += "<h1>cord8<h1>";
        msgg += "<br>";
        msgg += "<h3>아래 코드를 회원가입 창으로 돌아가 입력해주세요<h1>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += ePw + "</strong><div><br/> "; // 메일에 인증번호 넣기
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");// 내용, charset 타입, subtype
        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom(new InternetAddress("jojo6807@naver.com", "Admin"));// 보내는 사람

        return message;
    }

    // 랜덤 인증 코드 전송
    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤, rnd 값에 따라서 아래 switch 문이 실행됨

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    // a~z (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    // A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }

    // 메일 발송
    // sendSimpleMessage 의 매개변수로 들어온 to 는 곧 이메일 주소가 되고,
    // MimeMessage 객체 안에 내가 전송할 메일의 내용을 담는다.
    // 그리고 bean 으로 등록해둔 javaMail 객체를 사용해서 이메일 send!!
    public boolean sendSimpleMessage(String to) throws Exception {
        // to는 email로 중복 체크를 한다.
        boolean isTrue = memberRepository.existsByMemberEmail(to);
        System.out.println("이메일 중복 체크 : " + isTrue);
        if (!isTrue) {
            ePw = createKey(); // 랜덤 인증번호 생성
            System.out.println("ePw : " + ePw);
            MimeMessage message = createMessage(to); // 메일 발송
            try {
                emailsender.send(message);
                EPW = ePw;
                System.out.println("EPW : " + EPW);
            } catch (MailException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to send email. Please check your email configuration and try again.");
            }
            return true;
        }
        else {
            return false;
        }
    }

    // 비밀번호 찾기
    public boolean findEmail(String to) throws Exception {
        // to는 email로 중복 체크를 한다.
        boolean isTrue = memberRepository.existsByMemberEmail(to);
        if (isTrue) {
            ePw = createKey(); // 랜덤 인증번호 생성
            MimeMessage message = createMessage(to); // 메일 발송
            try {
                emailsender.send(message);
                EPW = ePw;
            } catch (MailException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to send email. Please check your email configuration and try again.");
            }
            return true;
        } else {
            return false;
        }
    }
}
