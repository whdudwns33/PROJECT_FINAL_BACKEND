package com.projectBackend.project.member;

import com.projectBackend.project.constant.Authority;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Builder
@Getter
@Setter
public class MemberDto {
    String memberEmail;
    String memberPassword;
    String phone;
    String nickName;
    Date birth;
    @Enumerated(EnumType.STRING)
    private Authority authority;

    // Dto를 entity로
    public MemberEntity toMemberEntity (PasswordEncoder passwordEncoder) {
        return MemberEntity.builder()
                .memberEmail(memberEmail)
                .memberPassword(passwordEncoder.encode(memberPassword))
                .phone(phone)
                .nickName(nickName)
                .birth(birth)
                .authority(authority)
                .build();
    }

    // entity를 dto로
    public static MemberDto of(MemberEntity member) {
        return MemberDto.builder()
                .memberEmail(member.getMemberEmail())
                .phone(member.getPhone())
                .nickName(member.getNickName())
                .birth(member.getBirth())
                .authority(member.getAuthority())
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberEmail, memberPassword);
        return authenticationToken;
    }

}
