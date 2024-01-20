package com.projectBackend.project.utils.configration;

import com.projectBackend.project.utils.Common;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;


@Configuration
public class ElasticsearchClientConfig {
//    @Value("${spring.elasticsearch.uris}")
    String elasticsearchUrl = Common.elastic;

    @Bean
    public RestHighLevelClient client() { // RestHighLevelClient 객체를 생성하여 Bean으로 등록
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(elasticsearchUrl)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}