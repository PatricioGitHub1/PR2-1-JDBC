package cat.iesesteveterradas;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ManagerDB {
    public static Connection connect (String filePath) {
        Connection conn = null;
        
        try {
            String url = "jdbc:sqlite:" + filePath;
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("BBDD driver: " + meta.getDriverName());
            }
            System.out.println("BBDD SQLite connectada");
        } catch (SQLException e) { e.printStackTrace(); }

        return conn;
    }

    public static void disconnect (Connection conn ) {
        try {
            if (conn != null) { 
                conn.close(); 
                System.out.println("DDBB SQLite desconnectada");
            }
        } catch (SQLException ex) { System.out.println(ex.getMessage()); }
    }

    public static int queryUpdate (Connection conn, String sql) {
        int result = 0;
        try {
            Statement stmt = conn.createStatement();
            result = stmt.executeUpdate(sql);
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    public static void queryInsertFaction (Connection conn, String name, String summary) {
        String query = String.format("insert into Factions(name, summary) values(\'%s\', \'%s\');", name, summary);
        queryUpdate(conn, query);
    }

    public static void queryInsertCharacters (Connection conn, String name, double damage, double defense, int idFaction) {
        String query = String.format("insert into Characters(name, damage, defense, idFaccio) values(\'%s\', %s, %s, %s)", name, damage, defense, idFaction);
        queryUpdate(conn, query);
    }

    public static ResultSet querySelect (Connection conn, String sql) {
        ResultSet rs = null;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) { e.printStackTrace(); }
        return rs;
    }
}
