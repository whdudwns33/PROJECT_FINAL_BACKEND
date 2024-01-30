package com.projectBackend.project.stock.jdbc;

import com.projectBackend.project.stock.StockDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.Table;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
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
        preparedStatement.setLong(1, stockDto.getOpen());
        preparedStatement.setLong(2, stockDto.getHigh());
        preparedStatement.setLong(3, stockDto.getLow());
        preparedStatement.setLong(4, stockDto.getClose());
        preparedStatement.setLong(5, stockDto.getVolume());
        preparedStatement.setLong(6, stockDto.getTradingValue());
        preparedStatement.setDouble(7, stockDto.getFluctuationRate());
        preparedStatement.setDate(8, new java.sql.Date(stockDto.getDate().getTime()));
        preparedStatement.setString(9, stockDto.getStockCode());
        preparedStatement.setString(10, stockDto.getStockName());

        // Double 값이 null이 아닌 경우에만 setDouble을 호출하고, null인 경우 setNull을 호출합니다.
        setNullableDouble(11, preparedStatement, stockDto.getBps());
        setNullableDouble(12, preparedStatement, stockDto.getPer());
        setNullableDouble(13, preparedStatement, stockDto.getPbr());
        setNullableDouble(14, preparedStatement, stockDto.getEps());
        setNullableDouble(15, preparedStatement, stockDto.getDiv());
        setNullableDouble(16, preparedStatement, stockDto.getDps());
    }

    // 보조 메서드: nullable한 Double 값 처리
    private void setNullableDouble(int parameterIndex, PreparedStatement preparedStatement, Double value) throws SQLException {
        if (value != null) {
            preparedStatement.setDouble(parameterIndex, value);
        } else {
            preparedStatement.setNull(parameterIndex, Types.DOUBLE);
        }
    }

}
