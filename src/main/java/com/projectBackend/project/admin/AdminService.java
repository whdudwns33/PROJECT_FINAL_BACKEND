package com.projectBackend.project.admin;

import com.projectBackend.project.buy.BuyDto;
import com.projectBackend.project.buy.BuyEntity;
import com.projectBackend.project.buy.BuyRepository;
import com.projectBackend.project.member.MemberDto;
import com.projectBackend.project.member.MemberEntity;
import com.projectBackend.project.member.MemberRepository;
import com.projectBackend.project.stock.StockDto;
import com.projectBackend.project.utils.MultiDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;
    private final BuyRepository buyRepository;

    // 회원을 검색하는 메서드
    public Page<MemberDto> searchMembers(String keyword, Pageable pageable) {
        return memberRepository.search(keyword, pageable).map(MemberDto::of);
    }

    // 회원 조회 및 구매 이력 조회
    public MultiDto getUserBuyData(MultiDto multiDto) {
        MemberDto memberDto = multiDto.getMemberDto();
        String email = memberDto.getMemberEmail();
        MultiDto newDto = new MultiDto();
        Optional<MemberEntity> memberEntityOptional = memberRepository.findByMemberEmail(email);
        if(memberEntityOptional.isPresent()) {
            MemberEntity memberEntity = memberEntityOptional.get();
            Long id = memberEntity.getId();
            List<BuyEntity> buyEntityList = buyRepository.findByMemberId(id);

            // 주식 리스트
            List<StockDto> stockDtoList = new ArrayList<>();
            // 구매 리스트
            List<BuyDto> buyDtoList = new ArrayList<>();

            // 구매 이력에서 주식이름은 주식 리스트에 담고,
            // 기격이나 구매수량은 구매 리스트에 담는다.
            if(buyEntityList != null) {
//                for(BuyEntity buyEntity : buyEntityList) {
//                    log.info("buyEntity : {}", buyEntity);
//                    StockDto stockDto = new StockDto();
//                    stockDto.setStockName(buyEntity.getName());
//                    BuyDto buyDto = BuyDto.builder()
//                            .buyPrice(buyEntity.getBuyPrice())
//                            .buyCount(buyEntity.getBuyCount())
//                            .build();
//                    stockDtoList.add(stockDto);
//                    buyDtoList.add(buyDto);
//                }
//                newDto.setBuyDtoList(buyDtoList);
//                newDto.setStockDtoList(stockDtoList);
//                newDto.setMemberDto(MemberDto.of(memberEntity));
                for(BuyEntity buy : buyEntityList) {
                    StockDto stockDto = new StockDto();
                    BuyDto buyDto = BuyDto.of(buy);
                    stockDto.setStockCode(buy.getCode());
                    stockDto.setStockName(buy.getName());
                    buyDtoList.add(buyDto);
                    stockDtoList.add(stockDto);
                }
                newDto.setMemberDto(memberDto);
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

}
