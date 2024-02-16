package com.projectBackend.project.stock.elastic;

import com.projectBackend.project.news.NewsDto;
import com.projectBackend.project.news.NewsEntity;
import com.projectBackend.project.stock.StockDto;
import com.projectBackend.project.stock.jpa.RecentStockEntity;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockElasticService {


    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    private final StockElasticRepository stockElasticRepository;

    private final RestHighLevelClient elasticsearchClient;

    @Transactional
    public void saveStocksToElasticsearch(Map<String, List<StockDto>> stockDataMap) {
        // Get the current year and month
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        String currentYearMonth = currentDate.format(formatter);

        for (Map.Entry<String, List<StockDto>> entry : stockDataMap.entrySet()) {
            String yearMonth = entry.getKey();

            // 들어온 데이터의 key가 현재 년월인지?
            if (currentYearMonth.equals(yearMonth)) {
                log.info("save yearMonth {}", yearMonth);
                List<StockDto> stockDtoList = entry.getValue();

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
                                // Bulk Insert
                                List<StockElasticEntity> elasticEntities = convertToElasticEntities(stockDtoList);
                                bulkInsert(elasticEntities);
                                log.info("elasticValue == 0, bulk Insert");
                            }
                        }
                    }
                    // 세팅과 맵핑이 적절하게 설정되어 있는 인덱스의 경우
                    // 바로 데이터 저장
                    else {
                        // 기존 데이터 truncate
                        deleteAllDocuments();
                        // Bulk Insert
                        List<StockElasticEntity> elasticEntities = convertToElasticEntities(stockDtoList);
                        bulkInsert(elasticEntities);
                        log.info("elasticValue != 0, bulk Insert");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void bulkInsert(List<StockElasticEntity> elasticEntities) {
        List<IndexQuery> indexQueries = new ArrayList<>();
        for (StockElasticEntity elasticEntity : elasticEntities) {
            IndexQuery indexQuery = new IndexQueryBuilder()
//                    .withId(elasticEntity.getId())
                    .withObject(elasticEntity)
                    .build();
            indexQueries.add(indexQuery);
        }

        if (!indexQueries.isEmpty()) {
            elasticsearchRestTemplate.bulkIndex(indexQueries, elasticsearchRestTemplate.getIndexCoordinatesFor(StockElasticEntity.class));
        }
    }

    private List<StockElasticEntity> convertToElasticEntities(List<StockDto> stockDtoList) {
        List<StockElasticEntity> elasticEntities = new ArrayList<>();

        for (StockDto stockDto : stockDtoList) {
            StockElasticEntity elasticEntity = new StockElasticEntity();

            elasticEntity.setStockOpen(stockDto.getOpen());
            elasticEntity.setStockHigh(stockDto.getHigh());
            elasticEntity.setStockLow(stockDto.getLow());
            elasticEntity.setStockClose(stockDto.getClose());
            elasticEntity.setStockVolume(stockDto.getVolume());
            elasticEntity.setStockTradingValue(stockDto.getTradingValue());
            elasticEntity.setStockFluctuationRate(stockDto.getFluctuationRate());
            elasticEntity.setStockDate(stockDto.getDate());
            elasticEntity.setStockCode(stockDto.getStockCode());
            elasticEntity.setStockName(stockDto.getStockName());
            elasticEntity.setStockBps(stockDto.getBps());
            elasticEntity.setStockPer(stockDto.getPer());
            elasticEntity.setStockPbr(stockDto.getPbr());
            elasticEntity.setStockEps(stockDto.getEps());
            elasticEntity.setStockDiv(stockDto.getDiv());
            elasticEntity.setStockDps(stockDto.getDps());

            elasticEntity.generateId(); // ID 생성 메서드 호출

            elasticEntities.add(elasticEntity);
        }

        return elasticEntities;
    }

    // stock_index의 value 값 조회
    public int getIndexValue () {
        // 만약 엘라스틱 서치의 데이터가 0개 이면 세팅과 맵핑이 되지 않은 스프링부트의 엔티티임
        // 따라서, 해당 엔티티는 제거
        SearchRequest searchRequest = new SearchRequest("stock_index");
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
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("stock_index");
        AcknowledgedResponse deleteIndexResponse = null;
        try {
            deleteIndexResponse = elasticsearchClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return deleteIndexResponse.isAcknowledged();
    }

    public void deleteAllDocuments() {
        Query query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchAllQuery()).build();

        elasticsearchRestTemplate.delete(query, StockElasticEntity.class, elasticsearchRestTemplate.getIndexCoordinatesFor(StockElasticEntity.class));
    }


    // 인덱스 구조 설정
    private boolean createIndexWithMapping() throws IOException {
        try {
            log.info("createIndexWithMapping");
            // Elasticsearch에 "news_index" 인덱스 생성 요청
            CreateIndexRequest createIndexRequest = new CreateIndexRequest("stock_index");
            // "settings" 및 "mappings"를 JSON 형식으로 설정
            String settingsAndMappings = "{\"settings\": {\"analysis\": {\"tokenizer\": {\"nori_user_dict_tokenizer\": {\"type\": \"nori_tokenizer\",\"decompound_mode\": \"mixed\",\"discard_punctuation\": \"false\"}},\"filter\": {\"korean_stop\": {\"type\": \"stop\",\"stopwords_path\": \"/Users/hwangjunho/coding/kh/final/ELK/elasticsearch-7.17.12/config/analysis/korean_stopwords.txt\"},\"nori_filter\": {\"type\": \"nori_part_of_speech\",\"stoptags\": [\"E\", \"IC\", \"J\", \"MAG\", \"MAJ\", \"MM\", \"SP\", \"SSC\", \"SSO\", \"SC\", \"SE\", \"XPN\", \"XSA\", \"XSN\", \"XSV\", \"UNA\", \"NA\", \"VSV\", \"NP\"]},\"ngram_filter\": {\"type\": \"ngram\",\"min_gram\": 2,\"max_gram\": 3}},\"analyzer\": {\"nori_analyzer_with_stopwords\": {\"type\": \"custom\",\"tokenizer\": \"nori_user_dict_tokenizer\",\"filter\": [\"nori_readingform\", \"korean_stop\", \"nori_filter\", \"trim\"]},\"nori_ngram_analyzer\": {\"type\": \"custom\",\"tokenizer\": \"nori_user_dict_tokenizer\",\"filter\": [\"nori_readingform\", \"ngram_filter\", \"trim\"]}}}}," +
            "\"mappings\": {\"properties\": {\"stockName\": {\"type\": \"text\",\"analyzer\": \"nori_analyzer_with_stopwords\"},\"id\": {\"type\": \"keyword\"},\"stockOpen\": {\"type\": \"long\"},\"stockHigh\": {\"type\": \"long\"},\"stockLow\": {\"type\": \"long\"},\"stockClose\": {\"type\": \"long\"},\"stockVolume\": {\"type\": \"long\"},\"stockTradingValue\": {\"type\": \"long\"},\"stockFluctuationRate\": {\"type\": \"double\"},\"stockDate\": {\"type\": \"long\"},\"stockCode\": {\"type\": \"text\"},\"stockBps\": {\"type\": \"double\"},\"stockPer\": {\"type\": \"double\"},\"stockPbr\": {\"type\": \"double\"},\"stockEps\": {\"type\": \"double\"},\"stockDiv\": {\"type\": \"double\"},\"stockDps\": {\"type\": \"double\"}}}}";


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
}
