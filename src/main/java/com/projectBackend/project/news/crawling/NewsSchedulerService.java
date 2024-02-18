package com.projectBackend.project.news.crawling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectBackend.project.news.elastic.NewsDto;
import com.projectBackend.project.utils.Common;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsSchedulerService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final TvRepository tvRepository;
    private final RtRepository rtRepository;
    private final MnRepository mnRepository;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void tvNews() throws JsonProcessingException {
        String url = Common.python + "/tvnews";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String data = response.getBody();
        if (data != null) {
            // 디티오는  공유
            List<NewsDto> newsDtos = objectMapper.readValue(data, new TypeReference<List<NewsDto>>() {
            });
            List<TvNewsEntity> newsList = tvRepository.findAll();
            int size = newsList.size();
            if (size > 0) {
                tvRepository.deleteAll();
                for (NewsDto newsDto : newsDtos) {
                    TvNewsEntity tvNewsEntity = new TvNewsEntity();
                    tvNewsEntity.setDescription(newsDto.getDescription());
                    tvNewsEntity.setThumb(newsDto.getThumb());
                    tvNewsEntity.setLink(newsDto.getLink());
                    tvRepository.save(tvNewsEntity);
                }
            }
            else {
                tvRepository.deleteAll();
                for (NewsDto newsDto : newsDtos) {
                    TvNewsEntity tvNewsEntity = new TvNewsEntity();
                    tvNewsEntity.setDescription(newsDto.getDescription());
                    tvNewsEntity.setThumb(newsDto.getThumb());
                    tvNewsEntity.setLink(newsDto.getLink());
                    tvRepository.save(tvNewsEntity);
                }
            }
        }
    }

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void rtNews() throws JsonProcessingException {
        String url = Common.python + "/rtnews";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String data = response.getBody();
//        log.info("String data : {}", data);
        if (data != null) {
            // 디티오는  공유
            List<NewsDto> newsDtos = objectMapper.readValue(data, new TypeReference<List<NewsDto>>() {
            });
            List<RtNewsEntity> newsList = rtRepository.findAll();
            int size = newsList.size();
            if (size > 0) {
                rtRepository.deleteAll();
                for (NewsDto newsDto : newsDtos) {
                    RtNewsEntity rtNewsEntity = new RtNewsEntity();
                    rtNewsEntity.setDescription(newsDto.getDescription());
                    rtNewsEntity.setLink(newsDto.getLink());
                    rtRepository.save(rtNewsEntity);
                }
            }
            else {
                for (NewsDto newsDto : newsDtos) {
                    RtNewsEntity rtNewsEntity = new RtNewsEntity();
                    rtNewsEntity.setDescription(newsDto.getDescription());
                    rtNewsEntity.setLink(newsDto.getLink());
                    rtRepository.save(rtNewsEntity);
                }
            }
        }
    }

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void mnNews() throws JsonProcessingException {
        String url = Common.python + "/manynews";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String data = response.getBody();
        if (data != null) {
            // 디티오는  공유
            List<NewsDto> newsDtos = objectMapper.readValue(data, new TypeReference<List<NewsDto>>() {
            });
            List<MnNewsEntity> newsList = mnRepository.findAll();
            int size = newsList.size();
            if (size > 0) {
                mnRepository.deleteAll();
                for (NewsDto newsDto : newsDtos) {
                    MnNewsEntity mnNewsEntity = new MnNewsEntity();
                    mnNewsEntity.setDescription(newsDto.getDescription());
                    mnNewsEntity.setLink(newsDto.getLink());
                    mnRepository.save(mnNewsEntity);
                }
            }
            else {
                for (NewsDto newsDto : newsDtos) {
                    MnNewsEntity mnNewsEntity = new MnNewsEntity();
                    mnNewsEntity.setDescription(newsDto.getDescription());
                    mnNewsEntity.setLink(newsDto.getLink());
                    mnRepository.save(mnNewsEntity);
                }
            }
        }
    }
}
