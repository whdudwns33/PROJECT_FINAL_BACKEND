package com.projectBackend.project.news.elastic;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Getter
@Setter
@Document(indexName = "news_index")
public class NewsEntity {

    @Id
    private String id;

    private String title;
    @Field(type = FieldType.Keyword)
    private String originalLink;
    @Field(type = FieldType.Text)
    private String link;
    private String description;
    private String pubDate;

    @Builder
    public NewsEntity(String id, String title, String originalLink, String link, String description, String pubDate) {
        this.id = id;
        this.title = title;
        this.originalLink = originalLink;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
    }
}
