package com.projectBackend.project.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 비밀번호 변경
    public boolean changePassword(MemberDto memberDto) {
        try {
            Optional<MemberEntity> member = memberRepository.findByMemberNickName(memberDto.getNickName());
            if (member.isPresent()) {
                MemberEntity memberEntity = member.get();
                memberEntity.setMemberPassword(passwordEncoder.encode(memberDto.getMemberPassword()));
                memberRepository.save(memberEntity);
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
