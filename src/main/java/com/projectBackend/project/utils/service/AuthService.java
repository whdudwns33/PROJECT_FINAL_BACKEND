package com.projectBackend.project.utils.service;


import com.projectBackend.project.dto.UserReqDto;
import com.projectBackend.project.dto.UserResDto;
import com.projectBackend.project.dto.TokenDto;
import com.projectBackend.project.entity.Member;
import com.projectBackend.project.entity.Music;
import com.projectBackend.project.entity.Token;
import com.projectBackend.project.utils.jwt.TokenProvider;
import com.projectBackend.project.repository.TokenRepository;
import com.projectBackend.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder; // 인증을 담당하는 클래스
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    // 회원 가입
    public UserResDto signup(UserReqDto requestDto) {
        if (userRepository.existsByUserEmail(requestDto.getUserEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }
        Member member = requestDto.toEntity(passwordEncoder);
        return UserResDto.of(userRepository.save(member));
    }

    // 로그인 및 토큰 저장, 발행
    public TokenDto login(UserReqDto requestDto) {
        try {
            System.out.println("requestDto 이메일 :" + requestDto.getUserEmail());
            System.out.println("requestDto 패스워드 :" + requestDto.getUserPassword());
            // 인증 코드 생성
            UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
            log.warn("authenticationToken: {}", authenticationToken);

            Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
            log.warn("authentication: {}", authentication);

            // 이메일을 참조하여 user 정보 조회
            String email = requestDto.getUserEmail();
            Optional<Member> userEntity = userRepository.findByUserEmail(email);
            System.out.println("로그인 userEntity : " + userEntity);
            if (userEntity.isPresent()) {
                // userEntity 객체의 정보를 데이터 베이스 객체로 생성
                Member user = userEntity.get();
                System.out.println("로그인 user : " + user);
                // 토큰 발급
                TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
                String refreshToken = tokenDto.getRefreshToken();
                // 토큰 저장
                Token token = new Token();
                token.setRefreshToken(refreshToken);
                token.setMember(user);
                tokenRepository.deleteByMember_Id(user.getId());
                tokenRepository.save(token);
                return tokenDto;
            }
            else {
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 로그인 상태 확인
    // 이메일을 참조하여 해당
    // 수정 : 이메일 => 토큰
    public String isLogined(String accessToken) {
        try {
            if (accessToken != null) {
                String email = tokenProvider.getUserEmail(accessToken);
                log.info("email info : {}", email);
                Optional<Member> member = userRepository.findByUserEmail(email);
                if (member.isPresent()) {
                    Member user = member.get();
                    System.out.println("로그인 정보 : " + user);
                    // 리프레쉬 토큰을 불러오기 위한 id
                    Long id = user.getId();
                    // 데이터 베이스의 리프레쉬 토큰
                    List<Token> tokens = tokenRepository.findAllByMemberId(id);
                    System.out.println("토큰 tokens : " + tokens);
                    // 가장 최근의 리프레쉬 데이터
                    if (!tokens.isEmpty()) {
                        Token token = tokens.get(tokens.size() - 1);
                        log.info("token : {}", token);
                        // 불러온 리프레쉬 토큰
                        String refreshToken = token.getRefreshToken();
                        log.info("refreshToken : {}", refreshToken);
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

    // 관리자 로그인
    public TokenDto admin (UserReqDto userReqDto) {
        String email = userReqDto.getUserEmail();

        if (email.equals("adminlogin123@admin.com")) {
        Optional<Member> userEntity = userRepository.findByUserEmail(email);


            if (userEntity.isPresent()) {
                // userEntity 객체의 정보를 데이터 베이스 객체로 생성
                Member user = userEntity.get();
                UsernamePasswordAuthenticationToken authenticationToken = userReqDto.toAuthentication();
                Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
                TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
                tokenDto.setRole("admin");
                String refreshToken = tokenDto.getRefreshToken();
                // 토큰 저장
                Token token = new Token();
                token.setRefreshToken(refreshToken);
                token.setMember(user);
                tokenRepository.deleteByMember_Id(user.getId());
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

    // 관리자 체크
    public boolean isAdmin (String token) {
        String email = tokenProvider.getUserEmail(token);
        Optional<Member> member = userRepository.findByUserEmail(email);
        if(member.isPresent()) {
            Member user = member.get();
            String role = String.valueOf(user.getAuthority());
            log.warn("Authority : {}", user.getAuthority());
            if (role.equals("ROLE_ADMIN")) {
                log.info("어드민 맞아용");
                return true;
            }
            else return false;
        }
        else return false;
    }


    // 카카오 로그인 => 카카오 토큰이 존재하지만, 사용하지 않을 생각
    public TokenDto kakaoLogin(String email) {
        try {

            System.out.println("kakao login : " + email);
            // 카카오 로그인 => 카카오 이메일 + 랜덤 비밀번호 사용
            // 랜덤 비밀번호를 저장하기 위한 데이터 조회 및 저장
            Optional<Member> member = userRepository.findByUserEmail(email);
            if (member.isPresent()) {
                Member user = member.get();
                System.out.println("카카오 로그인 회원 : " + user);

                // 랜덤 비밀번호 생성 및 저장
                String password = generateRandomPassword();
                // 비밀번호 해싱
                String hashedPassword = passwordEncoder.encode(password);
                user.setUserPassword(hashedPassword);
                userRepository.save(user);
                // 응답 dto
                UserReqDto userReqDto = new UserReqDto();
                userReqDto.setUserEmail(email);
                userReqDto.setUserPassword(password);
                UsernamePasswordAuthenticationToken authenticationToken = userReqDto.toAuthentication();
                log.info("승인 토큰 : {}", authenticationToken);
                Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
                log.info("승인 정보 : {}", authentication);
                TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
                // 카카오 토큰 저장
                if (tokenDto != null) {
                    // 토큰 저장
                    Token token = new Token();
                    String refreshToken = tokenDto.getRefreshToken();
                    System.out.println("카카오 리프레쉬 토큰 : " + refreshToken);
                    token.setRefreshToken(refreshToken);
                    token.setMember(user);
                    tokenRepository.deleteByMember_Id(user.getId());
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
    public String generateRandomPassword() {
        UUID uuid = UUID.randomUUID();

        // UUID를 문자열로 변환하고 '-' 제거
        String uuidAsString = uuid.toString().replaceAll("-", "");

        // 앞에서부터 10자리만 선택하여 랜덤 비밀번호로 사용
        String randomPassword = uuidAsString.substring(0, 10);

        return randomPassword;
    }

    // 이메일 중복 체크
    public boolean isEmail (String email) {
        if (email != null) {
            boolean isTrue = userRepository.existsByUserEmail(email);
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
        boolean isTrue = userRepository.existsByUserNickname(nickName);
        log.warn("닉네임 중복 확인 {} : ", isTrue);
        return isTrue;
    }

    // 닉네임으로 이메일을 찾아서 반환
    public String getEmail(String nickname) {
        if (nickname != null) {
            Optional<Member> member = userRepository.findByUserNickname(nickname);
            if (member.isPresent()) {
                Member user = member.get();
                return user.getUserEmail();
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    // 비밀번호 변경
    public boolean changePassword(UserReqDto userReqDto) {
        try {
            Optional<Member> member = userRepository.findByUserNickname(userReqDto.getUserNickname());
            if (member.isPresent()) {
                Member user = member.get();
                user.setUserPassword(passwordEncoder.encode(userReqDto.getUserPassword()));
                userRepository.save(user);
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // accessToken 재발급을 위해 리프레쉬 토큰에서 권한 정보 추출
    public String createAccessToken(String refreshToken) {
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        return tokenProvider.generateAccessToken(authentication);
    }


    // 길종환
    public UserResDto getUserInfo(String token) {
        String email = tokenProvider.getUserEmail(token);
        Optional<Member> userEntity = userRepository.findByUserEmail(email);

        if (userEntity.isPresent()) {
            Member user = userEntity.get();
            return UserResDto.of(user);
        } else {
            return null;
        }
    }

    public Member getUserByEmail(String email) {
        Optional<Member> userEntity = userRepository.findByUserEmail(email);
        return userEntity.orElse(null);
    }
    // 유저 포인트 증가
    public boolean increasePoints(String userEmail, int amount) {
        try {
            Member member = userRepository.findByUserEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("해당 이메일의 사용자를 찾을 수 없습니다."));
            member.setUserPoint(member.getUserPoint() + amount);
            userRepository.save(member);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    // 유저 포인트 감소
    public boolean decreasePoints(String userEmail, int amount) {
        try {
            Member member = userRepository.findByUserEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("해당 이메일의 사용자를 찾을 수 없습니다."));
            if (member.getUserPoint() < amount) {
                throw new RuntimeException("포인트가 부족합니다.");
            }
            member.setUserPoint(member.getUserPoint() - amount);
            userRepository.save(member);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    // 장현준, 전체유저리스트
    public List<Member> getUserList() {
        return userRepository.findAll();
    }



}
