package com.projectBackend.project.community;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<CommunityEntity, Long> {
    Page<CommunityEntity> findByContentContaining(String content, Pageable pageable);
    Page<CommunityEntity> findByAuthorIdContaining(String authorId, Pageable pageable);
}
