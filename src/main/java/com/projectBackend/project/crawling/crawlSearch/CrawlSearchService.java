package com.projectBackend.project.crawling.crawlSearch;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CrawlSearchService {
    private final CrawlSearchRepository crawlSearchRepository;

    public List<CrawlSearchDto> getCrawlSearchList() {
        List<CrawlSearchEntity> crawlSearchEntities = crawlSearchRepository.findAll();
        List<CrawlSearchDto> crawlSearchDtoList = new ArrayList<>();
        for (CrawlSearchEntity crawlSearchEntity : crawlSearchEntities) {
            CrawlSearchDto crawlSearchDto = CrawlSearchDto.of(crawlSearchEntity);
            crawlSearchDtoList.add(crawlSearchDto);
        }
        return crawlSearchDtoList;
    }

    public CrawlSearchDto getCrawlSearchById(Long id) {
        // Optional을 사용하여 findById 결과를 처리하는 것이 좋습니다.
        return crawlSearchRepository.findById(id)
                .map(CrawlSearchDto::of)
                .orElse(null);
    }

    public void saveCrawlSearch(CrawlSearchDto crawlSearchDto) {
        // DTO를 Entity로 변환하여 저장
        CrawlSearchEntity crawlSearchEntity = CrawlSearchEntity.builder()
                .searchName(crawlSearchDto.getSearchName())
                .searchCount(crawlSearchDto.getSearchCount())
                .searchUpdown(crawlSearchDto.getSearchUpdown())
                .searchPrice(crawlSearchDto.getSearchPrice())
                .searchChangeRate(crawlSearchDto.getSearchChangeRate())
                .searchMarketCap(crawlSearchDto.getSearchMarketCap())
                .build();

        crawlSearchRepository.save(crawlSearchEntity);
    }

    public void deleteCrawlSearch(Long id) {
        crawlSearchRepository.deleteById(id);
    }
}
