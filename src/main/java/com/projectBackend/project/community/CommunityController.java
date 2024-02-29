package com.projectBackend.project.community;

import com.projectBackend.project.utils.MultiDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "Community Controller", description = "종목 토론 관련 API")
public class CommunityController {
    private final CommunityService communityService;

    // 글 등록
    @PostMapping("/new")
    @ApiOperation(value = "토론 글 등록", notes = "토론 글 등록 API")
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
    @ApiOperation(value = "토론 글 추천", notes = "토론 글 추천 API")
    public ResponseEntity<Boolean> discussionLike(@PathVariable Long id, @RequestBody MultiDto multiDto) {
        return ResponseEntity.ok(communityService.likePost(id, multiDto));
    }

    // 비추천
    @PostMapping("/dislike/{id}")
    @ApiOperation(value = "토론 글 비추천", notes = "토론 글 비추천 API")
    public ResponseEntity<Boolean> discussionDislike(@PathVariable Long id, @RequestBody MultiDto multiDto) {
        return ResponseEntity.ok(communityService.dislikePost(id, multiDto));
    }

    // 통합 검색
    @GetMapping("/search")
    @ApiOperation(value = "토론 글 검색", notes = "토론 글 검색 API (내용 / 글쓴이)")
    public ResponseEntity<Page<CommunityDto>> discussionSearch(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "type", defaultValue = "content") String type,
            Pageable pageable) {
        return ResponseEntity.ok(communityService.search(keyword, type, pageable));
    }
}
