package com.projectBackend.project.admin;

import com.projectBackend.project.member.MemberDto;
import com.projectBackend.project.member.MemberEntity;
import com.projectBackend.project.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;

    // 회원을 검색하는 메서드
    public Page<MemberDto> searchMembers(String keyword, Pageable pageable) {
        return memberRepository.search(keyword, pageable).map(MemberDto::of);
    }

}
