package com.projectBackend.project.news;

import com.projectBackend.project.news.crawling.*;
import com.projectBackend.project.stock.StockDto;
import com.projectBackend.project.utils.MultiDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
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
    private final TvRepository tvRepository;
    private final RtRepository rtRepository;
    private final MnRepository mnRepository;

    public boolean saveToElastic(List<NewsDto> newsDtos) throws IOException {
        try {
            int elasticValue = getIndexValue();
            // 스프링부트의 엔티티일 경우
            if (elasticValue == 0) {
                // 데이터 삭제
                boolean acknowledged = deleteIndex();
                if (acknowledged) {
                    // 인덱스 구조 설정
                    boolean res = createIndexWithMapping();
                    if (res) {
                        return saveNews(newsDtos);
                    }
                    else {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
            // 세팅과 맵핑이 적절하게 설정되어 있는 인덱스의 경우
            // 바로 데이터 저장
            else {
                return saveNews(newsDtos);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // news_index의 value 값 조회
    public int getIndexValue () {
        // 만약 엘라스틱 서치의 데이터가 0개 이면 세팅과 맵핑이 되지 않은 스프링부트의 엔티티임
        // 따라서, 해당 엔티티는 제거
        SearchRequest searchRequest = new SearchRequest("news_index");
        SearchResponse searchResponse = null;
        try {
             searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SearchHits hits = searchResponse.getHits();
        return (int) hits.getTotalHits().value;
    }

    public boolean deleteIndex() {
        // 엘라스틱 index는 존재하지만, 데이터값이 없는 경우 제거
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("news_index");
        AcknowledgedResponse deleteIndexResponse = null;
        try {
            deleteIndexResponse = elasticsearchClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return deleteIndexResponse.isAcknowledged();
    }

    // 인덱스 구조 설정
    private boolean createIndexWithMapping() throws IOException {
        try {
            log.info("createIndexWithMapping");
            // Elasticsearch에 "news_index" 인덱스 생성 요청
            CreateIndexRequest createIndexRequest = new CreateIndexRequest("news_index");
            // "settings" 및 "mappings"를 JSON 형식으로 설정
            String settingsAndMappings = "{\"settings\": {\"analysis\": {\"tokenizer\": {\"nori_user_dict_tokenizer\": {\"type\": \"nori_tokenizer\",\"decompound_mode\": \"mixed\",\"discard_punctuation\": \"false\"}},\"filter\": {\"korean_stop\": {\"type\": \"stop\",\"stopwords_path\": \"D:/tools/elasticsearch-7.17.12/config/analysis/korean_stopwords.txt\"},\"nori_filter\": {\"type\": \"nori_part_of_speech\",\"stoptags\": [\"E\", \"IC\", \"J\", \"MAG\", \"MAJ\", \"MM\", \"SP\", \"SSC\", \"SSO\", \"SC\", \"SE\", \"XPN\", \"XSA\", \"XSN\", \"XSV\", \"UNA\", \"NA\", \"VSV\", \"NP\"]},\"ngram_filter\": {\"type\": \"ngram\",\"min_gram\": 2,\"max_gram\": 3}},\"analyzer\": {\"nori_analyzer_with_stopwords\": {\"type\": \"custom\",\"tokenizer\": \"nori_user_dict_tokenizer\",\"filter\": [\"nori_readingform\", \"korean_stop\", \"nori_filter\", \"trim\"]},\"nori_ngram_analyzer\": {\"type\": \"custom\",\"tokenizer\": \"nori_user_dict_tokenizer\",\"filter\": [\"nori_readingform\", \"ngram_filter\", \"trim\"]}}}}," +
                    "\"mappings\": {\"properties\": {\"title\": {\"type\": \"text\",\"analyzer\": \"nori_analyzer_with_stopwords\"},\"description\": {\"type\": \"text\",\"analyzer\": \"nori_analyzer_with_stopwords\"},\"originalLink\": {\"type\": \"keyword\"},\"link\": {\"type\": \"text\"},\"pubDate\": {\"type\": \"date\",\"format\": \"yyyyMMdd\"},\"id\": {\"type\": \"keyword\"}}}}";
            // "settings"와 "mappings"를 설정
            createIndexRequest.source(settingsAndMappings, XContentType.JSON);
            // 인덱스 생성 및 설정 적용
            CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            log.info("createIndexResponse : {}", createIndexResponse);
            // 인덱스 생성 요청이 Elasticsearch 클러스터에 성공적으로 전달되었는지 여부 확인
            boolean acknowledged = createIndexResponse.isAcknowledged();
            if (acknowledged) {
                log.info("Index creation successful");
                return true;
            } else {
                log.error("Failed to create index");
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 뉴스 데이터 저장 메서드
    public boolean saveNews (List<NewsDto> newsDtos) {
        log.info("elastic save");
        // 인덱스 바디 생성
        List<NewsEntity> newsEntities = new ArrayList<>();
        for (NewsDto newsDto : newsDtos) {
            NewsEntity news = newsDto.toEntity(newsDto);
            newsEntities.add(news);
        }
        newsRepository.saveAll(newsEntities);
        return true;
    }


    // 검색 쿼리
    // 특정 필드에서 특정 토큰을 사용한 Elasticsearch 검색 수행
    public List<NewsDto> searchByTokenizer(String query) throws IOException {
        List<String> fields = new ArrayList<>();
        fields.add("title");
        fields.add("description");
        if (fields != null && query != null) {
            List<NewsDto> newsDtos = new ArrayList<>();

            // Elasticsearch에 요청을 보낼 SearchRequest 객체 생성
            // 해당 객체가 검색에 사용되는 객체
            SearchRequest searchRequest = new SearchRequest("news_index");

            // SearchSourceBuilder를 사용하여 검색에 대한 세부 설정 구성
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            // QueryBuilders.multiMatchQuery()를 사용하여 다중 필드에 대한 검색 쿼리 생성
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery(query, fields.toArray(new String[0]))
                    .fuzziness("AUTO")
                    .prefixLength(3));

            // SearchRequest에 SearchSourceBuilder를 설정
            searchRequest.source(searchSourceBuilder);

            // RestHighLevelClient를 사용하여 Elasticsearch에 검색 요청을 보내고 결과를 받음
            SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
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
            return newsDtos;
        } else {
            return null;
        }
    }

    // 뉴스 페이지 크롤링 가져오기
    public List<List<NewsDto>> getNewsPage() {
        try {
            // 데이터 베이스의 값
            List<TvNewsEntity> tvNewsEntities = tvRepository.findAll();
            List<MnNewsEntity> mnNewsEntities = mnRepository.findAll();
            List<RtNewsEntity> rtNewsEntities = rtRepository.findAll();

            // Dto List로 변환
            List<NewsDto> tvDtos = new ArrayList<>();
            List<NewsDto> mnDtos = new ArrayList<>();
            List<NewsDto> rtDtos = new ArrayList<>();

            // TvNewsEntity의 속성값 출력
            for (TvNewsEntity tvNewsEntity : tvNewsEntities) {
                NewsDto newsDto = NewsDto.builder()
                        .thumb(tvNewsEntity.getThumb())
                        .description(tvNewsEntity.getDescription())
                        .link(tvNewsEntity.getLink())
                        .build();
                tvDtos.add(newsDto);
            }

            // MnNewsEntity의 속성값 출력
            for (MnNewsEntity mnNewsEntity : mnNewsEntities) {
                NewsDto newsDto = NewsDto.builder()
                        .description(mnNewsEntity.getDescription())
                        .link(mnNewsEntity.getLink())
                        .build();
                mnDtos.add(newsDto);
            }

            // RtNewsEntity의 속성값 출력
            for (RtNewsEntity rtNewsEntity : rtNewsEntities) {
                NewsDto newsDto = NewsDto.builder()
                        .description(rtNewsEntity.getDescription())
                        .link(rtNewsEntity.getLink())
                        .build();
                rtDtos.add(newsDto);
            }
            List<List<NewsDto>> newsDtoListInList = new ArrayList<>();
            newsDtoListInList.add(tvDtos);
            newsDtoListInList.add(rtDtos);
            newsDtoListInList.add(mnDtos);
            return newsDtoListInList;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
