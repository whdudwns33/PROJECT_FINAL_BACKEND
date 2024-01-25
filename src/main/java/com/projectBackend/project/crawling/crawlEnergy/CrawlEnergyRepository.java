package com.projectBackend.project.crawling.crawlEnergy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrawlEnergyRepository extends JpaRepository<CrawlEnergyEntity, Long> {
    Optional<CrawlEnergyEntity> findByCrawlEnergyName(String name);
}
