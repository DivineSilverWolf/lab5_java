package broadcast.general;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SocketConnect {
    private final Scanner scanner;
    private final PrintWriter writer;
    private final Socket socket;
    private final Gson gson;

    public Scanner getScanner() {
        return scanner;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public SocketConnect(Socket socket, Gson gson) throws IOException {
        scanner = new Scanner(socket.getInputStream());
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;
        this.gson = gson;
    }


    public Socket getSocket() {
        return socket;
    }

    public Gson getGson() {
        return gson;
    }
}
