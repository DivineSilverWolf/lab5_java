package broadcast.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ServerListenerAndSender {
    private static final String EMPTY_STRING = "";
    private static final String NEXT_LINE = "\n";
    private static final int BIOS = 1;
    public static void listenAndSend(SocketConnect socketConnect, AtomicInteger number, AtomicReference<String>[] messageHistory, ConcurrentHashMap<String, SocketConnect> mapNameAndSocketConnect, ConcurrentSkipListSet<String> names, String name, final String END_ID, final String NAME_ID){
        final Logger LOGGER = LogManager.getLogger(ServerListenerAndSender.class.getName()+ " : Thread:[" + Thread.currentThread().getName()
                + "] : ");
        LOGGER.warn("start listen and send");

        StringBuilder message = new StringBuilder(EMPTY_STRING);
        while (socketConnect.getScanner().hasNextLine()) {
            String str = socketConnect.getScanner().nextLine();
            message.append(str).append(NEXT_LINE);
            LOGGER.info("collect message");
            if (str.equals(END_ID)) {
                LOGGER.info("end message");
                LOGGER.info("send a message to everyone");
                socketConnect.getPrintWriter().println(message.toString().trim());
                for (String client : names) {
                    if (!name.equals(client)) {
                        mapNameAndSocketConnect.get(client).getPrintWriter().println(message.toString().trim());
                    }
                }
                LOGGER.info("send a message to everyone");
                messageHistory[number.getAndAdd(BIOS) % messageHistory.length].set(message.toString());
                LOGGER.info("write greetings in history");
                number.set(number.get() % messageHistory.length);
                message = new StringBuilder(EMPTY_STRING);
            } else if (str.equals(NAME_ID)) {
                LOGGER.info("name list query");
                LOGGER.info("fill in the list of names");
                StringBuilder messageName = new StringBuilder();
                for (String client : names) {
                    messageName.append(client + NEXT_LINE);
                }
                LOGGER.info("send name list");
                messageName.append(NAME_ID);
                socketConnect.getPrintWriter().println(messageName);
                message = new StringBuilder(EMPTY_STRING);
            }
        }
    }
}
