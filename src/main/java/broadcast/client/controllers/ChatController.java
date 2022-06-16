package broadcast.client.controllers;

import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.Scanner;

import broadcast.client.models.Chat;
import broadcast.client.models.ChatModels;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;

public class ChatController implements Initializable {
    private final String END_ID = "END_FAFAFAC528";
    private final String NAME_ID = "NAME_ZAFAPAC528";
    private final String PATTERN = " 'Дата:' E yyyy.MM.dd.'Время:' HH:mm:ss.";
    @FXML
    private ListView chat;
    @FXML
    private TextArea message;
    @FXML
    private ListView participant;


    private final Socket socket;
    private final Scanner scanner;
    private final PrintWriter writer;
    private final String name;

    public ChatController(Socket socket, Scanner scanner, PrintWriter writer, String name) {
        this.socket = socket;
        this.scanner = scanner;
        this.writer = writer;
        this.name = name;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<String> langsParticipant = FXCollections.observableArrayList("");
        ObservableList<Label> langsChat = FXCollections.observableArrayList();
        participant.setItems(langsParticipant);
        this.chat.setItems(langsChat);

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        MultipleSelectionModel<Label> chatSelectionModel = chat.getSelectionModel();
        MultipleSelectionModel<String> participantSelectionModel = participant.getSelectionModel();

        chatSelectionModel.selectedItemProperty().addListener((changed, oldValue, newValue) -> {
            content.putString(newValue.getText());
            clipboard.setContent(content);
        });
        participantSelectionModel.selectedItemProperty().addListener((changed, oldValue, newValue) -> {
            content.putString(newValue);
            clipboard.setContent(content);
        });

        Chat finalChat = ChatModels.loadChatResources(socket,langsParticipant,langsChat,scanner,writer);
        SimpleDateFormat formatForDateNow = new SimpleDateFormat(PATTERN);
        message.setOnKeyPressed(keyEvent -> ChatModels.pushMessage(keyEvent,message,name,formatForDateNow,finalChat,END_ID));
        ChatModels.startTimeLineRequests(finalChat, NAME_ID);

    }
}