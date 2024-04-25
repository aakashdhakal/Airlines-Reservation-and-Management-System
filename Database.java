import java.sql.*;

public class Database {

    // link database url
    private static String URL = "jdbc:mysql://localhost:3306/java_project";
    // link database username
    public static String USERNAME = "root";
    // link database password
    private static String PASSWORD = "";

    public ResultSet databaseGet(String query) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public void databaseSet(String query) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }

}
