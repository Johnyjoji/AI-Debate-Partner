import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/debate_db"; // ✅ use your DB name
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";     // put your MySQL password if any

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

}
