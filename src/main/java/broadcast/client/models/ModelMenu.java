package broadcast.client.models;

import broadcast.client.Client;
import broadcast.client.controllers.ChatController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.FileReader;
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
    private static final String ERROR_SERVER = "Не удалось установить соединение с сервером. Попробуйте повторить попытку позже или поменять ip адрес сервера.";
    public static void goChat(TextField name, Text error, Button start, TextField address){
        Socket socket = new Socket();
        try{
            socket.connect(new InetSocketAddress(address.getText(), 8080), 2000);
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
            error.setText(ERROR_SERVER);
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

    private static final String FILE_NAME = "..\\lab5Java\\lab5Java\\addressIP";

    private static boolean connect(String address){
        try(Socket socket = new Socket(address, 8080)){
            return true;
        }
        catch (IOException e){
            return false;
        }
    }

    public static void autoSelection(TextField address, Button select){
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String string = reader.readLine();
            while(string != null) {
                if(connect(string)) {
                    address.setText(string);
                    break;
                }
                string = reader.readLine();
            }
            if(string==null){
                select.setStyle("-fx-background-color: #ff0000");
            }
            else{
                select.setStyle("-fx-background-color: #00FF00");
            }
        } catch (IOException e) {
            select.setStyle("-fx-background-color: #ff0000");
            e.printStackTrace();
        }
    }
}
