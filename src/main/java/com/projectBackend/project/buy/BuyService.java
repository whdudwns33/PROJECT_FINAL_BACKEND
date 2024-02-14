package com.projectBackend.project.buy;

import com.projectBackend.project.member.MemberDto;
import com.projectBackend.project.member.MemberEntity;
import com.projectBackend.project.member.MemberRepository;
import com.projectBackend.project.stock.StockDto;
import com.projectBackend.project.utils.CommonService;
import com.projectBackend.project.utils.MultiDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.*;
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

    // 현재 시간 및 요일 확인
    ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    LocalTime startTime = LocalTime.of(9, 0);  // 09:00
    LocalTime endTime = LocalTime.of(15, 30);  // 15:30

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
                    stockDto.setStockName(buyEntity.getName());
                    BuyDto buyDto = BuyDto.builder()
                            .buyPrice(buyEntity.getBuyPrice())
                            .buyCount(buyEntity.getBuyCount())
                            .build();
                    stockDtoList.add(stockDto);
                    buyDtoList.add(buyDto);
                }
                newDto.setBuyDtoList(buyDtoList);
                newDto.setStockDtoList(stockDtoList);
                newDto.setMemberDto(MemberDto.of(memberEntity));
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
        // 주식 판매 가능한지 여부 확인
        if (isStockPurchaseAllowed(now, startTime, endTime)) {
            // 이메일 값으로 멤버 조회
            String email = commonService.returnEmail(multiDto);
            String name = multiDto.getStockDto().getStockName();
            String code = multiDto.getStockDto().getStockCode();

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
                    buyEntity.setCode(code);
                    buyEntity.setMember(memberEntity);
                    // 구매할때, 지금 지금 날짜 저장
                    buyEntity.setDate(LocalDate.now());

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
        }else {
            log.info("주식 판매 가능한 시간이 아니거나 주말입니다.");
            return false;
        }
    }

    // 판매(매도)
    @Transactional
    public boolean sell(MultiDto multiDto) {
        // 주식 판매 가능한지 여부 확인
        if (isStockPurchaseAllowed(now, startTime, endTime)) {
            // 이메일 값으로 멤버 조회
            String email = commonService.returnEmail(multiDto);
            log.info("email : {}", email);
            String name = multiDto.getStockDto().getStockName();
            log.info("name : {}", name);
            // 총 수량
            int sellCount = multiDto.getBuyDto().getSellCount();
            log.info("sellCount : {}", sellCount);
            int sellPrice = multiDto.getBuyDto().getSellPrice();
            log.info("sellPrice : {}", sellPrice);
            // 구매한 총 수량
            int totalCount = 0;
            
            Optional<MemberEntity> memberEntityOptional = memberRepository.findByMemberEmail(email);
            if (memberEntityOptional.isPresent()) {
                MemberEntity memberEntity = memberEntityOptional.get();
                int total = memberEntity.getPoint() + sellPrice;
                Long id = memberEntity.getId();
                // 데이터베이스의 데이터 조회
                List<BuyEntity> buyEntityList = buyRepository.findByMemberIdAndName(id, name);

                log.info("buyEntityList : {}", buyEntityList);
                if (!buyEntityList.isEmpty()) {
                    // 총 구매 수량 계산
                    for (BuyEntity buyEntity : buyEntityList) {
                        int buyCount = buyEntity.getBuyCount();
                        totalCount = totalCount + buyCount;
                    }
                    log.info("totalCount : {}", totalCount);
                    // 매도 매수 수량 비교
                    for (BuyEntity buyEntity : buyEntityList) {
                        if (sellCount > 0 && sellCount <= totalCount) {
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
                        else {
                            return false;
                        }
                    }
                    // 최종 가격
                    memberEntity.setPoint(total);
                    memberRepository.save(memberEntity);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            log.info("주식 판매 가능한 시간이 아니거나 주말입니다.");
            return false;
        }
    }

    // 시간 체크
    private boolean isStockPurchaseAllowed(ZonedDateTime now, LocalTime startTime, LocalTime endTime) {
        return !(now.toLocalTime().isBefore(startTime) || now.toLocalTime().isAfter(endTime) || now.getDayOfWeek().equals(DayOfWeek.SATURDAY) || now.getDayOfWeek().equals(DayOfWeek.SUNDAY));
    }
}
