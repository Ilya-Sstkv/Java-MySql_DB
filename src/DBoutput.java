import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBoutput {

    public static ResultSet execute(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery("select * from user_pages;");
    }
}