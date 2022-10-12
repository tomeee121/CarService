package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    private static String DB_URL = "jdbc:h2:";
    private static final String SQL_STATEMENT = "CREATE TABLE IF NOT EXISTS COMPANY(" +
            "ID INT, " +
            "NAME VARCHAR" +
            ");";

    /**
     * Method to establish connection with Database
     * and execute simple statement
     *
     * @throws SQLException - if connection doesn't succeed
     */

    public static void main(String[] args) throws SQLException {

        String dbName = getDbName(args, "carsharing");
        String dbFilePath = "./src/carsharing/db/" + dbName;
        DB_URL += dbFilePath;
        try (final Connection connection = DriverManager.getConnection(DB_URL)) {
            connection.setAutoCommit(true);
            connection.prepareStatement(SQL_STATEMENT).execute();
        }

    }

    private static String getDbName(String[] args, String defaultName) {
        return args.length == 2 && "-databaseFileName".equals(args[0]) ? args[1] : defaultName;
    }


}