package com.projectBackend.project.news;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;

@Getter
@Setter
@Document(indexName = "news_index")
public class NewsEntity {

    @Id
    private String id;
    private String title;
    private String originallink;
    private String link;
    private String description;
    private String pubDate;

    @Builder
    public NewsEntity(String id, String title, String originallink, String link, String description, String pubDate) {
        this.id = id;
        this.title = title;
        this.originallink = originallink;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
    }
}
