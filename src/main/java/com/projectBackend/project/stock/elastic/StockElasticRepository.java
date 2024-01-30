package com.projectBackend.project.stock.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StockElasticRepository extends ElasticsearchRepository<StockElasticEntity, String> {
    long count();
}