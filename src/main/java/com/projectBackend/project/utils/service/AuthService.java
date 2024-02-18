package com.projectBackend.project.utils.service;

import com.projectBackend.project.member.MemberDto;
import com.projectBackend.project.member.MemberEntity;
import com.projectBackend.project.member.MemberRepository;
import com.projectBackend.project.utils.jwt.TokenProvider;
import com.projectBackend.project.utils.token.TokenDto;
import com.projectBackend.project.utils.token.TokenEntity;
import com.projectBackend.project.utils.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;

import static com.projectBackend.project.utils.service.MailService.EPW;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    // 로그인 회원가입 관리
    // member와 밀접한 관계가 있지만, 보안이 필요가 없거나 토큰을 발급 받는 논리 영역
    // 하지만 컨트롤러는 MemberContoroller 등에서 사용할 예정 (authController -> x)
    private final AuthenticationManagerBuilder managerBuilder; // 인증을 담당하는 클래스
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    // 회원 가입
    public MemberDto signup(MemberDto memberDto) {
        if (memberRepository.existsByMemberEmail(memberDto.getMemberEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }
        MemberEntity member = memberDto.toMemberEntity(passwordEncoder);
        return MemberDto.of(memberRepository.save(member));
    }

    // 로그인 및 토큰 저장, 발행
    public TokenDto login(MemberDto memberDto) {
        try {
            // 인증 코드 생성
            UsernamePasswordAuthenticationToken authenticationToken = memberDto.toAuthentication();
            log.warn("authenticationToken: {}", authenticationToken);
            Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
            log.warn("authentication: {}", authentication);

            // 이메일을 참조하여 user 정보 조회
            String email = memberDto.getMemberEmail();
            Optional<MemberEntity> memberEntity = memberRepository.findByMemberEmail(email);

            if (memberEntity.isPresent()) {
                MemberEntity member = memberEntity.get();
                String authority = String.valueOf(member.getAuthority());

                // 회원 또는 관리자 로그인
                if ("ROLE_USER".equals(authority) || "ROLE_ADMIN".equals(authority)) {
                    TokenDto tokenDto = tokenProvider.generateTokenDto(authority, authentication);
                    String refreshToken = tokenDto.getRefreshToken();
                    tokenDto.setRole(authority);

                    // 토큰 저장
                    TokenEntity token = new TokenEntity();
                    token.setRefreshToken(refreshToken);
                    token.setMember(member);
                    tokenRepository.deleteByMember_Id(member.getId());
                    tokenRepository.save(token);

                    return tokenDto;
                } else {
                    return null; // 유효한 역할이 아닌 경우
                }
            } else {
                return null; // 사용자 정보가 없는 경우
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 예외 발생 시
        }
    }

    // 로그인 상태 체크
    // accessToken을 활용하여 이메일 값을 출력하고 해당 이메일로 리프레쉬 토큰 발급 및 체크
    public String isLogined(String accessToken) {
        try {
            if (accessToken != null) {
                String email = tokenProvider.getUserEmail(accessToken);
                log.info("email info : {}", email);
                Optional<MemberEntity> member = memberRepository.findByMemberEmail(email);
                if (member.isPresent()) {
                    MemberEntity memberEntity = member.get();
                    log.info("member : {}", memberEntity);
                    // 리프레쉬 토큰을 불러오기 위한 id
                    Long id = memberEntity.getId();
                    // 데이터 베이스의 리프레쉬 토큰
                    Optional<TokenEntity> tokenEntity = tokenRepository.findById(id);
                    // 가장 최근의 리프레쉬 데이터
                    if (!tokenEntity.isEmpty()) {
                        TokenEntity token = tokenEntity.get();
                        log.info("token : {}", token);
                        // 불러온 리프레쉬 토큰
                        String refreshToken = token.getRefreshToken();
                        log.info("refreshToken : {}", refreshToken);
                        // 회원의 리프레쉬 토큰 유효성 체크
                        if ( tokenProvider.validateRefreshToken(refreshToken)) {
                            return email;
                        }
                        else {
                            return null;
                        }                    
                    } else {
                        return null;
                    }
                } else {
                    System.out.println("해당 회원 정보가 없습니다.");
                    return null;
                }
            }
            else {
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("로그인 상태가 아닙니다.");
            return null;
        }
    }

    // 카카오 로그인
    public TokenDto kakaoLogin(String email) {
        try {

            System.out.println("kakao login : " + email);
            // 카카오 로그인 => 카카오 이메일 + 랜덤 비밀번호 사용
            // 랜덤 비밀번호를 저장하기 위한 데이터 조회 및 저장
            Optional<MemberEntity> member = memberRepository.findByMemberEmail(email);
            if (member.isPresent()) {
                MemberEntity memberEntity = member.get();

                // 랜덤 비밀번호 생성 및 저장
                String password = generateRandomPassword();
                // 비밀번호 해싱
                String hashedPassword = passwordEncoder.encode(password);
                memberEntity.setMemberPassword(hashedPassword);
                memberRepository.save(memberEntity);

                // 응답 dto
                MemberDto memberDto = MemberDto.builder()
                        .memberEmail(email)
                        .memberPassword(password)
                        .build();
                UsernamePasswordAuthenticationToken authenticationToken = memberDto.toAuthentication();
                log.info("승인 토큰 : {}", authenticationToken);
                Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
                log.info("승인 정보 : {}", authentication);
                TokenDto tokenDto = tokenProvider.generateTokenDto("ROLE_USER",authentication);
                // 카카오 토큰 저장
                if (tokenDto != null) {
                    // 토큰 저장
                    TokenEntity token = new TokenEntity();
                    String refreshToken = tokenDto.getRefreshToken();
                    token.setRefreshToken(refreshToken);
                    token.setMember(memberEntity);
                    tokenRepository.deleteByMember_Id(memberEntity.getId());
                    tokenRepository.save(token);
                    return tokenDto;
                }
                else {
                    return null;
                }
            }
            else {
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // 카카오 로그인 이후 랜덤 패스워드 생성
    // 알파벳, 숫자, 특수문자를 모두 포함한 문자열
    private static final String ALL_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
    public String generateRandomPassword() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder password = new StringBuilder();
        int length = secureRandom.nextInt(5) + 8;

        for (int i = 0; i < length; i++) {
            // ALL_CHARACTERS에서 랜덤으로 문자 선택
            int randomIndex = secureRandom.nextInt(ALL_CHARACTERS.length());
            char randomChar = ALL_CHARACTERS.charAt(randomIndex);
            password.append(randomChar);
        }

        return password.toString();
    }

    // 리프레쉬 토큰 반환
    public String returnRefreshToken (String accessToken) {
        String email = tokenProvider.getUserEmail(accessToken);
        Optional<MemberEntity> memberEntity = memberRepository.findByMemberEmail(email);
        if (memberEntity.isPresent()) {
            MemberEntity member = memberEntity.get();
            Long id = member.getId();
            String refreshToken = tokenRepository.findByMember_Id(id);
            if (refreshToken != null) {
                log.info("refreshToken info : {}", refreshToken);
                return refreshToken;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    // 이메일 중복 체크
    public boolean isEmail (String email) {
        if (email != null) {
            boolean isTrue = memberRepository.existsByMemberEmail(email);
            log.warn("이메일 중복 확인 {} : ", isTrue);
            return isTrue;
        }
        else {
            return false;
        }
    }

    // 닉네임 중복 체크
    public boolean isNickName(String nickName) {
        System.out.println("닉네임 : " + nickName);
        boolean isTrue = memberRepository.existsByMemberNickName(nickName);
        log.warn("닉네임 중복 확인 {} : ", isTrue);
        return isTrue;
    }


    // accessToken 재발급을 위해 리프레쉬 토큰에서 권한 정보 추출
    public String createAccessToken(String refreshToken) {
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        return tokenProvider.generateAccessToken(authentication);
    }

    // 비밀번호 찾기
    public String findPassword(MemberDto memberDto) {
        String email = memberDto.getMemberEmail();
        log.info("find password email : {}", email);
        Optional<MemberEntity> memberEntity = memberRepository.findByMemberEmail(email);
        if (memberEntity.isPresent()) {
            MemberEntity member = memberEntity.get();
            String password = generateRandomPassword();
            member.setMemberPassword(passwordEncoder.encode(password));
            return password;
        }
        else {
            return null;
        }
    }
}
