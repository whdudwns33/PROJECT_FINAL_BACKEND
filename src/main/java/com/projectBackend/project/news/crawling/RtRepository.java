package com.projectBackend.project.news.crawling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RtRepository extends JpaRepository<RtNewsEntity, Long> {
}
