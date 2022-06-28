package broadcast.client.models;

import broadcast.general.SocketConnect;
import broadcast.general.message.type.Message;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatModels {
    private static String NEXT_LINE = "\n";
    private static String NAME = "Имя: ";
    private static String EMPTY_STR = "";
    private static int START = 0;
    private static int BIOS = 1;
    private static int TIME_TO_GETTER_RESOURCES = 1000;
    private static int TIME_ENDLESSLY = -1;

    public static void pushMessage(KeyEvent keyEvent, TextArea message, String name, SimpleDateFormat formatForDateNow, Chat finalChat) {
        KeyCodeCombination keyComb = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHIFT_DOWN);
        if (keyComb.match(keyEvent)) {
            int position = message.getCaretPosition();
            message.setText(message.getText().substring(START, position) + NEXT_LINE + message.getText().substring(position));
            message.positionCaret(position + BIOS);
        } else if (KeyCode.ENTER == keyEvent.getCode()) {
            int position = message.getCaretPosition();
            Date date = new Date();
            message.setText(message.getText().substring(START, position - BIOS) + message.getText().substring(position));

            String messageText = message.getText().trim();
            message.setText(EMPTY_STR);
            try {
                Message messageOut = new Message();
                messageOut.setMessage(messageText);
                messageOut.setName(name);
                messageOut.setDate(formatForDateNow.format(date));
                finalChat.chat(messageOut);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void startTimeLineRequests(Chat finalChat, final String name) {
        Message messageOut = new Message();
        messageOut.setName(name);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(TIME_TO_GETTER_RESOURCES), //1000 мс * 60 сек = 1 мин
                ae -> {
                    try {
                        finalChat.chat(messageOut);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ));
        timeline.setCycleCount(TIME_ENDLESSLY);
        timeline.play();
    }

    public static Chat loadChatResources(SocketConnect socketConnect, ObservableList<String> langsParticipant, ObservableList<Label> langsChat) {
        Chat chat = null;
        try {
            chat = new Chat(socketConnect, langsParticipant, langsChat);
            chat.chat();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chat;
    }
}