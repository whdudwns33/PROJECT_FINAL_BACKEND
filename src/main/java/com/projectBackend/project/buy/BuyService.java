package com.projectBackend.project.buy;

import com.projectBackend.project.member.MemberEntity;
import com.projectBackend.project.member.MemberRepository;
import com.projectBackend.project.stock.StockDto;
import com.projectBackend.project.stock.jpa.RecentStockEntity;
import com.projectBackend.project.utils.CommonService;
import com.projectBackend.project.utils.MultiDto;
import com.projectBackend.project.utils.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BuyService {

    private final BuyRepository buyRepository;
    private final CommonService commonService;
    private final MemberRepository memberRepository;

    // 회원 조회 및 구매 이력 조회
    public MultiDto getData(MultiDto multiDto) {
        MultiDto newDto = new MultiDto();
        String email = commonService.returnEmail(multiDto);
        log.info("email : {}", email);
        String name = multiDto.getStockDto().getStockName();
        log.info("name : {}", name);

        Optional<MemberEntity> memberEntityOptional = memberRepository.findByMemberEmail(email);
        if(memberEntityOptional.isPresent()) {
            MemberEntity memberEntity = memberEntityOptional.get();
            Long id = memberEntity.getId();
            List<BuyEntity> buyEntityList = buyRepository.findByMemberIdAndName(id, name);
            log.info("buyEntityList : {}", buyEntityList);

            // 주식 리스트
            List<StockDto> stockDtoList = new ArrayList<>();
            // 구매 리스트
            List<BuyDto> buyDtoList = new ArrayList<>();

            // 구매 이력에서 주식이름은 주식 리스트에 담고,
            // 기격이나 구매수량은 구매 리스트에 담는다.
            if(buyEntityList != null) {
                for(BuyEntity buyEntity : buyEntityList) {
                    log.info("buyEntity : {}", buyEntity);
                    StockDto stockDto = new StockDto();
                    BuyDto buyDto = new BuyDto();
                    stockDto.setStockName(buyEntity.getName());
                    buyDto.setBuyCount(buyEntity.getBuyCount());
                    buyDto.setBuyPrice(buyEntity.getBuyPrice());
                    stockDtoList.add(stockDto);
                    buyDtoList.add(buyDto);
                }
                newDto.setBuyDtoList(buyDtoList);
                newDto.setStockDtoList(stockDtoList);
                return newDto;
            }
            else {
                log.info("구매 내역이 없음");
                return null;
            }
        }
        else {
            log.info("memberEntityOptional 없습니다.");
            return null;
        }
    }

    // 구매(매수)
    @Transactional
    public boolean buy(MultiDto multiDto) {
        // 이메일 값으로 멤버 조회
        String email = commonService.returnEmail(multiDto);
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

    // 판매(매도)
    public boolean sell(MultiDto multiDto) {
        // 이메일 값으로 멤버 조회
        String email = commonService.returnEmail(multiDto);
        log.info("email : {}", email);
        String name = multiDto.getStockDto().getStockName();
        log.info("name : {}", name);
        // 총 수량
        int sellCount = multiDto.getBuyDto().sellCount;
        log.info("sellCount : {}", sellCount);
        int sellPrice = multiDto.getBuyDto().sellPrice;
        log.info("sellPrice : {}", sellPrice);

        Optional<MemberEntity> memberEntityOptional = memberRepository.findByMemberEmail(email);
        if (memberEntityOptional.isPresent()) {
            MemberEntity memberEntity = memberEntityOptional.get();
            Long id = memberEntity.getId();
            // 데이터베이스의 데이터 조회
            List<BuyEntity> buyEntityList = buyRepository.findByMemberIdAndName(id, name);

            log.info("buyEntityList : {}", buyEntityList);
            if (buyEntityList != null) {
                for (BuyEntity buyEntity : buyEntityList) {
                    if(sellCount > 0) {
                        int count = buyEntity.getBuyCount();

                        if (count >= sellCount) {
                            buyEntity.setBuyCount(count - sellCount);
                            buyRepository.save(buyEntity);
                            break;
                        } else {
                            buyEntity.setBuyCount(0);
                            sellCount -= count;
                            buyRepository.save(buyEntity);
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
