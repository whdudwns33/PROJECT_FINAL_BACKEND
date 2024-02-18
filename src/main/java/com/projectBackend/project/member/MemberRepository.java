package com.projectBackend.project.member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByMemberEmail(String email);
    Optional<MemberEntity> findByMemberNickName(String nickName);
    boolean existsByMemberEmail(String email);
    boolean existsByMemberNickName(String nickName);
    List<MemberEntity> findAllByPhone(String phone);

    @Query("SELECT m FROM MemberEntity m WHERE " +
            "LOWER(m.memberEmail) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.memberNickName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.authority) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.memberPassword) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.birth) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.registrationDate) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<MemberEntity> search(@Param("keyword") String keyword, Pageable pageable);

}
