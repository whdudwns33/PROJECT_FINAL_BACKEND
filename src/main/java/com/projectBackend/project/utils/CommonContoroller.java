package com.projectBackend.project.utils;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonContoroller {
    private final CommonService commonService;

    @GetMapping("/index")
    public ResponseEntity<MultiDto> stockIndex() {
        return ResponseEntity.ok(commonService.getIndex());
    }

    @GetMapping("/main")
    public ResponseEntity<MultiDto> stockMain() {
        return ResponseEntity.ok(commonService.getMainData());
    }

}