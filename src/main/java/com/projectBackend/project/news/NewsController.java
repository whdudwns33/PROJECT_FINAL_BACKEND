package com.projectBackend.project.news;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor

public class NewsController {
    private final NewsService newsService;

    // 파이썬 데이터 저장
    @PostMapping("/save")
    public ResponseEntity<Boolean> saveNewsToEntity (@RequestBody List<NewsDto> newsDtos) {
        return ResponseEntity.ok(newsService.saveToElastic(newsDtos));
    }
}
