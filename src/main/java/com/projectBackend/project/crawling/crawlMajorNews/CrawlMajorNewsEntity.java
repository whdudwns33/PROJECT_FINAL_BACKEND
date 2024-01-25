package com.projectBackend.project.crawling.crawlMajorNews;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity
@Table(name = "crawlMajorNews")
@NoArgsConstructor
public class CrawlMajorNewsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String CrawlMajorNewsName;
    private String CrawlMajorNewsLink;
    private String CrawlMajorNewsSummary;
    private String CrawlMajorNewsMedia;
    private String CrawlMajorNewsDate;



    @Builder
    public CrawlMajorNewsEntity(Long id, String CrawlMajorNewsName, String CrawlMajorNewsLink,
                                String CrawlMajorNewsSummary, String CrawlMajorNewsMedia, String CrawlMajorNewsDate) {
        this.id = id;
        this.CrawlMajorNewsName = CrawlMajorNewsName;
        this.CrawlMajorNewsLink = CrawlMajorNewsLink;
        this.CrawlMajorNewsSummary = CrawlMajorNewsSummary;
        this.CrawlMajorNewsMedia = CrawlMajorNewsMedia;
        this.CrawlMajorNewsDate = CrawlMajorNewsDate;
    }
}
