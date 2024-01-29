package com.projectBackend.project.crawling.crawlEnergy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CrawlEnergyService {
    private final CrawlEnergyRepository crawlEnergyRepository;

    public List<CrawlEnergyDto> getCrawlEnergyList() {
        List<CrawlEnergyEntity> crawlEnergyEntities = crawlEnergyRepository.findAll();
        List<CrawlEnergyDto> crawlEnergyDtoList = new ArrayList<>();
        for (CrawlEnergyEntity crawlEnergyEntity : crawlEnergyEntities) {
            CrawlEnergyDto crawlEnergyDto = CrawlEnergyDto.of(crawlEnergyEntity);
            crawlEnergyDtoList.add(crawlEnergyDto);
        }
        return crawlEnergyDtoList;

    }

}
