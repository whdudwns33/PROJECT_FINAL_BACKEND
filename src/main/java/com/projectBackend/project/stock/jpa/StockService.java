    package com.projectBackend.project.stock.jpa;

    import com.projectBackend.project.stock.StockChartReqDto;
    import com.projectBackend.project.stock.StockDto;
    import com.projectBackend.project.stock.jdbc.StockJdbcBatchService;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.http.HttpEntity;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Service;
    import org.springframework.web.client.RestTemplate;
    import org.springframework.web.util.UriComponentsBuilder;
    import org.springframework.web.util.UriUtils;

    import javax.transaction.Transactional;
    import java.nio.charset.StandardCharsets;
    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;
    import java.util.*;

    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.util.Date;


    @Service
    @Slf4j
    @Transactional
    @RequiredArgsConstructor
    public class StockService {
        private final StockRepository stockRepository;
        private final RestTemplate restTemplate;
        private final RecentStockRepository recentStockRepository;
        private final StockJdbcBatchService stockJdbcBatchService;

        public List<StockEntity> getStockByName(String stockName) {
            return stockRepository.findByStockNameContainingIgnoreCase(stockName);
        }
        public void batchInsertOrUpdate(Map<String, List<StockDto>> stockDataMap) {
            // Get the current year and month
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
            String currentYearMonth = currentDate.format(formatter);

            for (Map.Entry<String, List<StockDto>> entry : stockDataMap.entrySet()) {

                String yearMonth = entry.getKey();
                List<StockDto> stockDtoList = entry.getValue();

                // 들어온 데이터의 key가 현재 년월인지?
                if (currentYearMonth.equals(yearMonth)) {
                    // DB안에 현재 년월에 해당하는 데이터가 존재하는지?
                    if (existsData()) {
                        // 존재하면 recent_stock에 update
                        stockJdbcBatchService.batchUpdate(stockDtoList, RecentStockEntity.class);
                    } else {
                        // 존재하지 않으면 recent_stock에 insert
                        stockJdbcBatchService.batchInsert(stockDtoList, RecentStockEntity.class);
                    }
                } else {
                    // 과거 년월 데이터이면 stock에 insert
                    stockJdbcBatchService.batchInsert(stockDtoList, StockEntity.class);
                }
            }
        }

        private boolean existsData() {
            // 해당 년월에 해당하는 데이터가 존재하는지 여부를 체크하는 쿼리
            return recentStockRepository.count() > 0;
        }


        // 조영준 : 주식 리스트 출력
        public List<StockDto> getStockList(String type) throws ParseException {
            Date date = new Date();

            // Date date = dateFormat.parse(formattedDate);
            // 무한 스크롤을 위한 임시 200개 세팅
            Pageable pageable = PageRequest.of(0, 200);
            log.info("today_date : {}", date);
            log.info("type : {}", type);
            List<StockDto> stockDtoList = new ArrayList<>();
            List<RecentStockEntity> stockEntities;
            switch (type) {
                case "고가":
                    stockEntities = recentStockRepository.findTop200ByOrderByStockHighDesc(date, pageable);
                    break;
                case "EPS":
                    stockEntities = recentStockRepository.findTop200ByOrderByStockEpsDesc(date, pageable);
                    break;
                case "PER":
                    stockEntities = recentStockRepository.findTop200ByOrderByStockPerAsc(date, pageable);
                    break;
                case "DIV":
                    stockEntities = recentStockRepository.findTop200ByOrderByStockDivDesc(date, pageable);
                    break;
                default:
                    log.info("잘못된 접근입니다.");
                    return stockDtoList;
            }

            for (RecentStockEntity recentStockEntity : stockEntities) {
                StockDto stockDto = createStockDtoFromEntity(recentStockEntity);
                stockDtoList.add(stockDto);
            }

            return stockDtoList;
        }

        // Entity to Dto
        private StockDto createStockDtoFromEntity(RecentStockEntity recentStockEntity) {
            StockDto stockDto = new StockDto();
            stockDto.setOpen(recentStockEntity.getStockOpen());
            stockDto.setHigh(recentStockEntity.getStockHigh());
            stockDto.setLow(recentStockEntity.getStockLow());
            stockDto.setClose(recentStockEntity.getStockClose());
            stockDto.setVolume(recentStockEntity.getStockVolume());
            stockDto.setTradingValue(recentStockEntity.getStockTradingValue());
            stockDto.setFluctuationRate(recentStockEntity.getStockFluctuationRate());
            stockDto.setDate(recentStockEntity.getStockDate());
            stockDto.setStockCode(recentStockEntity.getStockCode());
            stockDto.setStockName(recentStockEntity.getStockName());
            stockDto.setBps(recentStockEntity.getStockBps());
            stockDto.setPer(recentStockEntity.getStockPer());
            stockDto.setPbr(recentStockEntity.getStockPbr());
            stockDto.setEps(recentStockEntity.getStockEps());
            stockDto.setDiv(recentStockEntity.getStockDiv());
            stockDto.setDps(recentStockEntity.getStockDps());

            return stockDto;
        }

        public String getChartData(StockChartReqDto stockChartReqDto) {
            log.info("stockName=========================================={}",stockChartReqDto.getStockName());
            log.info(stockChartReqDto.getColumnType());
            log.info(String.valueOf(stockChartReqDto.getFutureDays()));
            log.info(String.valueOf(stockChartReqDto.getMonths()));

            Optional<RecentStockEntity> stockEntityOptional = recentStockRepository.findFirstStockByStockNameOrderByStockDateDesc(stockChartReqDto.getStockName());

            // stockEntityOptional의 값이 존재하면 stockCode를 가져옴, 아니면 null
            String stockCode = stockEntityOptional.map(RecentStockEntity::getStockCode).orElse(null);

            // stockChartDtd.getMonths() 로 시작일과 종료일 계산
            int months = stockChartReqDto.getMonths();
            LocalDate currentDate = LocalDate.now();
            LocalDate firstDate = currentDate.minusMonths(months);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
            String endDate = currentDate.format(formatter);
            String startDate = firstDate.format(formatter);
            log.info(endDate, startDate);

            String columnType = stockChartReqDto.getColumnType();
            int futureDays = stockChartReqDto.getFutureDays();

            return sendArimaRequest(stockCode, startDate, endDate, columnType, futureDays);
        }

        public String sendArimaRequest(String stockCode, String startDate, String endDate, String columnType, int futureDays) {
            String arimaEndpoint = "http://localhost:5000/python/arima";

            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("stock_code", stockCode);
            requestBody.put("start_date", startDate);
            requestBody.put("end_date", endDate);
            requestBody.put("column_type", columnType);
            requestBody.put("future_days", futureDays);

            // Create HttpEntity with headers and body
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // URL 확인
            log.info("URL: " + arimaEndpoint);

            // Flask 서버에 POST 요청 보내기
            ResponseEntity<String> response = restTemplate.postForEntity(arimaEndpoint, requestEntity, String.class);

            // 응답 결과 처리 (예를 들어 로그에 출력)
            System.out.println("Flask 서버 응답: " + response.getBody());
            return response.getBody();
        }

        public String getLstmData(StockChartReqDto stockChartReqDto) {
            log.info("stockName=========================================={}",stockChartReqDto.getStockName());
            log.info(stockChartReqDto.getColumnType());
            log.info(String.valueOf(stockChartReqDto.getFutureDays()));
            log.info(String.valueOf(stockChartReqDto.getMonths()));

            Optional<RecentStockEntity> stockEntityOptional = recentStockRepository.findFirstStockByStockNameOrderByStockDateDesc(stockChartReqDto.getStockName());

            // stockEntityOptional의 값이 존재하면 stockCode를 가져옴, 아니면 null
            String stockCode = stockEntityOptional.map(RecentStockEntity::getStockCode).orElse(null);

            // stockChartDtd.getMonths() 로 시작일과 종료일 계산
            int months = stockChartReqDto.getMonths();
            LocalDate currentDate = LocalDate.now();
            LocalDate firstDate = currentDate.minusMonths(months);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
            String endDate = currentDate.format(formatter);
            String startDate = firstDate.format(formatter);
            log.info(endDate, startDate);

            String columnType = stockChartReqDto.getColumnType();
            int futureDays = stockChartReqDto.getFutureDays();

            return sendLstmRequest(stockCode, startDate, endDate, columnType, futureDays);
        }

        public String sendLstmRequest(String stockCode, String startDate, String endDate, String columnType, int futureDays) {
            String arimaEndpoint = "http://localhost:5001/python/lstm";

            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("stock_code", stockCode);
            requestBody.put("start_date", startDate);
            requestBody.put("end_date", endDate);
            requestBody.put("column_type", columnType);
            requestBody.put("future_days", futureDays);

            // Create HttpEntity with headers and body
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // URL 확인
            log.info("URL: " + arimaEndpoint);

            // Flask 서버에 POST 요청 보내기
            ResponseEntity<String> response = restTemplate.postForEntity(arimaEndpoint, requestEntity, String.class);

            // 응답 결과 처리 (예를 들어 로그에 출력)
            System.out.println("Flask response: " + response.getBody());
            return response.getBody();
        }


    }