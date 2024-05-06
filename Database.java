import java.sql.*;

public class Database {

    // link database url
    private static String URL = "jdbc:mysql://localhost:3306/java_project";
    // link database username
    public static String USERNAME = "root";
    // link database password
    private static String PASSWORD = "";

    public ResultSet databaseQuery(String query, Object... params) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
        if (query.trim().toLowerCase().startsWith("select")) {
            return statement.executeQuery();
        } else {
            statement.executeUpdate();
            return null;
        }
    }
}