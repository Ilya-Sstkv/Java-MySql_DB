import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.io.BufferedReader;
import java.util.Random;
import java.util.Scanner;

public class Menu {

    private static ArrayList<Boolean> friendList = new ArrayList<>(); //Список друзей пользователя
    private static ArrayList<String> nameList_r = new ArrayList<>(); //Список повторяющихся имен
    private static int userIndex;
    private static int choice;

    public static void adminMenu(Connection connection, Scanner scan)
            throws SQLException, IOException {

        Random random = new Random();
        BufferedReader reader;

        boolean menu = true;
        int newUsers;
        int userPick;
        String userName = "";
        String name = "";

        for (PersonalPage personalPage : Main.userList) {
            if (personalPage.getId() == Main.active_user) userName = personalPage.getName();
        }

        nameListFill(); //Заполнение списка имен

        while(menu) { //Вызов меню
            System.out.println("\t\t- = СОЦИАЛЬНАЯ СЕТЬ = -");
            System.out.println("\t\t(Вы: " + userName + ")");
            System.out.println("\n1. Посмотреть пользователей");
            System.out.println("2. Обновить список пользователей");
            System.out.println("3. Добавить пользователя");
            System.out.println("4. Удалить пользователя");
            System.out.println("5. Открыть сервер для рассылки сообщений");
            System.out.println("6. Выход");
            choice = correctInput(6, scan);

            switch (choice){
                case 1: { //Вывод всех пользователей (admin не может добавлять пользователей в друзья)
                    System.out.println("\n\t- = СТРАНИЦЫ  ПОЛЬЗОВАТЕЛЕЙ = -\n");
                    while (true) {
                        DBrefresh(connection);
                        for (int i = 0; i < Main.userList.size(); i++) {
                            System.out.print(i + 1 + ". " + Main.userList.get(i).getName());
                            if(Main.userList.get(i).getName().equals(userName)) {
                                System.out.println(" (Вы)");
                                userIndex = i;
                            } else System.out.println();
                        }
                        System.out.println(Main.userList.size() + 1 + ". -Назад в меню");
                        DBrefresh(connection);
                        userPick = correctInput(Main.userList.size()+1, scan);
                        DBrefresh(connection);
                        if(userPick == Main.userList.size()+1) break; //Выход
                        System.out.print("\nПользователь " + userPick + ": ");
                        if (userPick == userIndex+1) System.out.println("(ваша страница)");
                        System.out.println(Main.userList.get(userPick - 1).toStringAdmin());
                    }
                    break;
                }
                case 2: { //Очищение/перезапись пользователей
                    DBrefresh(connection);
                    boolean isAnyoneOnline = false;
                    for (PersonalPage personalPage : Main.userList){
                        if(personalPage.getOnline()){
                            System.out.println("\nВы не можете обновить списко пользователей " +
                                    "пока другие пользователи авторизованы в сети\n");
                            isAnyoneOnline = true;
                            break;
                        }
                    }
                    if(isAnyoneOnline) break;
                    Main.userList.clear();
                    DBupdater.clear(connection);
                    nameList_r.clear();
                    System.out.print("\nУкажите новое количество пользователей социальной сети (0-10): ");
                    newUsers = correctInput(10, scan);
                    for (int i = 0; i < newUsers - 1; i++) {
                        try { //Генерация случайного имени
                            int similar_names = 0;
                            reader = new BufferedReader(new FileReader("nameList.txt"));
                            int nameSetter = 1 + random.nextInt(15);
                            for (int j = 0; j < nameSetter; j++) {
                                name = reader.readLine();
                            }
                            nameList_r.add(name);
                            reader.close();

                            for (String list : nameList_r) {
                                if (list.contains(name)) {
                                    similar_names++;
                                }
                            }
                            if (similar_names != 0) {
                                name += "_" + similar_names;
                            }
                        }
                        catch (IOException e) {
                            System.out.println("Ошибка открытия файла: " + e);
                        }

                        int randomId = 1001 + random.nextInt(8999); //Генерация случайного id
                        for (int j = 0; j < Main.userList.size(); j++) {
                            if (Main.userList.get(j).getId() == randomId) {
                                randomId = 1001 + random.nextInt(8999);
                                j = 0;
                            }
                        }

                        int password = 10000001 + random.nextInt(89999999); //Генерация случайного пароля
                        PersonalPage guest = new PersonalPage(randomId, name,
                                String.valueOf(password), "0", false);
                        DBupdater.insert(guest, connection);
                    }
                    PersonalPage adminPage = new PersonalPage( //Добавление admin/admin
                            1000, "admin", "admin",
                            "0", true);
                    DBupdater.insert(adminPage, connection);
                    Main.listFill(connection);
                    System.out.println("Список пользователей успешно обновлен!\n");
                    break;
                }
                case 3: { //Добавить нового пользователя
                    System.out.println("\n\t- = ДОБАВЛЕНИЕ ПОЛЬЗОВАТЕЛЯ = -\n");
                    DBrefresh(connection);
                    System.out.print("Введите имя нового пользователя: ");
                    scan.nextLine();
                    while(true){ //Ввод имени нового пользователя
                        name = scan.nextLine();
                        if(nameList_r.contains(name)){
                            System.out.print("\nПользователь с таким именем уже существует.\n" +
                            "Попробуйте другое имя: ");
                            continue;
                        }
                        break;
                    }
                    System.out.print("Введите пароль пользователя: "); //Ввод пароля пользователя
                    String password = scan.nextLine();

                    int randomId = 1001 + random.nextInt(8999); //Генерация случайного id
                    for (int j = 0; j < Main.userList.size(); j++) {
                        if (Main.userList.get(j).getId() == randomId) {
                            randomId = 1001 + random.nextInt(8999);
                            j = 0;
                        }
                    }

                    PersonalPage guest = new PersonalPage(randomId, name,
                            password, "0", false);
                    DBupdater.insert(guest, connection);
                    Main.userList.clear();
                    Main.listFill(connection);
                    System.out.println("\nПользователь " + name + " был успешно добавлен!\n");
                    addUser_Friend(randomId, connection);
                    break;
                }
                case 4: {
                    System.out.println("\n\t- = СТРАНИЦЫ  ПОЛЬЗОВАТЕЛЕЙ = -\n");
                    DBrefresh(connection);
                    for (int i = 0; i < Main.userList.size(); i++) {
                        System.out.print(i + 1 + ". " + Main.userList.get(i).getName());
                        if (Main.userList.get(i).getName().equals(userName)) {
                            System.out.println(" (Вы)");
                        } else System.out.println();
                    }
                    System.out.println(Main.userList.size() + 1 + ". -Назад в меню");
                    userPick = correctInput(Main.userList.size() + 1, scan);
                    DBrefresh(connection);
                    if (userPick == Main.userList.size() + 1) break; //Выход
                    if (Main.userList.get(userPick-1).getOnline()){
                        System.out.println("\nВы не можете удалить этого пользователя," +
                                "так как он сейчас онлайн\n");
                        break;
                    }
                    System.out.println("\nПользователь " + Main.userList.get(userPick - 1).getName() +
                            " был успешно удален\n");
                    DBupdater.delete(Main.userList.get(userPick - 1).getId(), connection);
                    deleteUser_Friend(Main.userList.get(userPick - 1).getId(), connection);
                    Main.userList.remove(userPick - 1);
                    break;
                }
                case 5: {
                    DBrefresh(connection);
                    System.out.println("\n\t   - = ОТКРЫТИЕ  СЕРВЕРА = -\n");
                    System.out.println("Сервер ожидает подключения...\n");
                    Server.startServer();
                    break;
                }
                case 6: { //Выход (в том числе из сети)
                    DBupdater.onlineUnset(Main.active_user, connection);
                    menu = false;
                    break;
                }
            }
        }
    }

    public static void userMenu(Connection connection, Scanner scan)
            throws SQLException, IOException, InterruptedException {

        Thread tryToServer = new Thread(new ConnectionTry());
        tryToServer.start();

        int index = 0;
        int userPick;
        int interaction;
        boolean menu = true;
        String userName = "";

        for (int i = 0; i < Main.userList.size(); i++) {
            if (Main.userList.get(i).getId() == Main.active_user) {
                userName = Main.userList.get(i).getName();
                index = i;
            }
        }

        friendListFill(index, 0);

        while(menu) { //Вызов меню
            System.out.println("\t\t- = СОЦИАЛЬНАЯ СЕТЬ = -");
            System.out.println("\t\t(Вы: " + userName + ")");
            System.out.println("\n1. Посмотреть пользователей");
            System.out.println("2. Выход");
            choice = correctInput(2, scan);

            switch (choice) {
                case 1: {
                    System.out.println("\n\t- = СТРАНИЦЫ  ПОЛЬЗОВАТЕЛЕЙ = -\n");
                    int memorySize = Main.userList.size();
                    while (true) {
                        DBrefresh(connection);
                        for (int i = 1; i < Main.userList.size(); i++) {
                            System.out.print(i + ". " + Main.userList.get(i).getName());
                            if(Main.userList.get(i).getName().equals(userName)) {
                                System.out.println(" (Вы)");
                                userIndex = i;
                            } else if(friendList.get(i)){
                                System.out.println(" (Ваш друг)");
                            } else System.out.println();
                        }
                        System.out.println(Main.userList.size() + ". -Назад в меню");
                        userPick = correctInput(Main.userList.size(), scan);
                        DBrefresh(connection);
                        if(Main.userList.size() != memorySize){
                            System.out.println("\n//Произошло обновление базы данных...");
                            memorySize = Main.userList.size();
                            continue;
                        }
                        if(userPick == Main.userList.size()) break; //Выход
                        System.out.print("\nПользователь " + userPick + ": ");
                        if (userPick == userIndex) System.out.println("(ваша страница)");
                        System.out.println(Main.userList.get(userPick).toString());
                        if(!friendList.get(userPick) && userPick != userIndex) {
                            System.out.println("1. Добавить " + Main.userList.get(userPick).getName() +
                                    " в друзья");
                            System.out.println("2. -Назад");
                            interaction = correctInput(2, scan);
                            DBrefresh(connection);
                            if(Main.userList.size() != memorySize){
                                System.out.println("\n//Произошло обновление базы данных...");
                                memorySize = Main.userList.size();
                                continue;
                            }
                            if (interaction == 1) {
                                friendList.set(userPick, true);
                                System.out.println("\nПользователь " + Main.userList.get(userPick).getName() +
                                        " был успешно добавлен в друзья.\n");
                                changeFriend(index, connection);
                            }
                            else System.out.println();
                        }
                        else if (userPick != userIndex){
                            System.out.println("1. Удалить " + Main.userList.get(userPick).getName() +
                                    " из списка друзей");
                            System.out.println("2. -Назад");
                            interaction = correctInput(2, scan);
                            DBrefresh(connection);
                            if(Main.userList.size() != memorySize){
                                System.out.println("\n//Произошло обновление базы данных...");
                                memorySize = Main.userList.size();
                                continue;
                            }
                            if (interaction == 1) {
                                friendList.set(userPick, false);
                                System.out.println("Вы удалили пользователя " +
                                        Main.userList.get(userPick).getName() + " из друзей\n");
                            }
                            else System.out.println();
                        }
                    }
                    break;
                }
                case 2: {
                    DBupdater.onlineUnset(Main.active_user, connection);
                    ConnectionTry.closeThread = true;
                    menu = false;
                    break;
                }
            }
        }
    }

    private static int correctInput(int limit, Scanner scan){ //Проверка на числовой ввод
        while (true) {
            int input;
            try {
                input = scan.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Ошибка числового ввода, попробуйте ещё раз: ");
                scan.nextLine();
                continue;
            }
            if (input < 1 || input > limit) {
                System.out.print("Указана неверная опция, попробуйте ещё раз: ");
                continue;
            }
            return input;
        }
    }

    private static void nameListFill(){ //Заполенение списка повторяющихся имен
        for (PersonalPage personalPage : Main.userList) {
            nameList_r.add(personalPage.getName());
        }
    }

    private static void friendListFill(int index, int id){
        friendList.clear();
        friendList.add(false);
        String code = Main.userList.get(index).getFriend();
        int very_important;
        if(id == 0) very_important = 1;
        else very_important = 2;
        while(code.length() < Main.userList.size()-very_important){ //Если код короче, чем нужно...
            code = "0" + code; //... в его начало добавляются нули до необходимого размера
        }

        for(int i = 0; i < code.length(); i++){
            if(Integer.parseInt(String.valueOf(code.charAt(i))) == 1){
                friendList.add(true);
            } else {
                friendList.add(false);
            }
        }
    }

    private static void changeFriend(int index, Connection connection) throws SQLException {
        String code = "";
        for (Boolean friend : friendList) {
            if (friend) code += "1";
            else code += "0";
        }
        int value = Integer.parseInt(code, 2);
        DBupdater.friendUpdate(Main.userList.get(index).getId(), value, connection);
    }

    private static void addUser_Friend(int id, Connection connection) throws SQLException {
        for(int i = 1; i<Main.userList.size(); i++) {
            friendListFill(i, id);
            for (int j = 0; j < Main.userList.size(); j++) {
                if (Main.userList.get(j).getId() == id)
                    friendList.add(j, false);
            }
            changeFriend(i, connection);
        }
    }

    private static void deleteUser_Friend(int id, Connection connection) throws SQLException {
        for(int i = 1; i<Main.userList.size(); i++) {
            friendListFill(i, 0);
            for (int j = 0; j < Main.userList.size(); j++) {
                if (Main.userList.get(j).getId() == id)
                    friendList.remove(j);
            }
            changeFriend(i, connection);
        }
    }

    private static void DBrefresh(Connection connection) throws SQLException {
        Main.userList.clear();
        Main.listFill(connection);
    }
}