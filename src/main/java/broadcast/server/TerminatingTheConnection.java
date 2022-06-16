package broadcast.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class TerminatingTheConnection {
    private static final String HELLO_CLIENT_OUT = " покинул чат";
    private static final int BIOS = 1;
    private static final String NEXT_LINE = "\n";
    public static void endConnect(SocketConnect socketConnect, AtomicInteger number, AtomicReference<String>[] messageHistory, ConcurrentHashMap<String, SocketConnect> mapNameAndSocketConnect, ConcurrentSkipListSet<String> names, String name, final String END_ID){
        final Logger LOGGER = LogManager.getLogger(ServerListenerAndSender.class.getName()+ " : Thread:[" + Thread.currentThread().getName()
                + "] : ");
        LOGGER.warn("start terminating the connection");
        LOGGER.info("clear the name from the list, as well as the socket attached to it");
        mapNameAndSocketConnect.remove(name);
        names.remove(name);
        LOGGER.info("send a message to everyone that the broadcast.client has left the chat");
        String strOut = name + HELLO_CLIENT_OUT + NEXT_LINE + END_ID;
        socketConnect.getPrintWriter().println(strOut);
        for (String client : names) {
            if (!name.equals(client)) {
                mapNameAndSocketConnect.get(client).getPrintWriter().println(name + HELLO_CLIENT_OUT + NEXT_LINE + END_ID);
            }
        }
        LOGGER.info("add to history");
        messageHistory[number.getAndAdd(BIOS) % messageHistory.length].set(strOut);
        number.set(number.get() % messageHistory.length);
    }
}
