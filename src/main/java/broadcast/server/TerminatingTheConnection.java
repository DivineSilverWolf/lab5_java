package broadcast.server;

import broadcast.general.SocketConnect;
import broadcast.general.message.type.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class TerminatingTheConnection {
    private static final String CLIENT_OUT = " покинул чат";
    private static final int BIOS = 1;

    public static void endConnect(SocketConnect socketConnect, AtomicInteger number, AtomicReference<Message>[] messageHistory, ConcurrentHashMap<String, SocketConnect> mapNameAndSocketConnect, ConcurrentSkipListSet<String> names, String name, final String END_ID) {
        final Logger LOGGER = LogManager.getLogger(ServerListenerAndSender.class.getName() + " : Thread:[" + Thread.currentThread().getName()
                + "] : ");
        LOGGER.warn("start terminating the connection");
        LOGGER.info("clear the name from the list, as well as the socket attached to it");
        mapNameAndSocketConnect.remove(name);
        names.remove(name);
        LOGGER.info("send a message to everyone that the broadcast.client has left the chat");
        String strOut = name + CLIENT_OUT;
        Message messageOut = new Message();
        messageOut.setMessage(strOut);
        socketConnect.getWriter().println(socketConnect.getGson().toJson(messageOut));
        for (String client : names) {
            if (!name.equals(client)) {
                mapNameAndSocketConnect.get(client).getWriter().println(mapNameAndSocketConnect.get(client).getGson().toJson(messageOut));
            }
        }
        LOGGER.info("add to history");
        messageHistory[number.getAndAdd(BIOS) % messageHistory.length].set(messageOut);
        number.set(number.get() % messageHistory.length);
    }
}
