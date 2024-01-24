package com.projectBackend.project.stock.jdbc;

import com.projectBackend.project.stock.StockDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class StockJdbcBatchService {

    private final JdbcTemplate jdbcTemplate;

    public StockJdbcBatchService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsert(List<StockDto> stockDtoList) {
        log.info("batchInsert");
        String sql = "INSERT INTO stock (stock_open, stock_high, stock_low, stock_close, " +
                "stock_volume, stock_trading_value, stock_fluctuation_rate, stock_date, stock_code, " +
                "stock_name, stock_bps, stock_per, stock_pbr, stock_eps, stock_div, stock_dps) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                StockDto stockDto = stockDtoList.get(i);
                setPreparedStatementParameters(preparedStatement, stockDto);
            }

            @Override
            public int getBatchSize() {
                return stockDtoList.size();
            }
        });
    }

    public void batchUpdate(List<StockDto> stockDtoList) {
        log.info("batchUpdate");

        int batchSize = 10000; // 한 달 치 데이터를 10000개씩 분리
        int totalSize = stockDtoList.size();

        for (int fromIndex = 0; fromIndex < totalSize; fromIndex += batchSize) {
            int toIndex = Math.min(fromIndex + batchSize, totalSize);

            List<StockDto> subList = stockDtoList.subList(fromIndex, toIndex);
            executeBatchUpdate(subList);
        }
    }

    private void executeBatchUpdate(List<StockDto> batch) {
        String sql = "UPDATE stock SET stock_open=?, stock_high=?, stock_low=?, " +
                "stock_close=?, stock_volume=?, stock_trading_value=?, " +
                "stock_fluctuation_rate=?, stock_date=?, stock_code=?, stock_name=?, " +
                "stock_bps=?, stock_per=?, stock_pbr=?, stock_eps=?, " +
                "stock_div=?, stock_dps=? WHERE stock_code=? AND stock_date=?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                StockDto stockDto = batch.get(i);
                setPreparedStatementParameters(preparedStatement, stockDto);
                preparedStatement.setString(17, stockDto.getStockCode());
                preparedStatement.setString(18, stockDto.getDate());
            }

            @Override
            public int getBatchSize() {
                return 100; // 분리한 데이터 덩어리를 쪼개서 Batch Update
            }
        });
    }


    private void setPreparedStatementParameters(PreparedStatement preparedStatement, StockDto stockDto)
            throws SQLException {
        preparedStatement.setString(1, stockDto.getOpen());
        preparedStatement.setString(2, stockDto.getHigh());
        preparedStatement.setString(3, stockDto.getLow());
        preparedStatement.setString(4, stockDto.getClose());
        preparedStatement.setString(5, stockDto.getVolume());
        preparedStatement.setString(6, stockDto.getTradingValue());
        preparedStatement.setString(7, stockDto.getFluctuationRate());
        preparedStatement.setString(8, stockDto.getDate());
        preparedStatement.setString(9, stockDto.getStockCode());
        preparedStatement.setString(10, stockDto.getStockName());
        preparedStatement.setString(11, stockDto.getBps());
        preparedStatement.setString(12, stockDto.getPer());
        preparedStatement.setString(13, stockDto.getPbr());
        preparedStatement.setString(14, stockDto.getEps());
        preparedStatement.setString(15, stockDto.getDiv());
        preparedStatement.setString(16, stockDto.getDps());
    }

}
