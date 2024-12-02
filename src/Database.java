import java.sql.*;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/booking";
    private static final String USERNAME = "Your username";
    private static final String PASSWORD = "Your password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
