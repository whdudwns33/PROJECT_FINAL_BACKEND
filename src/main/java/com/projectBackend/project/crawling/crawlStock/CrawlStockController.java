package com.projectBackend.project.crawling.crawlStock;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
