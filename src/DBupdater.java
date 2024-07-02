import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBupdater {

    public static void insert(PersonalPage page, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String insert = "";
        insert += page.getId() + ", '" + page.getName() +
                "', '" + page.getPassAdmin() + "', " +
                page.getFriend() + ", " +
                (page.getOnline() ? 1 : 0);
        statement.execute(
                "insert into user_pages (id, name, password, friend, online)" +
                        " values(" + insert + ");"
        );
    }

    public static void delete(int id, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(
                "delete from user_pages where id = " + id + ";"
        );
    }

    public static void onlineSet(int id, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("update user_pages set online = 1 where id = " + id + ";");
    }

    public static void onlineUnset(int id, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("update user_pages set online = 0 where id = " + id + ";");
    }

    public static void friendUpdate(int id, int value, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("update user_pages set friend = " + value + " where id = " + id + ";");
    }

    public static void clear(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("delete from user_pages;");
    }
}