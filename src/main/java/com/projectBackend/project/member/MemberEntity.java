package com.projectBackend.project.member;

import com.projectBackend.project.constant.Authority;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "member")
public class MemberEntity {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String memberEmail;
    String memberPassword;
    String phone;
    String memberNickName;
    int point;
    Date birth;
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;



    @Builder
    public MemberEntity(String memberEmail, String memberPassword, String phone, String nickName, Date birth, Authority authority, int point) {
        this.memberEmail = memberEmail;
        this.memberPassword = memberPassword;
        this.phone = phone;
        this.memberNickName = nickName;
        this.birth = birth;
        this.authority = authority;
        this.point = point;
        this.registrationDate = LocalDateTime.now(); // 현재 날짜와 시간을 얻음
    }
}
