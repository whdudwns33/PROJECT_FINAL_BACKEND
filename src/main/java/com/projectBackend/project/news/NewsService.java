package com.projectBackend.project.news;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final RestHighLevelClient elasticsearchClient;

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

    // 특정 필드에서 특정 토큰을 사용한 Elasticsearch 검색 수행
    public void searchByTokenizer(String field, String token) throws IOException {
        // Elasticsearch에 요청을 보낼 SearchRequest 객체 생성
        SearchRequest searchRequest = new SearchRequest("news_index");

        // SearchSourceBuilder를 사용하여 검색에 대한 세부 설정 구성
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // QueryBuilders.matchQuery()를 사용하여 토큰을 사용한 검색 쿼리 생성
        searchSourceBuilder.query(QueryBuilders.matchQuery(field, token));

        // SearchRequest에 SearchSourceBuilder를 설정
        searchRequest.source(searchSourceBuilder);

        // RestHighLevelClient를 사용하여 Elasticsearch에 검색 요청을 보내고 결과를 받음
        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);

        // 검색 결과를 처리 (필요에 따라 추가적인 로직 구현)
        // ...
    }

}
