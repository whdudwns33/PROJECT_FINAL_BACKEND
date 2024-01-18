package com.projectBackend.project.utils.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.projectBackend.project.utils.Common.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KakaoService {

    // 카카오 로그인 토큰 발급
    public String kakaoToken (String code) {
        try {
            //카카오로 post 방식 요청
            RestTemplate rt = new RestTemplate();

            //HttpHeader 오브젝트 생성
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            //HttpBody 오브젝트 생성
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", clientId);
            params.add("redirect_uri", redirectUri);
            params.add("client_secret", clientSecret);
            params.add("code", code);

            //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                    new HttpEntity<>(params, headers);

            //Http 요청하기 - Post방식으로 그리고 response 변수의 응답받음.
            ResponseEntity<String> response = rt.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );
            log.info("response {} : ", response.getBody());
            System.out.println("status" + response.getStatusCodeValue());
            if (response.getStatusCodeValue() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                // JSON 문자열을 JsonNode로 변환 및 토큰 정보 출력
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                String accessToken = jsonNode.get("access_token").asText();
                log.info("Access Token: {}", accessToken);
                // 발급 받은 엑세스 토큰으로 이메일 데이터 출력
                String kakaoEmailData = kakaoEmail(accessToken);
                log.info("kakao email : {}", kakaoEmailData);
                return kakaoEmailData;
            }
            else {
                return null;
            }
        }
        catch (Exception e) {
            System.out.println("post 응답이 되지 않음");
            e.printStackTrace();
            return null;
        }
    }


    // 카카오 이메일 받아오기
    public String kakaoEmail(String accessToken) {
        try {
            RestTemplate rt = new RestTemplate();
            ObjectMapper objectMapper = new ObjectMapper();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity<String> requestEntity = new HttpEntity<>("request body content", headers);

            ResponseEntity<String> response = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    requestEntity,
                    String.class,
                    headers
            );

            // 추가: 응답 내용 로깅
            log.info("Get RESPONSE : {}", response);

            if (response.getStatusCodeValue() == 200) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                JsonNode kakaoAccountNode = jsonNode.path("kakao_account");
                String email = kakaoAccountNode.path("email").asText();
                if (email != null) {
                    log.info("Received email: {}", email);
                    return email;
                } else {
                    log.warn("카카오 이메일이 전송되지 않음");
                    return null;
                }
            } else {
                // 추가: 실패한 경우 응답 내용 로깅
                log.error("카카오 유저 연결이 되지 않음. 응답: {}", response);
                return null;
            }
        } catch (Exception e) {
            // 추가: 예외가 발생한 경우 스택 트레이스 출력
            log.error("카카오 이메일 요청 중 예외 발생", e);
            return null;
        }
    }




}
