package com.projectBackend.project.member;

import com.projectBackend.project.constant.Authority;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Getter
@Setter
public class MemberDto {
    String memberEmail;
    String memberPassword;
    String phone;
    String nickName;
    int point;
    Date birth;
    String cnum;
    @Enumerated(EnumType.STRING)
    private Authority authority;

    LocalDateTime registrationDate;

    // Dto를 entity로
    public MemberEntity toMemberEntity (PasswordEncoder passwordEncoder) {
        return MemberEntity.builder()
                .memberEmail(memberEmail)
                .memberPassword(passwordEncoder.encode(memberPassword))
                .phone(phone)
                .nickName(nickName)
                .birth(birth)
                .authority(Authority.ROLE_USER)
                .point(point)
                .build();
    }

    // entity를 dto로
    public static MemberDto of(MemberEntity member) {
        return MemberDto.builder()
                .memberEmail(member.getMemberEmail())
                .phone(member.getPhone())
                .nickName(member.getMemberNickName())
                .birth(member.getBirth())
                .authority(member.getAuthority())
                .registrationDate(member.getRegistrationDate())
                .point(member.getPoint())
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberEmail, memberPassword);
        return authenticationToken;
    }

}
