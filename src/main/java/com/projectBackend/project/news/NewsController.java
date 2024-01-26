package com.projectBackend.project.news;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    // 검색 엔진
    @GetMapping("/search")
    public ResponseEntity<List<NewsDto>> searchNews (@RequestParam List<String> fields, String token) throws IOException {
        return ResponseEntity.ok(newsService.searchByTokenizer(fields, token));
    }
}
