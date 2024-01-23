package com.projectBackend.project.stock.jdbc;



import com.projectBackend.project.stock.StockDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class StockJdbcBulkInsert {
    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;


    public void bulkInsert(List<StockDto> stockDtoList) {

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            connection.setAutoCommit(false);  // 트랜잭션 시작

            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO stock (stock_open, stock_high, stock_low, stock_close, stock_volume, " +
                            "stock_trading_value, stock_fluctuation_rate, stock_date, stock_code, stock_name, " +
                            "stock_bps, stock_per, stock_pbr, stock_eps, stock_div, stock_dps) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {


                for (StockDto stockDto : stockDtoList) {
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

                    preparedStatement.addBatch();
                }

                int[] affectedRows = preparedStatement.executeBatch();
                connection.commit();  // 트랜잭션 커밋

            } catch (SQLException e) {
                connection.rollback();  // 트랜잭션 롤백
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
