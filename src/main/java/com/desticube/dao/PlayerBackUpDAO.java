package com.desticube.dao;

import com.desticube.DestiRollBack;
import com.desticube.entity.PlayerBackUp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class PlayerBackUpDAO {
//    private static ConfigUtil langFile  = new ConfigUtil(DestiRollBack.getPlugin().getDataFolder().getAbsolutePath() + "/" + "config.yml");

    private static ResultSet rs;
    private static Connection connection = null;
    private static final Logger logger = LoggerFactory.getLogger(PlayerBackUpDAO.class);

    public PlayerBackUpDAO() {
    }

    public PlayerBackUpDAO(DestiRollBack plugin) {
        String userName = plugin.getConfig().getString("database.username");
        String password = plugin.getConfig().getString("database.password");
        String host = plugin.getConfig().getString("database.host");
        String port = plugin.getConfig().getString("database.port");
        String database = plugin.getConfig().getString("database.database");
        boolean useSSL = plugin.getConfig().getBoolean("database.use-ssl");
        String connectionString;
        if (useSSL) {
            connectionString = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=true";
        } else {
            connectionString = "jdbc:mysql://" + host + ":" + port + "/" + database;
        }
        try {
            connection = DriverManager.getConnection(connectionString, userName, password);
            System.out.println("Connection Made");
        } catch (SQLException e) {
            logger.error(e.toString());
        }
    }

    public static synchronized Future<ArrayList<PlayerBackUp>> selectbyPlayerName(String playerUUID) {
        return CompletableFuture.supplyAsync(() -> {
            String selectedPlayerUUID = "%" + playerUUID + "%";
            PreparedStatement stmt;
            ArrayList<PlayerBackUp> PlayerBackUps = new ArrayList();


            try {
                String query = "SELECT * FROM DestiRollBack"
                               + "WHERE playersUUID LIKE ?";
                stmt = connection.prepareStatement(query);
                stmt.setString(1, selectedPlayerUUID);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    PlayerBackUp playerBackUp = new PlayerBackUp();

                    playerBackUp.setId(String.valueOf(rs.getInt("id")));
                    playerBackUp.setPlayersUUID(rs.getString("playerUUID"));
                    playerBackUp.setBackUpDate(rs.getString("backUpDate"));
                    playerBackUp.setEventType(rs.getString("eventType"));
                    playerBackUp.setWorldUUID(rs.getString("worldUUID"));
                    playerBackUp.setPlayersXP(rs.getFloat("playersXP"));
                    playerBackUp.setEventTypeCause(rs.getString("eventTypeCause"));
                    playerBackUp.setInventory(rs.getString("inventory"));

                    PlayerBackUps.add(playerBackUp);
                }
            } catch (SQLException e) {
                logger.error(e.toString());
            }
            logger.info("Found orders:  " + PlayerBackUps.size());
            return PlayerBackUps;
        });
    }

    public synchronized void backupInventory(PlayerBackUp pbp) {
        CompletableFuture.runAsync(() -> {

            String sql = "INSERT INTO DestiRollBack(playersUUID, backUpDate,eventType,worldUUID, playersXP, eventTypeCause, inventory) VALUES("+pbp.getPlayersUUID()+","+ pbp.getBackUpDate()+","+pbp.getEventType()+","+pbp.getWorldUUID()+","++""+")";
            System.out.println(sql);
            try {
                Statement stmt = connection.createStatement();
                stmt.executeUpdate(sql);
                return true;
            } catch (SQLException e) {
                logSQLError("Something went wrong.", e);
            }
            //Create the player_stats table
            try {
                Statement stmt = connection.createStatement();
                stmt.executeUpdate(sql);
                System.out.println("Database Created!");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
    }

    public static synchronized void connect() {
        DestiRollBack plugin = DestiRollBack.getPlugin();
        String userName = plugin.getConfig().getString("database.username");
        String password = plugin.getConfig().getString("database.password");
        String host = plugin.getConfig().getString("database.host");
        String port = plugin.getConfig().getString("database.port");
        String database = plugin.getConfig().getString("database.database");
        boolean useSSL = plugin.getConfig().getBoolean("database.use-ssl");
        String connectionString;
        if (useSSL) {
            connectionString = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=true";
        } else {
            connectionString = "jdbc:mysql://" + host + ":" + port + "/" + database;
        }
        try {
            connection = DriverManager.getConnection(connectionString, userName, password);
            System.out.println("Database Connected!");
        } catch (SQLException e) {
            logger.error(e.toString());
        }

    }

    public static synchronized void initDB() {
        CompletableFuture.runAsync(() -> {
            String sql = """
                    CREATE TABLE IF NOT EXISTS DestiRollBack
                    (
                        id             INT PRIMARY KEY AUTO_INCREMENT,
                        playerUUID    VARCHAR(255),
                        backUpDate     VARCHAR(255),
                        eventType      VARCHAR(255),
                        worldUUID      VARCHAR(255),
                        playersXP      FLOAT,
                        eventTypeCause VARCHAR(255),
                        inventory      LONGTEXT
                    );

                    """;

            //Create the player_stats table
            try {
                Statement stmt = connection.createStatement();
                stmt.executeUpdate(sql);
                System.out.println("Database Created!");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
    }


    public static synchronized void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.toString());
            }
        }
    }
}