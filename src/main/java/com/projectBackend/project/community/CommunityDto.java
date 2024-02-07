package com.projectBackend.project.community;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@Getter
@Setter
public class CommunityDto {
    Long id;
    String content;
    LocalDateTime date;
    String authorId;
    int views;
    List<String> likes;
    List<String> dislikes;


    // dto를 entity로
    public CommunityEntity toEntity() {
        return CommunityEntity.builder()
                .content(this.content)
                .authorId(this.authorId)
                .build();
    }

    // entity를 dto로
    public static CommunityDto of(CommunityEntity discussion) {
        return CommunityDto.builder()
                .id(discussion.getId())
                .content(discussion.getContent())
                .date(discussion.getDate())
                .authorId(discussion.getAuthorId())
                .views(discussion.getViews())
                .likes(discussion.getLikes())
                .dislikes(discussion.getDislikes())
                .build();
    }
}
