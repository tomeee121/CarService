package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class H2Utils {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:./src/carsharing/db/";

    private static String dbName = "carsharing.mv.db";

    public static void setName(String[] args) {
        if (args.length != 0 && Optional.of(args[0])
                                        .get()
                                        .equals("-databaseFileName")) {
            dbName = args[1];
        }
    }

    public static void createDb() {
        String drop = "DROP TABLE IF EXISTS COMPANY";
        String dropCarTable = "DROP TABLE IF EXISTS CAR";

        String sqlCompany = "CREATE TABLE IF NOT EXISTS COMPANY " +
                "(ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(255) UNIQUE NOT NULL )";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(dropCarTable);
            stmt.execute(drop);
            stmt.executeUpdate(sqlCompany);
        } catch (SQLException e) {
            System.out.println("SQL exception while executing update on table Company");
        }
    }

    public static void createTableCar() {

        String sqlCar = "CREATE TABLE IF NOT EXISTS CAR " +
                "(ID INTEGER PRIMARY KEY AUTO_INCREMENT, \n" +
                "NAME VARCHAR(255) UNIQUE NOT NULL,\n" +
                "COMPANY_ID INTEGER NOT NULL," +
                "FOREIGN KEY(COMPANY_ID) REFERENCES COMPANY(ID));";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sqlCar);
        } catch (SQLException e) {
            System.out.println("SQL exception while executing update on table Car");
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL + dbName);
            connection.setAutoCommit(true);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
