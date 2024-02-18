package com.projectBackend.project.news.elastic;

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
    public ResponseEntity<Boolean> saveNewsToEntity (@RequestBody List<NewsDto> newsDtos) throws IOException {
        return ResponseEntity.ok(newsService.saveToElastic(newsDtos));
    }

    // 검색 엔진
    @GetMapping("/search")
    public ResponseEntity<List<NewsDto>> searchNews (@RequestParam String token) throws IOException {
        return ResponseEntity.ok(newsService.searchByTokenizer(token));
    }

    // 뉴스 데이터 가져오기
    @GetMapping("/getNews")
    private ResponseEntity<List<List<NewsDto>>> getNews () {
        return ResponseEntity.ok(newsService.getNewsPage());
    }
}
