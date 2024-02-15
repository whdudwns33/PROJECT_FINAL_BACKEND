package com.projectBackend.project.community;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "community")
public class CommunityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String authorId;

    @Column(nullable = false)
    private int views;

    @Column(nullable = false)
    @ElementCollection
    private List<String> likes;

    @Column(nullable = false)
    @ElementCollection
    private List<String> dislikes;

    @Builder
    public CommunityEntity(String content, String authorId) {
        this.content = content;
        this.authorId = authorId;
        this.date = LocalDateTime.now();
        this.views = 0;
        this.likes = new ArrayList<>();
        this.dislikes = new ArrayList<>();
    }

    // 추가된 메서드
    public void addLike(String nickname) {

//        likes.add(nickname);
        if (!likes.contains(nickname)) {
            likes.add(nickname);
        }
    }

    public void addDislike(String nickname) {

//        dislikes.add(nickname);
        if (!dislikes.contains(nickname)) {
            dislikes.add(nickname);
        }
    }
}
