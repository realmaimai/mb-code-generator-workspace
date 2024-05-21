package com.maimai.builders;

import com.maimai.utils.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Objects;

// TODO change this class to DatabaseUtils
@Slf4j
public class BuildTable {
    private static Connection connection = null;
    private static final String SQL_SHOW_TABLE_STATUS = "show table status";

    static {
        String driverName = PropertiesUtils.getString("db", "driver", "name");
        String url = PropertiesUtils.getString("db", "url");
        String user = PropertiesUtils.getString("db", "username");
        String password = PropertiesUtils.getString("db", "password");
        try {
            // connect to db via jdbc
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            log.info("error when connecting to db: " + e);
        }
    }

    public static void getTables() {
        PreparedStatement ps = null;
        ResultSet tableResult = null;
        try {
            ps = connection.prepareStatement(SQL_SHOW_TABLE_STATUS);
            tableResult = ps.executeQuery();
            while (tableResult.next()) {
                String tableName = tableResult.getString("name");
                String comment = tableResult.getString("comment");
                log.info("table name: {}, comment: {}", tableName, comment);
            }
        } catch (Exception e) {
            log.info("get table from connection error: " + e);
        } finally {
            if (!Objects.isNull(ps)) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.info("prepare statement error: " + e);
                }
            }
            if (!Objects.isNull(tableResult)) {
                try {
                    tableResult.close();
                } catch (SQLException e) {
                    log.info("result set error: " + e);
                }
            }
            if (!Objects.isNull(connection)) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.info("connection error: " + e);
                }
            }

        }
    }
}
