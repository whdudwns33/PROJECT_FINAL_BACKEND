package com.projectBackend.project.news.elastic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Api(tags = "News Controller", description = "뉴스 관련 API")

public class NewsController {
    private final NewsService newsService;

    // 파이썬 데이터 저장
    @PostMapping("/save")
    @ApiOperation(value = "파이썬 데이터 저장", notes = "파이썬 데이터 저장 API")
    public ResponseEntity<Boolean> saveNewsToEntity (@RequestBody List<NewsDto> newsDtos) throws IOException {
        return ResponseEntity.ok(newsService.saveToElastic(newsDtos));
    }

    // 검색 엔진
    @GetMapping("/search")
    @ApiOperation(value = "뉴스 검색", notes = "뉴스 검색 API")
    public ResponseEntity<List<NewsDto>> searchNews (@RequestParam String token) throws IOException {
        return ResponseEntity.ok(newsService.searchByTokenizer(token));
    }

    // 뉴스 데이터 가져오기
    @GetMapping("/getNews")
    @ApiOperation(value = "뉴스 데이터 가져오기", notes = "뉴스 데이터 가져오기 API")
    private ResponseEntity<List<List<NewsDto>>> getNews () {
        return ResponseEntity.ok(newsService.getNewsPage());
    }
}
