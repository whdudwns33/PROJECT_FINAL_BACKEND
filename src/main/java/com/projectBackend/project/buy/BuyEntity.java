package com.projectBackend.project.buy;

import com.projectBackend.project.member.MemberEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "buy")
public class BuyEntity {
    @Id
    @Column(name = "buy_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 구매 종목 이름
    private String name;

    // 구매 종목 코드
    private String code;

    // 구매 가격
    private int buyPrice;

    // 구매 수량
    private int buyCount;

    // 구매 날짜
    @Column(columnDefinition = "DATE")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @Builder
    public BuyEntity(Long id, String name, String code, int buyPrice, int buyCount, LocalDate date, MemberEntity member) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.buyPrice = buyPrice;
        this.buyCount = buyCount;
        this.date = date;
        this.member = member;
    }
}
