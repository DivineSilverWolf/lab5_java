package broadcast.client.models;

import broadcast.client.Client;
import broadcast.client.controllers.ChatController;
import broadcast.general.SocketConnect;
import broadcast.general.message.type.MessageLoad;
import com.google.gson.Gson;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ModelMenu {
    private static final String ERROR_PRESENT = "Это имя уже есть";
    private static final String ERROR_EMPTY_OR_SPACE = "Имя не может содержать только пробелы или вовсе быть пустым";
    private static final String CHAT = "chat";
    private static final String ERROR_SERVER = "Не удалось установить соединение с сервером. Попробуйте повторить попытку позже или поменять ip адрес сервера.";
    private static final String NAME_CREATED = "Имя создано";

    public static void goChat(TextField name, Text error, Button start, TextField address, TextField port, MessageLoad messageLoadOut) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(address.getText(), Integer.parseInt(port.getText())), 2000);
            SocketConnect socketConnect = new SocketConnect(socket, new Gson());
            String nameClient = name.getText().trim();
            messageLoadOut.setName(nameClient);
            socketConnect.getWriter().println(socketConnect.getGson().toJson(messageLoadOut));
            MessageLoad messageLoadIn = socketConnect.getGson().fromJson(socketConnect.getScanner().nextLine(), MessageLoad.class);
            if (messageLoadIn.getError() != null && messageLoadIn.getError().equals(ERROR_PRESENT)) {
                System.out.println(ERROR_PRESENT);
                error.setText(ERROR_PRESENT);
            } else if (messageLoadIn.getError() != null && messageLoadIn.getError().equals(ERROR_EMPTY_OR_SPACE)) {
                System.out.println(ERROR_EMPTY_OR_SPACE);
                error.setText(ERROR_EMPTY_OR_SPACE);
            } else if (messageLoadIn.getAccepter() != null && messageLoadIn.getAccepter().equals(NAME_CREATED) && nameClient.equals(messageLoadIn.getName())) {
                setRoot(CHAT, start.getScene(), socketConnect, nameClient);
            }
        } catch (IOException e) {
            e.printStackTrace();
            error.setText(ERROR_SERVER);
        }
    }

    public static void setRoot(String fxml, Scene scene, SocketConnect socketConnect, String name) throws IOException {
        scene.setRoot(loadFXML(fxml, socketConnect, name));
    }

    private static Parent loadFXML(String fxml, SocketConnect socketConnect, String name) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource(fxml + ".fxml"));
        fxmlLoader.setController(new ChatController(socketConnect, name));
        return fxmlLoader.load();
    }

    private static boolean connect(String address, int port) {
        try (Socket socket = new Socket(address, port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static final String FILE_NAME = "..\\lab5Java\\lab5Java\\addressIP";
    private static final char DIVISION_ADDRESS = ':';
    private static final int BIOS = 1;
    private static final int START_ADDRESS = 0;

    public static void autoSelection(TextField address, TextField port, Button select) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String string = reader.readLine();
            while (string != null) {

                String strAddress = string.substring(START_ADDRESS, string.indexOf(DIVISION_ADDRESS));
                int intPort = Integer.parseInt(string.substring(string.indexOf(DIVISION_ADDRESS) + BIOS));

                if (connect(strAddress, intPort)) {
                    address.setText(strAddress);
                    port.setText(intPort + "");
                    break;
                }
                string = reader.readLine();
            }
            if (string == null) {
                select.setStyle("-fx-background-color: #ff0000");
            } else {
                select.setStyle("-fx-background-color: #00FF00");
            }
        } catch (IOException e) {
            select.setStyle("-fx-background-color: #ff0000");
            e.printStackTrace();
        }
    }
}