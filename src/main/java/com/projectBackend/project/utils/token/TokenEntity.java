package com.projectBackend.project.utils.token;

import com.projectBackend.project.member.MemberEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "token")
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String refreshToken; // 리프레시 토큰
    @OneToOne
    @JoinColumn(name = "user_id")
    MemberEntity member;

}
