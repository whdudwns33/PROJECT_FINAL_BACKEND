package com.projectBackend.project.stock.jdbc;

import com.projectBackend.project.stock.StockDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.Table;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class StockJdbcBatchService {

    private final JdbcTemplate jdbcTemplate;

    public StockJdbcBatchService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsert(List<StockDto> stockDtoList, Class<?> entityClass) {
        log.info("batchInsert");

        String tableName = getTableName(entityClass);

        String sql = "INSERT INTO " + tableName + " (stock_open, stock_high, stock_low, stock_close, " +
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

    public void batchUpdate(List<StockDto> stockDtoList, Class<?> entityClass) {
        log.info("batchUpdate");

        executeDelete(entityClass);
        batchInsert(stockDtoList, entityClass);

    }

    private void executeDelete(Class<?> entityClass) {
        String tableName = getTableName(entityClass);

        // Delete all existing data
        String deleteSql = "DELETE FROM " + tableName;
        jdbcTemplate.update(deleteSql);
    }

    private String getTableName(Class<?> entityClass) {
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        if (tableAnnotation != null) {
            return tableAnnotation.name();
        } else {
            throw new IllegalArgumentException("Entity class is missing @Table annotation.");
        }
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
