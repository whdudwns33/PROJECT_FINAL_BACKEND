package com.projectBackend.project.news;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;

    public boolean saveToElastic(List<NewsDto> newsDtos) {
        try {
            List<NewsEntity> newsEntities = new ArrayList<>();
            for(NewsDto newsDto : newsDtos) {
               NewsEntity news = newsDto.toEntity(newsDto);
               newsEntities.add(news);
            }
            newsRepository.saveAll(newsEntities);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
