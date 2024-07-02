import java.io.*;
import java.net.Socket;

public class Client {

    public static void startClient() throws InterruptedException {

        try(Socket clientSocket = new Socket("localhost", 12345)) {
            System.out.println("\n//Произошло подключение к серверу");
            Thread.sleep(2000);

            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

            String message = input.readLine();
            System.out.println("//Получено сообщение от сервера: " + message);
            System.out.print("//Вы можете продолжить работу с меню: ");

            input.close();
            output.close();
        }
        catch (IOException ignored){}
    }
}