package xyz.notanipgrabber.chunkgiver;

import java.io.File;
import java.sql.*;

public class DatabaseManager {
    private Connection connection;

    public void connect() {
        try {
            File dataFolder = new File("plugins/chunkGiver");
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder.getAbsolutePath() + "/data.db");
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS players (uuid TEXT PRIMARY KEY, sound BOOLEAN, message BOOLEAN)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        }
    }

    public void savePlayerData(String uuid, boolean sound, boolean message) {
        String query = "INSERT OR REPLACE INTO players (uuid, sound, message) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid);
            statement.setBoolean(2, sound);
            statement.setBoolean(3, message);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPlayerSoundSetting(String uuid) {
        String query = "SELECT sound FROM players WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean("sound");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Default value if player not found
    }

    public boolean getPlayerMessageSetting(String uuid) {
        String query = "SELECT message FROM players WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean("message");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Default value if player not found
    }

    public void setPlayerSoundSetting(String uuid, boolean sound) {
        String query = "UPDATE players SET sound = ? WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, sound);
            statement.setString(2, uuid);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerMessageSetting(String uuid, boolean message) {
        String query = "UPDATE players SET message = ? WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, message);
            statement.setString(2, uuid);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
