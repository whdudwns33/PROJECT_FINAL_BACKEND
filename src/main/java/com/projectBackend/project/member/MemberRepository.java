package com.projectBackend.project.member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByMemberEmail(String email);
    Optional<MemberEntity> findByMemberNickName(String nickName);
    boolean existsByMemberEmail(String email);
    boolean existsByNickname(String nickName);
}
