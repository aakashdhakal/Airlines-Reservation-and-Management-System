import java.sql.*;

public class Database {

    // link database url
    public static String URL = "jdbc:mysql://localhost:3306/java_project";
    // link database username
    public static String USERNAME = "root";
    // link database password
    public static String PASSWORD = "";

    public void connect() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        System.out.println("Connected to database " + connection.getCatalog());
    }

}
