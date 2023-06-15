package com.desticube.util;

import java.sql.*;

/*
 https://github.com/Spigot-Plugin-Development-Tutorial/plugin-with-mysql-db/blob/master/src/main/java/me/kodysimpson/stattracker/db/Database.java
 */
public class DatabaseUtil {

    private Connection connection;

    public Connection getConnection() throws SQLException {

        if (connection != null) {
            return connection;
        }

        //Try to connect to my MySQL database running locally
        String url = "jdbc:mysql://localhost/stat_tracker";
        String user = "root";
        String password = "";

        Connection connection = DriverManager.getConnection(url, user, password);

        this.connection = connection;

        System.out.println("Connected to database.");

        return connection;
    }

    public void initializeDatabase() throws SQLException {

        Statement statement = getConnection().createStatement();

        //Create the player_stats table
        String sql = "CREATE TABLE IF NOT EXISTS PLayerInventory (" +
                "ID varchar(36) PRIMARY KEY, " +
                "PlayerUUID varchar(36) NOT NULL, " +
                "WORLDUUID varchar(36) NOT NULL, " +
                "BackUpDate DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "EventType varchar(36) NOT NULL, " +
                "EventTypeCause varchar(36), " +
                "Inventory LONGTEXT NOT NULL)";

        statement.execute(sql);
        statement.close();

    }

/*    public findPlayerBackUpsByUUID(String uuid) throws SQLException {

        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM player_stats WHERE uuid = ?");
        statement.setString(1, uuid);

        ResultSet resultSet = statement.executeQuery();

        PlayerStats playerStats;

        if (resultSet.next()) {

            playerStats = new PlayerStats(resultSet.getString("uuid"), resultSet.getInt("deaths"), resultSet.getInt("kills"), resultSet.getLong("blocks_broken"), resultSet.getDouble("balance"), resultSet.getDate("last_login"), resultSet.getDate("last_logout"));

            statement.close();

            return playerStats;
        }

        statement.close();

        return null;
    }*/


}
