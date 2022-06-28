package broadcast.client.models;

import broadcast.general.SocketConnect;
import broadcast.general.message.type.Message;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

import java.net.SocketException;
import java.util.Set;

public class Connect implements Runnable {
    private SocketConnect socketConnect;
    private ObservableList<String> participant;
    private ObservableList<Label> chat;
    private final int TIME_OUT = 1000 * 10;

    public Connect(SocketConnect socketConnect, ObservableList<String> participant, ObservableList<Label> chat) {
        this.participant = participant;
        this.chat = chat;
        this.socketConnect = socketConnect;
    }

    @Override
    public void run() {
        try {
            socketConnect.getSocket().setSoTimeout(TIME_OUT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        while (true) {
            Message messageIn = socketConnect.getGson().fromJson(socketConnect.getScanner().nextLine(), Message.class);
            if (messageIn.getMessage() != null) {
                String messageClient;
                if (messageIn.getName() != null)
                    messageClient = messageIn.getName() + messageIn.getDate() + "\n" + messageIn.getMessage();
                else
                    messageClient = messageIn.getMessage();
                Label label = new Label(messageClient);
                Platform.runLater(() -> chat.add(label));
            } else {
                Set<String> messageSet = messageIn.getClients();
                for (String client : messageSet)
                    if (!participant.contains(client))
                        Platform.runLater(() -> participant.add(client));
                Platform.runLater(() -> participant.removeIf(client -> !messageSet.contains(client)));
            }
        }
    }
}