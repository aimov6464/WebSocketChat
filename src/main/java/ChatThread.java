import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ChatThread implements Runnable {
    private final Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private boolean running;
    private final List<PrintWriter> allUsers;
    private String username;

    public ChatThread(Socket socket, List<PrintWriter> otherUsers) {
        this.socket = socket;
        this.allUsers = otherUsers;
        try {
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            running = true;
            this.allUsers.add(writer);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        writer.println("Enter your name");
        writer.flush();
        username = this.getText();
        while (running) {
            String message = this.getText();
            if (message == null) {
                running = false;
            } else {
                this.broadCastMessage(message);
            }
        }
        this.allUsers.remove(allUsers.indexOf(writer));
        this.closeCloseable(reader);
        this.closeCloseable(writer);
        this.closeCloseable(socket);
    }

    private void closeCloseable(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                System.out.printf(e.getMessage());
            }
        }
    }

    private void broadCastMessage(String message) {
        for (PrintWriter w : allUsers) {
            if (!w.equals(writer)) {
                w.println(username + ": " + message);
                w.flush();
            }
        }
    }

    private String getText() {
        String response = null;
        try {
            response = reader.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return response;
    }
}