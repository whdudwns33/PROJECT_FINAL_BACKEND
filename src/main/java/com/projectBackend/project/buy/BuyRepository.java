package com.projectBackend.project.buy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyRepository extends JpaRepository<BuyEntity, Long> {
    List<BuyEntity> findByMemberIdAndName(Long memberId, String Name);
    List<BuyEntity> findByMemberId(Long id);


}
