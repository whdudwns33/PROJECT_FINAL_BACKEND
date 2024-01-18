package com.projectBackend.project.crawling.crawlGold;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity
@Table(name = "crawlGold")
@NoArgsConstructor
public class CrawlGoldEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String CrawlGoldname;
    private String CrawlGoldunit;
    private String CrawlGoldprice;
    private String CrawlGoldyesterday;
    private String CrawlGoldrate;
    private String CrawlGolddate;

    @Builder
    public CrawlGoldEntity(Long id, String CrawlGoldname, String CrawlGoldunit,
                           String CrawlGoldprice, String CrawlGoldyesterday,
                           String CrawlGoldrate, String CrawlGolddate) {
        this.id = id;
        this.CrawlGoldname = CrawlGoldname;
        this.CrawlGoldunit = CrawlGoldunit;
        this.CrawlGoldprice = CrawlGoldprice;
        this.CrawlGoldyesterday = CrawlGoldyesterday;
        this.CrawlGoldrate = CrawlGoldrate;
        this.CrawlGolddate = CrawlGolddate;
    }
}
