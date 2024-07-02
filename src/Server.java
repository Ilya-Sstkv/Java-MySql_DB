import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void startServer() {

        int onlineUsers = 0;

        for(int i = 0; i < Main.userList.size(); i++){
            if(Main.userList.get(i).getOnline()) onlineUsers++;
        }
        if(onlineUsers == 1) {
            System.out.println("Отсутствуют пользователи онлайн.\n");
            return;
        }

        try(ServerSocket serverSocket = new ServerSocket(12345)) {

            Socket client = null;
            BufferedReader input = null;
            PrintWriter output = null;

            System.out.println("Подключено пользователей: " + (onlineUsers - 1));

            Scanner scan = new Scanner(System.in);
            System.out.print("\nВведите сообщение для отправки клиенту: ");
            String message = scan.nextLine();
            System.out.println();

            for (int i = 0; i < (onlineUsers - 1); i++) {
                client = serverSocket.accept();
                input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                output = new PrintWriter(client.getOutputStream(), true);
                output.println(message);
            }

            input.close();
            output.close();
            client.close();
        }
        catch (IOException ignored){}
    }
}