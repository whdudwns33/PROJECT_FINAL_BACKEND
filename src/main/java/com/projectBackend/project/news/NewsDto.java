package com.projectBackend.project.news;

import com.projectBackend.project.constant.Authority;
import com.projectBackend.project.member.MemberDto;
import com.projectBackend.project.member.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@Builder
public class NewsDto {
    private String id;
    private String title;
    private String originalLink;
    private String link;
    private String description;
    private String pubDate;

    public static NewsDto of(NewsEntity news) {
        return NewsDto.builder()
                .title(news.getTitle())
                .originalLink(news.getOriginalLink())
                .link(news.getLink())
                .description(news.getDescription())
                .pubDate(news.getPubDate())
                .build();
    }

    public NewsEntity toEntity(NewsDto dto) {
        return NewsEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .originalLink(dto.getOriginalLink())
                .link(dto.getLink())
                .description(dto.getDescription())
                .pubDate(dto.getPubDate())
                .build();
    }
}
