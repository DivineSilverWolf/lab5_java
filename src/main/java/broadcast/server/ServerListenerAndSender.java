package broadcast.server;

import broadcast.general.SocketConnect;
import broadcast.general.message.type.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ServerListenerAndSender {
    private static final int BIOS = 1;

    public static void listenAndSend(SocketConnect socketConnect, AtomicInteger number, AtomicReference<Message>[] messageHistory, ConcurrentHashMap<String, SocketConnect> mapNameAndSocketConnect, ConcurrentSkipListSet<String> names, String name, final String END_ID, final String NAME_ID) {
        final Logger LOGGER = LogManager.getLogger(ServerListenerAndSender.class.getName() + " : Thread:[" + Thread.currentThread().getName()
                + "] : ");
        LOGGER.warn("start listen and send");
        while (socketConnect.getSocket().isConnected()) {
            try {
                LOGGER.info("completed message");
                Message messageIn = socketConnect.getGson().fromJson(socketConnect.getScanner().nextLine(), Message.class);
                if (messageIn.getMessage() != null) {
                    LOGGER.info("end message");
                    LOGGER.info("send a message to everyone");
                    socketConnect.getWriter().println(socketConnect.getGson().toJson(messageIn));
                    for (String client : names) {
                        if (!name.equals(client)) {
                            mapNameAndSocketConnect.get(client).getWriter().println(mapNameAndSocketConnect.get(client).getGson().toJson(messageIn));
                        }
                    }
                    LOGGER.info("send a message to everyone");
                    messageHistory[number.getAndAdd(BIOS) % messageHistory.length].set(messageIn);
                    LOGGER.info("write greetings in history");
                    number.set(number.get() % messageHistory.length);
                } else {
                    LOGGER.info("name list query");
                    LOGGER.info("fill in the list of names");
                    messageIn.setClients(names);
                    LOGGER.info("send name list");
                    socketConnect.getWriter().println(socketConnect.getGson().toJson(messageIn));
                }
            } catch (Exception e) {
                break;
            }
        }
    }
}
