import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static ArrayList<PersonalPage> userList = new ArrayList<>(); //Список пользователей
    static int active_user; //Активный пользователь

    public static void main(String[] args) throws SQLException, IOException, InterruptedException {

        Connection connection = GetConnection.connect(); //Получение соединения

        Scanner scan = new Scanner(System.in);

        listFill(connection); //Заполнение списка пользователей из базы данных

        if(userList.isEmpty()){ //Если список пользователей пуст... добавление admin/admin
            PersonalPage adminPage = new PersonalPage(
                    1000, "admin", "admin",
                    "0", false);
            userList.add(adminPage);
            DBupdater.insert(adminPage, connection);
        }

        active_user = logIn(scan, connection); //Получение id активного пользователя

        DBupdater.onlineSet(active_user, connection); //Активный пользователь онлайн

        if(active_user == 1000) Menu.adminMenu(connection, scan);
        else Menu.userMenu(connection, scan);

        GetConnection.disconnect(connection);
    }

    static void listFill(Connection connection) throws SQLException {
        ResultSet result = DBoutput.execute(connection);
        while(result.next()) {
            String friend = Integer.toBinaryString(
                    Integer.parseInt(result.getString("friend")));
            boolean isOnline = result.getString("online").equals("1");

            PersonalPage fromDBPage = new PersonalPage(
                    Integer.parseInt(result.getString("id")),
                    result.getString("name"), result.getString("password"),
                    friend, isOnline);
            userList.add(fromDBPage);
        }
    }

    private static int logIn(Scanner scan, Connection connection) throws SQLException {
        System.out.println("\n\t   - = ВОЙДИТЕ В АККАУНТ = -");
        while (true) {
            System.out.print("\nВведите логин: ");
            String login = scan.nextLine();
            System.out.print("Введите пароль: ");
            String pass = scan.nextLine();

            if(login.equals("online_bug_fix")){ //Исключительно для устранения ошибок
                for (PersonalPage personalPage : userList){
                    personalPage.setOnline(false);
                    DBupdater.onlineUnset(personalPage.getId(), connection);
                }
                System.out.println("\n//Онлайн всех пользователей был установлен на 0");
                continue;
            }

            for (PersonalPage personalPage : userList) {
                if (personalPage.getName().equals(login) &&
                        personalPage.getPassAdmin().equals(pass) &&
                        !personalPage.getOnline()) {
                    System.out.println("\nВы выполнили вход как " +
                            personalPage.getName() + "! (id " +
                            personalPage.getId() + ")\n");
                    personalPage.setOnline(true);
                    return personalPage.getId();
                }
            }
            System.out.println("\nНеверно указан логин или пароль, " +
                    "или такой пользователь уже авторизован.\n" +
                    "Попробуйте ещё раз.");
        }
    }
}