package broadcast.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketConnect {
    private final PrintWriter printWriter;
    private final Scanner scanner;

    public SocketConnect(Socket socket) throws IOException {
        scanner = new Scanner(socket.getInputStream());
        printWriter = new PrintWriter(socket.getOutputStream(), true);
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public Scanner getScanner() {
        return scanner;
    }
}
