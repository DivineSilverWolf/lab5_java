package broadcast.client.models;

import broadcast.client.Client;
import broadcast.client.controllers.ChatController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ModelMenu {
    private static final String ERROR_PRESENT = "Это имя уже есть";
    private static final String ERROR_EMPTY_OR_SPACE = "Имя не может содержать только пробелы или вовсе быть пустым";
    private static final String CHAT = "chat";
    public static void goChat(TextField name, Text error, Button start){
        Socket socket = new Socket();
        try{
            socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8080), 2000);
            Scanner scanner = new Scanner(socket.getInputStream());
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(name.getText().trim());
            String answer = scanner.nextLine();
            if (answer.equals(ERROR_PRESENT)) {
                System.out.println(ERROR_PRESENT);
                error.setText(ERROR_PRESENT);
            }
            else if(answer.equals(ERROR_EMPTY_OR_SPACE)){
                System.out.println(ERROR_EMPTY_OR_SPACE);
                error.setText(ERROR_EMPTY_OR_SPACE);
            }
            else {
                setRoot(CHAT, start.getScene(), socket, scanner, writer, name.getText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void setRoot(String fxml, Scene scene, Socket socket, Scanner scanner, PrintWriter writer, String name) throws IOException {
        scene.setRoot(loadFXML(fxml, socket, scanner, writer, name));
    }

    private static Parent loadFXML(String fxml, Socket socket, Scanner scanner, PrintWriter writer, String name) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource(fxml + ".fxml"));
        fxmlLoader.setController(new ChatController(socket,scanner,writer,name));
        return fxmlLoader.load();
    }
}
