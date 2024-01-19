package com.projectBackend.project.utils.token;

import com.projectBackend.project.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    String findByMember_Id(Long memberId);
    void deleteByMember_Id(Long memberId);
}
