package com.projectBackend.project.news;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface NewsRepository extends ElasticsearchRepository<NewsEntity, String> {
}
