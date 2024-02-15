package com.projectBackend.project.community;

import com.projectBackend.project.utils.MultiDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 종목 토론 페이지
@Slf4j
@RestController
@Setter
@Getter
@ToString
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityController {
    private final CommunityService communityService;

    // 글 등록
    @PostMapping("/new")
    public ResponseEntity<CommunityDto> discussionPost(@RequestBody MultiDto multiDto) {
        return ResponseEntity.ok(communityService.savePost(multiDto));
    }

//    // 전체 불러오기
//    @GetMapping("/list")
//    public ResponseEntity<Page<CommunityDto>> discussionList(Pageable pageable) {
//        return ResponseEntity.ok(communityService.getAllPosts(pageable));
//    }

//    // 조회수
//    @PostMapping("/view/{id}")
//    public ResponseEntity<Boolean> discussionView(@PathVariable Long id) {
//        return ResponseEntity.ok(communityService.incrementViews(id));
//    }

    // 추천
    @PostMapping("/like/{id}")
    public ResponseEntity<Boolean> discussionLike(@PathVariable Long id, @RequestBody MultiDto multiDto) {
        return ResponseEntity.ok(communityService.likePost(id, multiDto));
    }

    // 비추천
    @PostMapping("/dislike/{id}")
    public ResponseEntity<Boolean> discussionDislike(@PathVariable Long id, @RequestBody MultiDto multiDto) {
        return ResponseEntity.ok(communityService.dislikePost(id, multiDto));
    }

    // 통합 검색
    @GetMapping("/search")
    public ResponseEntity<Page<CommunityDto>> discussionSearch(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "type", defaultValue = "content") String type,
            Pageable pageable) {
        return ResponseEntity.ok(communityService.search(keyword, type, pageable));
    }
}
