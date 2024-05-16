import java.sql.*;

public class Database {

    private static String URL = "jdbc:mysql://localhost:3306/java_project";
    private static String USERNAME = "root";
    private static String PASSWORD = "";

    public static ResultSet databaseQuery(String query, Object... params) throws Exception {
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = connection.prepareStatement(query);

        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }

        if (query.trim().toLowerCase().startsWith("select")) {
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                return null;
            }
            return resultSet;
        } else {
            statement.executeUpdate();
            return null;
        }
    }
}