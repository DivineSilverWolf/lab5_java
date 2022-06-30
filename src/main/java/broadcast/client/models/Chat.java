package broadcast.client.models;

import broadcast.general.SocketConnect;
import broadcast.general.message.type.Message;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

import java.io.IOException;

public class Chat {
    private SocketConnect socketConnect;
    private ObservableList<String> participant;
    private ObservableList<Label> chat;

    public Chat(SocketConnect socketConnect, ObservableList<String> participant, ObservableList<Label> chat) throws IOException {
        this.chat = chat;
        this.participant = participant;
        this.socketConnect = socketConnect;
    }

    public void chat(Message message) throws IOException {
        socketConnect.getWriter().println(socketConnect.getGson().toJson(message));
    }

    public void chat() throws IOException {
        new Thread(new Connect(socketConnect, participant, chat)).start();
    }
}
