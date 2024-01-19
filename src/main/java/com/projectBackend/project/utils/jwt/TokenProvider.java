package com.projectBackend.project.utils.jwt;

import com.projectBackend.project.constant.Authority;
import com.projectBackend.project.utils.token.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY ="auth";
    private static final String BEARER_TYPE = "Bearer"; // 토큰의 타입
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 10 * 60; // 임시 1분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 임시 1시간
    private final Key key; // 토큰을 서명(signiture)하기 위한 Key

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // HS512 알고리즘을 사용하는 키 생성
    }

    // 토큰 생성
    public TokenDto generateTokenDto(String authoruty , Authentication authentication) {
        if (authoruty.equals("ROLE_USER")) {
            log.warn("authentication : {} ", authentication);
            // 권한 정보 문자열 생성,
            String authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            long now = (new Date()).getTime(); // 현재 시간
            // 토큰 만료 시간 설정
            Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
            Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

            // 토큰 생성
            String accessToken = io.jsonwebtoken.Jwts.builder()
                    .setSubject(authentication.getName())
                    .claim(AUTHORITIES_KEY, authorities)
                    .setExpiration(accessTokenExpiresIn)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();

            // 리프레시 토큰 생성
            String refreshToken = io.jsonwebtoken.Jwts.builder()
                    .setExpiration(refreshTokenExpiresIn)
                    .setSubject(authentication.getName())
                    .claim(AUTHORITIES_KEY, authorities)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
            log.warn("accessToken {} : ", accessToken);
            log.warn("refreshToken {} : ", refreshToken);
            // 토큰 정보를 담은 TokenDto 객체 생성
            return TokenDto.builder()
                    .grantType(BEARER_TYPE)
                    .accessToken(accessToken)
                    .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                    .refreshToken(refreshToken)
                    .refreshTokenExpiresIn(refreshTokenExpiresIn.getTime())
                    .role("ROLE_USER")
                    .build();
        } else if (authoruty.equals("ROLE_ADMIN")) {
            log.warn("authentication {} : ", authentication);
            // 권한 정보 문자열 생성,
            String authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            long now = (new Date()).getTime(); // 현재 시간
            // 토큰 만료 시간 설정
            Date accessTokenExpiresIn = new Date(now + 1000 * 60 * 60 * 60);
            Date refreshTokenExpiresIn = new Date(now + 1000 * 60 * 60 * 60);

            // 토큰 생성
            String accessToken = io.jsonwebtoken.Jwts.builder()
                    .setSubject(authentication.getName())
                    .claim(AUTHORITIES_KEY, authorities)
                    .setExpiration(accessTokenExpiresIn)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();

            // 리프레시 토큰 생성
            String refreshToken = io.jsonwebtoken.Jwts.builder()
                    .setExpiration(refreshTokenExpiresIn)
                    .setSubject(authentication.getName())
                    .claim(AUTHORITIES_KEY, authorities)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
            log.warn("accessToken {} : ", accessToken);
            log.warn("refreshToken {} : ", refreshToken);
            // 토큰 정보를 담은 TokenDto 객체 생성
            return TokenDto.builder()
                    .grantType(BEARER_TYPE)
                    .accessToken(accessToken)
                    .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                    .refreshToken(refreshToken)
                    .refreshTokenExpiresIn(refreshTokenExpiresIn.getTime())
                    .role("ROLE_ADMIN")
                    .build();
        }
        else {
            return null;
        }
    }


    // 토큰 복호화
    public Claims parseClaims(String accessToken) {
        try {
            return io.jsonwebtoken.Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // 액세스 토큰을 사용하여 Spring Security의 Authentication 객체를 생성
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        // 토큰의 claim 부분에서 권한 정보를 체크. 만약 권한이 없다면 null 반환
        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰");
        }
        // UserDetails 객체를 생성할 때 사용되며, 사용자의 권한에 따라 인증 및 권한 부여를 수행하는 데 활용.
        // 토큰에 담긴 권한 정보들을 가져옴
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        // 권한 정보들을 이용해 유저 객체를 만들어서 반환, 여기서 User 객체는 UserDetails 인터페이스를 구현한 객체
        User principal = new User(claims.getSubject(), "", authorities);
        log.warn("principal {} :", principal);
        // 유저 객체, 토큰, 권한 정보들을 이용해 인증 객체를 생성해서 반환
        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    // 토큰의 유효성 검사
    public boolean validateToken(String token) {
        try {
            io.jsonwebtoken.Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | io.jsonwebtoken.MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    // refresh 토큰의 유효성 검사
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Claims refreshTokenClaims = parseClaims(refreshToken);

            // refreshToken이 유효한지 확인
            if (refreshTokenClaims.get(AUTHORITIES_KEY) != null) {
                System.out.println("리프레쉬 토큰은 유효");
                // 추가적인 유효성 검사를 수행할 수 있습니다.
                // 유효한 경우 true 반환
                return true;
            } else {
                System.out.println("리프레쉬 토큰은 유효하지 않음");
                // 권한 정보가 없는 경우 또는 다른 유효성 검사 실패 시 false 반환
                return false;
            }
        } catch (ExpiredJwtException e) {
            // refreshToken이 만료된 경우
            log.info("만료된 refresh 토큰입니다.");
        } catch (SecurityException | io.jsonwebtoken.MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }

        // 예외가 발생하거나 유효성 검사에 실패한 경우 false 반환
        return false;
    }


    // access 토큰 재발급
    public String generateAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime(); // 현재 시간
        // 토큰 만료 시간 설정
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        // 토큰 생성
        return io.jsonwebtoken.Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // 토큰으로 이메일 정보 출력
    public String getUserEmail(String token) {
        try {
            Claims claims = io.jsonwebtoken.Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            // 'sub' 키에 해당하는 값을 추출
            String email = (String) claims.get("sub");
            System.out.println(email);
            return email;
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우 예외 처리
            // 이 부분은 필요에 따라 처리하십시오.
            return null;
        }
    }
}
