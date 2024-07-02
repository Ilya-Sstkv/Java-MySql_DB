public class ConnectionTry implements  Runnable {

    public static boolean closeThread = false;

    public void run() {
        try {
            while (true) {
                Client.startClient();
                if(closeThread) break;
            }
        } catch (InterruptedException ignored) {}
    }
}