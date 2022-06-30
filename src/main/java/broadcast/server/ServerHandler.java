package broadcast.server;

import broadcast.general.SocketConnect;
import broadcast.general.message.type.Message;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ServerHandler implements Runnable {
    private final String END_ID = "END_FAFAFAC528";
    private final String NAME_ID = "NAME_ZAFAPAC528";
    private final int TIME_OUT = 10000;

    private final ConcurrentSkipListSet<String> names;
    private final Socket socket;
    private final ConcurrentHashMap<String, SocketConnect> mapNameAndSocketConnect;
    private final AtomicReference<Message>[] messageHistory;
    private final AtomicInteger number;

    public ServerHandler(Socket socket, ConcurrentHashMap<String, SocketConnect> mapNameAndSocketConnect, ConcurrentSkipListSet<String> names, AtomicReference<Message>[] messageHistory, AtomicInteger number) {
        this.mapNameAndSocketConnect = mapNameAndSocketConnect;
        this.names = names;
        this.socket = socket;
        this.messageHistory = messageHistory;
        this.number = number;
    }

    @Override
    public void run() {
        final Logger LOGGER = LogManager.getLogger(ServerHandler.class.getName() + " : Thread:[" + Thread.currentThread().getName()
                + "] : ");
        try {
            SocketConnect socketConnect = new SocketConnect(socket, new Gson());
            socket.setSoTimeout(TIME_OUT);
            LOGGER.info("Client start load");
            String name = LoaderClient.load(socketConnect, names, mapNameAndSocketConnect);
            LOGGER.info("Client final load");
            LoaderClient.finalLoad(number, messageHistory, socketConnect, name, names, mapNameAndSocketConnect, END_ID);
            LOGGER.info("start listen and send");
            ServerListenerAndSender.listenAndSend(socketConnect, number, messageHistory, mapNameAndSocketConnect, names, name, END_ID, NAME_ID);
            LOGGER.info("broadcast.client out");
            TerminatingTheConnection.endConnect(socketConnect, number, messageHistory, mapNameAndSocketConnect, names, name, END_ID);
            LOGGER.info("broadcast.client out completion");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
