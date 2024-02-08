package com.projectBackend.project.buy;

import com.projectBackend.project.member.MemberEntity;
import com.projectBackend.project.member.MemberRepository;
import com.projectBackend.project.stock.StockDto;
import com.projectBackend.project.stock.jpa.RecentStockEntity;
import com.projectBackend.project.utils.MultiDto;
import com.projectBackend.project.utils.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BuyService {

    private final BuyRepository buyRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @Transactional
    public boolean buy(MultiDto multiDto) {
        // 이메일 값으로 멤버 조회
        String accessToken = multiDto.getAccessToken();
        String email = tokenProvider.getUserEmail(accessToken);
        String name = multiDto.getStockDto().getStockName();

        // 회원 조회
        Optional<MemberEntity> memberEntityOptional = memberRepository.findByMemberEmail(email);
        if (memberEntityOptional.isPresent()) {
            MemberEntity memberEntity = memberEntityOptional.get();

            // 구매한 가격, 갯수 전달
            BuyDto buyDto = multiDto.getBuyDto();
            int buyPrice = buyDto.getBuyPrice();
            int buyCount = buyDto.getBuyCount();
            int totalCost = buyPrice * buyCount;

            if (memberEntity.getPoint() >= totalCost) {
                // 충분한 포인트가 있을 경우 구매 처리
                memberEntity.setPoint(memberEntity.getPoint() - totalCost);
                memberRepository.save(memberEntity);

                // 새로운 구매 기록 생성
                BuyEntity buyEntity = new BuyEntity();
                buyEntity.setBuyCount(buyCount);
                buyEntity.setBuyPrice(buyPrice);
                buyEntity.setName(name);
                buyEntity.setMember(memberEntity);

                buyRepository.save(buyEntity);

                return true;
            } else {
                log.warn("포인트가 부족합니다.: {}", email);

                return false;
            }
        } else {
            // 사용자를 찾을 수 없음
            log.warn("Member not found for email: {}", email);
            return false;
        }
    }
}
