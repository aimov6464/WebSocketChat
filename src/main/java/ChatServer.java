import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private ServerSocket serverSocket;
    private List<PrintWriter> allUsers;

    public ChatServer() {
        try {
            serverSocket = new ServerSocket(5555);
            allUsers = new ArrayList<>();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void processor() {
        while (true) {
            try {
                System.out.println("Listening mode...>>>");
                new Thread(new ChatThread(serverSocket.accept(), allUsers)).start();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                break;
            }
        }
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new ChatServer().processor();
    }
}