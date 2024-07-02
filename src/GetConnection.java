import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GetConnection {

    public static Connection connect() throws SQLException{
        String url = "jdbc:mysql://localhost:3306/trpo_4.7.1";
        String name = "admin";
        String pass = "admin";
        Connection connection = DriverManager.getConnection(url, name, pass);
        if(!connection.isClosed()) System.out.println("//Соединение с БД установлено");
        return connection;
    }

    public static void disconnect(Connection connection) throws SQLException {
        connection.close();
        if(connection.isClosed()) System.out.println("//Соединение с БД разорвано");
    }
}