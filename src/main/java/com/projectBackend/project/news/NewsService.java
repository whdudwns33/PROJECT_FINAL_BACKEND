package com.projectBackend.project.news;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
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
    public List<NewsDto> searchByTokenizer(List<String> fields, String token) throws IOException {
        if (fields != null && token != null) {
            List<NewsDto> newsDtos = new ArrayList<>();
            for (String field : fields) {
                // Elasticsearch에 요청을 보낼 SearchRequest 객체 생성
                SearchRequest searchRequest = new SearchRequest("news_index");
                // SearchSourceBuilder를 사용하여 검색에 대한 세부 설정 구성
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                // QueryBuilders.matchQuery()를 사용하여 토큰을 사용한 검색 쿼리 생성
                searchSourceBuilder.query(QueryBuilders.matchQuery(field, token));
                // SearchRequest에 SearchSourceBuilder를 설정
                searchRequest.source(searchSourceBuilder);
                // RestHighLevelClient를 사용하여 Elasticsearch에 검색 요청을 보내고 결과를 받음
                SearchResponse searchResponse = null;

                searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
                SearchHits hits = searchResponse.getHits();
                log.info("hits : {}", hits);

                // SearchHits에서 결과를 추출
                for (SearchHit hit : hits) {
                    // 각 hit에서 필요한 정보를 추출
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();

                    // 추출한 정보를 사용하여 NewsDto를 생성하거나 처리
                    // 예시: title, link, description, pubDate 등 필드를 추출
                    String title = (String) sourceAsMap.get("title");
                    String link = (String) sourceAsMap.get("link");
                    String description = (String) sourceAsMap.get("description");
                    String pubDate = (String) sourceAsMap.get("pubDate");

                    // 추출한 정보를 사용하여 NewsDto를 생성하거나 처리
                    NewsDto newsDto = NewsDto.builder()
                            .title(title)
                            .link(link)
                            .description(description)
                            .pubDate(pubDate)
                            .build();
                    newsDtos.add(newsDto);
                }
            }
            // NewsDto를 반환하거나 처리
            return newsDtos;
        }
        else {
            return null;
        }
    }
}
