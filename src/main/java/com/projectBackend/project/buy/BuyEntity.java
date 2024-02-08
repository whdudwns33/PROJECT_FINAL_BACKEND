package com.projectBackend.project.buy;

import com.projectBackend.project.member.MemberEntity;
import com.projectBackend.project.stock.jpa.RecentStockEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

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
    Long id;

    String name;
    // 구매 가격
    int buyPrice;
    // 구매 수량
    int buyCount;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "buy_id")
    private List<RecentStockEntity> recentStockEntitys;
}
