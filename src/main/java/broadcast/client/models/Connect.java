package broadcast.client.models;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.Set;

public class Connect implements Runnable {
    private final String END_ID = "END_FAFAFAC528";
    private final String NAME_ID = "NAME_ZAFAPAC528";
    private Socket socket;
    private ObservableList<String> participant;
    private ObservableList<Label> chat;
    private Scanner scanner;
    private final int TIME_OUT = 1000 * 10;
    private final String NEXT_LINE = "\n";
    private final int STRING_EMPTY = 0;

    public Connect(Socket socket, ObservableList<String> participant, ObservableList<Label> chat, Scanner scanner) {
        this.participant = participant;
        this.chat = chat;
        this.socket = socket;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        StringBuilder message = new StringBuilder();
        try {
            socket.setSoTimeout(TIME_OUT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        while (true) {
            assert scanner != null;
            if (!scanner.hasNextLine()) break;
            String line = scanner.nextLine();
            if (!line.equals(END_ID) && !line.equals(NAME_ID))
                message.append(line + NEXT_LINE);
            else if (line.equals(END_ID)) {
                String text = message.toString().trim();
                Label label = new Label(text);
                Platform.runLater(() -> chat.add(label));
                message.setLength(STRING_EMPTY);
            } else if (line.equals(NAME_ID)) {
                Set<String> messageSet = Set.of(message.toString().trim().split(NEXT_LINE));
                for (String client : messageSet)
                    if (!participant.contains(client))
                        Platform.runLater(() -> participant.add(client));
                Platform.runLater(() -> participant.removeIf(client -> !messageSet.contains(client)));

                message.setLength(STRING_EMPTY);
            }
        }
    }
}