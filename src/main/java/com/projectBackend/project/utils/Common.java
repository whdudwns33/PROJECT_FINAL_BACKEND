package com.projectBackend.project.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Component
public class Common {
    // 파이썬 url
    public static String python = "http://localhost:5000/python";
    // 엘라스틱
    public static String elastic = "localhost:9200";
    // sms
    public static String api = "NCSX0MYODTVI4R8T";
    public static String secretApi = "U35KK751OXT6FH57ZHJZLNAQITFEMUUX";

    // 카카오 토큰 발급을 요청하는 url
    public static String kakaoTokenUrl = "https://kauth.kakao.com/oauth/token";
    public static String clientId = "a42a4db55c114cff5770a883fc8607f9";
    public static String redirectUri = "http://localhost:3000/kakao";
    public static String clientSecret = "Xs7FwH1FUNOkspaOszcuw2wZXTQGrEIs";

    // 날짜
    // 현재 날짜
    public static LocalDate localDate = LocalDate.now();
    // localDate를 문자열로 변환하여 사용할 수 있도록 수정. YYYYMMDD 형식
    public static String date = localDate.format(DateTimeFormatter.BASIC_ISO_DATE);


}
