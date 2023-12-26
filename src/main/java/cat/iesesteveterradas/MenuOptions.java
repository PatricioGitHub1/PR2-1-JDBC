package cat.iesesteveterradas;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;

public class MenuOptions {
    Connection conn;

    enum Output {
        TABLE_FACTIONS,
        TABLE_CHARACTERS,
        FACTION_CHARACTERS,
        BEST_CHARACTER
    }

    final String MAIN_MENU = "--------- For Honor Database ---------"
                    + "\n 1) Show Tables"
                    + "\n 2) Show Characters from a Faction"
                    + "\n 3) Characters with the highest ATTACK statistics"
                    + "\n 4) Characters with the highest DEFENSE statistics"
                    + "\n Exit) Insert \'Exit\' to close programm"
                    + "\n\n Option: ";

    final String MENU_TABLES= "---------- Available Tables ----------"
                    + "\n 1) Factions"
                    + "\n 2) Characters"
                    + "\n\n Option: ";

    final String MENU_FACTIONS = "-------------- FACTIONS --------------"
                    + "\n 1) Samurais"
                    + "\n 2) Wu Lin"
                    + "\n 3) Vikings"
                    + "\n\n Option: ";

    public MenuOptions(Connection conn) {
        this.conn = conn;
    }

    // Function that runs main Menu
    public void showMenu() throws SQLException {
        String input;
        Scanner sc = new Scanner(System.in);
        
        while (true) {
            System.out.print("\n" + MAIN_MENU);
            String idFaccio = "";
            input = sc.nextLine();
            input = input.toLowerCase();
            switch (input) {
                case "1":
                    showMenu1(sc);
                    break;
                case "2":
                    idFaccio = factionPickerMenu(sc);
                    if (idFaccio != null) {
                        printFormatedResult(ManagerDB.querySelect(conn, String.format("select chr.*, fac.name from Characters chr join Factions fac on fac.id = chr.idFaccio where chr.idFaccio = %s;", idFaccio)), 
                                            Output.FACTION_CHARACTERS);
                    }
                    break;
                case "3":
                    idFaccio = factionPickerMenu(sc);
                    if (idFaccio != null) {
                        printFormatedResult(ManagerDB.querySelect(conn, String.format("select * from Characters where idFaccio = %s order by damage desc limit 1;", idFaccio)), 
                                            Output.BEST_CHARACTER);
                    }
                    break;
                case "4":
                    idFaccio = factionPickerMenu(sc);
                    if (idFaccio != null) {
                        printFormatedResult(ManagerDB.querySelect(conn, String.format("select * from Characters where idFaccio = %s order by defense desc limit 1;", idFaccio)), 
                                            Output.BEST_CHARACTER);
                    }
                    break;
                case "exit":

                    System.out.print("Closing programm");
                    for (int i = 0; i < 3; i++) {
                        System.out.print(" . ");
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // We close Scanner and Connection
                    sc.close();
                    conn.close();

                    System.out.println("Bye !");

                    return;

                default:
                    System.out.println("Unknown option | Please introduce a valid option");
                    break;
            }
        }
    
    }

    // Menu to choose the Table to show
    public void showMenu1(Scanner sc) {
        System.out.print("\n" + MENU_TABLES);
        String input = sc.nextLine();
        String query;
        ResultSet rs;
        switch (input) {
            case "1":
                query = "select * from Factions;";
                rs = ManagerDB.querySelect(conn, query);
                printFormatedResult(rs, Output.TABLE_FACTIONS);
                break;
            case "2":
                query = "select * from Characters;";
                rs = ManagerDB.querySelect(conn, query);
                printFormatedResult(rs, Output.TABLE_CHARACTERS);
                break;
            default:
                System.out.println("Unknown Table");
                break;
        }
    }

    // Menu for picking a Faction and returning the id in String
    public String factionPickerMenu(Scanner sc) {
        try {
            System.out.print("\n" + MENU_FACTIONS);
            String input = sc.nextLine();
            System.out.println(input);

            if (Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= 3) {
                return input;
            }

            

        } catch (NumberFormatException e) {
            System.out.print("Introduce an Integer | ");
        }
        System.out.println("Unknown Value");
        return null;
    }

    // Function to print a ResultSet in different layouts depending on the Enum provided
    public void printFormatedResult(ResultSet rs, Output out) {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            switch (out) {
                case TABLE_FACTIONS:
                
                    System.out.printf("================================================================================%n");
                    System.out.printf("| %-5s | %-15s | %-50s |%n", rsmd.getColumnName(1), rsmd.getColumnName(2), rsmd.getColumnName(3));
                    System.out.printf("================================================================================%n");
                    
                    while (rs.next()) {
                        if (rs.getString(3).length() > 50) {
                            String summaryLine = rs.getString(3);
                            int iterations = summaryLine.length()/50;

                            for (int i = 0; i < iterations + 1; i++) {
                                if (i == 0) {
                                    System.out.printf("| %-5s | %-15s | %-50s |%n", Integer.toString(rs.getInt(1)), rs.getString(2), summaryLine.substring(0, 50));
                                    summaryLine = summaryLine.substring(50);
                                } else if (summaryLine.length() < 50) {
                                    System.out.printf("| %-5s | %-15s | %-50s |%n", " ", " ", summaryLine);
                                } else {
                                    System.out.printf("| %-5s | %-15s | %-50s |%n", " ", " ", summaryLine.substring(0, 50));
                                    summaryLine = summaryLine.substring(50);
                                }
                                    
                            }

                        } else {
                            System.out.printf("| %-5s | %-15s | %-50s |%n", Integer.toString(rs.getInt(1)), rs.getString(2), rs.getString(3));
                        }

                        System.out.printf("--------------------------------------------------------------------------------%n");
                    }

                    break;

                case TABLE_CHARACTERS:
                    
                    System.out.printf("=======================================================================%n");
                    System.out.printf("| %-10s | %-15s | %-10s | %-10s | %-10s |%n", rsmd.getColumnName(1), rsmd.getColumnName(2), rsmd.getColumnName(3), rsmd.getColumnName(4), rsmd.getColumnName(5));
                    System.out.printf("=======================================================================%n");
                    
                    while (rs.next()) {
                        System.out.printf("| %-10s | %-15s | %-10s | %-10s | %-10s |%n", Integer.toString(rs.getInt(1)), rs.getString(2), Double.toString(rs.getDouble(3)), Double.toString(rs.getDouble(4)), Integer.toString(rs.getInt(5)));
                        System.out.printf("-----------------------------------------------------------------------%n");

                    }

                    break;

                case FACTION_CHARACTERS:
                    System.out.printf("=========================================================================================%n");
                    System.out.printf("| %-10s | %-15s | %-10s | %-10s | %-10s | %-15s |%n", rsmd.getColumnName(1), rsmd.getColumnName(2), rsmd.getColumnName(3), rsmd.getColumnName(4), rsmd.getColumnName(5), rsmd.getColumnName(6));
                    System.out.printf("=========================================================================================%n");
                    
                    while (rs.next()) {
                        System.out.printf("| %-10s | %-15s | %-10s | %-10s | %-10s | %-15s |%n", Integer.toString(rs.getInt(1)), rs.getString(2), Double.toString(rs.getDouble(3)), Double.toString(rs.getDouble(4)), Integer.toString(rs.getInt(5)), rs.getString(6));
                        System.out.printf("-----------------------------------------------------------------------------------------%n");

                    }

                    break;
                
                case BEST_CHARACTER:
                    System.out.printf("=======================================================================%n");
                    System.out.printf("| %-10s | %-15s | %-10s | %-10s | %-10s |%n", rsmd.getColumnName(1), rsmd.getColumnName(2), rsmd.getColumnName(3), rsmd.getColumnName(4), rsmd.getColumnName(5));
                    System.out.printf("=======================================================================%n");
                    
                    System.out.printf("| %-10s | %-15s | %-10s | %-10s | %-10s |%n", Integer.toString(rs.getInt(1)), rs.getString(2), Double.toString(rs.getDouble(3)), Double.toString(rs.getDouble(4)), Integer.toString(rs.getInt(5)));
                    System.out.printf("-----------------------------------------------------------------------%n");

                default:
                    break;
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
}
