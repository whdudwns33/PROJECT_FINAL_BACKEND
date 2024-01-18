package com.projectBackend.project.crawlStock;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Setter
@Getter
@ToString
@RequiredArgsConstructor
@RequestMapping("/CrawlStock")
public class CrawlStockController {

//    @GetMapping("/getStock")
//    public ResponseEntity<List<CrawlStockDto>> getStock() {
//        return ResponseEntity.ok()
//    }

}
