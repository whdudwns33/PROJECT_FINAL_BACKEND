package com.projectBackend.project.mypage;

import com.projectBackend.project.buy.BuyDto;
import com.projectBackend.project.buy.BuyEntity;
import com.projectBackend.project.buy.BuyRepository;
import com.projectBackend.project.member.MemberDto;
import com.projectBackend.project.member.MemberEntity;
import com.projectBackend.project.member.MemberRepository;
import com.projectBackend.project.stock.StockDto;
import com.projectBackend.project.utils.MultiDto;
import com.projectBackend.project.utils.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyPageService {
    private final MemberRepository memberRepository;
    private final BuyRepository buyRepository;
    private final TokenProvider tokenProvider;


    // 회원 정보 및 구매 이력 가져오기
    public MultiDto getMemberAndBuy(MultiDto multiDto) {
        MultiDto newDto = new MultiDto();
        String email = tokenProvider.getUserEmail(multiDto.getAccessToken());
        Optional<MemberEntity> memberEntityOptional = memberRepository.findByMemberEmail(email);
        if(memberEntityOptional.isPresent()) {
            MemberEntity memberEntity = memberEntityOptional.get();
            Long id = memberEntity.getId();
            List<BuyEntity> buyEntityList = buyRepository.findByMemberId(id);
            log.info("buyEntityList : {}",buyEntityList);

            if (buyEntityList != null) {
                MemberDto memberDto = MemberDto.of(memberEntity);
                List<BuyDto> buyDtoList = new ArrayList<>();
                List<StockDto> stockDtoList = new ArrayList<>();
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
                return null;
            }
        }
        return null;
    }

    // 포인트 충전
    public boolean savePointMethod(MultiDto multiDto) {
        int point = multiDto.getMemberDto().getPoint();
        String email = tokenProvider.getUserEmail(multiDto.getAccessToken());
        // 데이터베이스의 회원 정보
        Optional<MemberEntity> memberEntityOptional = memberRepository.findByMemberEmail(email);
        if (memberEntityOptional.isPresent()) {
            MemberEntity memberEntity = memberEntityOptional.get();
            int memberPoint = memberEntity.getPoint();
            memberEntity.setPoint(memberPoint + point);
            memberRepository.save(memberEntity);
            return true;
        } else {
            return false;
        }
    }
}
