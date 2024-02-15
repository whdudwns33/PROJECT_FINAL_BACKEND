package com.projectBackend.project.admin;

import com.projectBackend.project.member.MemberDto;
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

import java.util.Map;

@Slf4j
@RestController
@Setter
@Getter
@ToString
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/search")
    public ResponseEntity<Page<MemberDto>> search(@RequestParam String keyword, Pageable pageable) {
        Page<MemberDto> searchResults = adminService.searchMembers(keyword, pageable);
        return ResponseEntity.ok(searchResults);
    }

    // 구매 이력 조회
    @PostMapping("/getUserBuy")
    public ResponseEntity<MultiDto> getBuyInfo(@RequestBody MultiDto multiDto) {
        return ResponseEntity.ok(adminService.getUserBuyData(multiDto));
    }
}
