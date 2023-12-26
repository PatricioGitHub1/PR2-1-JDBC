package cat.iesesteveterradas;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        String filePath = obtenirPathFitxer().toString();
        Connection conn;

        try {
            // Create database if needed and simulate loading
            File dbFile = new File(filePath);

            System.out.print("Loading");
            Thread.sleep(500);
            System.out.print(" . ");
            Thread.sleep(500);
            System.out.print(" . ");

            if (!dbFile.exists()) {
                conn = initDB(filePath);
            } else {
                String url = "jdbc:sqlite:" + filePath;
                conn = DriverManager.getConnection(url);
            }

            Thread.sleep(500);
            System.out.print(" . \n");
            
            MenuOptions menuOptions = new MenuOptions(conn);
            menuOptions.showMenu();

        } catch (SQLException sqle) {
            System.out.println("An SQL Exception took place: " + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("An exception took place while reading the document: " + e.getMessage());
        } 
    }
     
    public static Path obtenirPathFitxer() {
        return Paths.get(System.getProperty("user.dir"), "data", "forHonor.db");
    }

    public static Connection initDB(String dbpath) {
        try {
            // Creation of the Database
            String url = "jdbc:sqlite:" + dbpath;
            Connection conn = DriverManager.getConnection(url);
            
            // Drop tables if they already exist
            ManagerDB.queryUpdate(conn, "DROP TABLE IF EXISTS Factions;");
            ManagerDB.queryUpdate(conn, "DROP TABLE IF EXISTS Characters;");

            // Populate databse with two tables
            ManagerDB.queryUpdate(conn, "CREATE TABLE IF NOT EXISTS Factions ("
                                    + "	id integer PRIMARY KEY AUTOINCREMENT,"
                                    + "	name VARCHAR(15) NOT NULL,"
                                    + " summary VARCHAR(500) NOT NULL);");
            
            ManagerDB.queryUpdate(conn, "CREATE TABLE IF NOT EXISTS Characters ("
                                    + "	id integer PRIMARY KEY AUTOINCREMENT,"
                                    + "	name VARCHAR(15) NOT NULL,"
                                    + " damage REAL,"
                                    + " defense REAL,"
                                    + " idFaccio int NOT NULL);");
            
            // Insert rows into Factions
            ManagerDB.queryInsertFaction(conn, "Samurais", "The Samurai of the Dawn Empire come from a land, far to the East and they tell a tale of a homeland and an Emperor that were lost to sea and fire during the great cataclysm. Now, more than a millennia later the nomadic warriors roam no more and have rebuilt themselves in a new land with a new Emperor. After having been driven from their ancestral homes and rebuilding their forces, though strong and mighty, the Samurai still find themselves vastly outnumbered by their new neighbors.");
            ManagerDB.queryInsertFaction(conn, "Wu Lin", "The faction known as the Wu Lin is made up of warriors from ancient China, who now travel west seeking vengeance from war, betrayal, and personal tragedies. After The Cataclysm, China descended into hundreds of years of civil war. Of all the factions partaking in the civil war, the Wu Lin have begun to rise to the top. But even they have begun to splinter after so many years of conflict. Many of their warriors have begun to travel west, each with their own reason.");
            ManagerDB.queryInsertFaction(conn, "Vikings", "The Vikings are one of the four playable factions in For Honor. Previously thought to have vanished, the Vikings have returned - in great numbers - from across the sea. They have come to take new lands, plunder, expand the clans and reclaim their ancient homeland in the North. Hundreds of clans now dominate amidst the cold and icy tundra. The Vikings are the undisputed power of the rivers and seas. The Vikings live for battle and glory as they seek out riches and new land.");

            // Insert rows into Characters
            ManagerDB.queryInsertCharacters(conn, "Kyoshin", 14.0, 120.0, 1);
            ManagerDB.queryInsertCharacters(conn, "Shinobi", 13.5, 125.0, 1);
            ManagerDB.queryInsertCharacters(conn, "Aramusha", 11.5, 140.0, 1);

            ManagerDB.queryInsertCharacters(conn, "Zhanhu", 10.5, 125.0, 2);
            ManagerDB.queryInsertCharacters(conn, "Tiandi", 10.0, 130.0, 2);
            ManagerDB.queryInsertCharacters(conn, "Shaolin", 13.5, 120.0, 2);

            ManagerDB.queryInsertCharacters(conn, "Raider", 15.5, 110.0, 3);
            ManagerDB.queryInsertCharacters(conn, "Berserker", 12.0, 150, 3);
            ManagerDB.queryInsertCharacters(conn, "Valkyrie", 11.5, 135.0, 3);

            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
