package com.projectBackend.project.news.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface NewsRepository extends ElasticsearchRepository<NewsEntity, String> {
}
