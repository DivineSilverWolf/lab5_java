package broadcast.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class LoaderClient {
    private static final String ERROR_PRESENT = "Это имя уже есть";
    private static final String ERROR_EMPTY_OR_SPACE = "Имя не может содержать только пробелы или вовсе быть пустым";
    private static final String NAME_CREATED = "Имя создано";
    private static final String EMPTY_STRING = "";
    public static String load(SocketConnect socketConnect, ConcurrentSkipListSet<String> names, ConcurrentHashMap<String, SocketConnect> mapNameAndSocketConnect){
        String name;
        final Logger LOGGER = LogManager.getLogger(LoaderClient.class.getName()+ " : Thread:[" + Thread.currentThread().getName()
                + "] : ");
        LOGGER.warn("start load");
        while (true) {
            name = socketConnect.getScanner().nextLine();
            name = name.trim();
            if (name.equals(EMPTY_STRING)) {
                LOGGER.info("name is empty");
                socketConnect.getPrintWriter().println(ERROR_EMPTY_OR_SPACE);
            }
            else if (names.add(name)) {
                LOGGER.warn("name " + name + " created");
                mapNameAndSocketConnect.put(name, socketConnect);
                socketConnect.getPrintWriter().println(NAME_CREATED);
                break;
            } else {
                LOGGER.info("name " + name + " has already");
                socketConnect.getPrintWriter().println(ERROR_PRESENT);
            }
        }
        return name;
    }
    private static final String HELLO_CLIENT_1 = "В беседу зашел ";
    private static final String HELLO_CLIENT_2 = " поприветствуйте его!";
    private static final String NEXT_LINE = "\n";
    private static final int BIOS = 1;
    public static void finalLoad(AtomicInteger number, AtomicReference<String>[] messageHistory, SocketConnect socketConnect, String name, ConcurrentSkipListSet<String> names, ConcurrentHashMap<String, SocketConnect> mapNameAndSocketConnect, final String END_ID){
        final Logger LOGGER = LogManager.getLogger(LoaderClient.class.getName()+ " : Thread:[" + Thread.currentThread().getName()
                + "] : ");
        LOGGER.warn("start final load");
        LOGGER.info("read and send history");
        for (int i = number.get(); i < messageHistory.length + number.get(); i++) {
            if (messageHistory[i % messageHistory.length].get() != null)
                socketConnect.getPrintWriter().println(messageHistory[i % messageHistory.length].get());
        }

        LOGGER.info("write greetings in history and send");
        String strHello = HELLO_CLIENT_1 + name + HELLO_CLIENT_2 + NEXT_LINE + END_ID;
        socketConnect.getPrintWriter().println(strHello);
        messageHistory[number.getAndAdd(BIOS) % messageHistory.length].set(strHello);
        number.set(number.get() % messageHistory.length);
        LOGGER.info("add name and socket entry");
        for (String client : names) {
            if (!name.equals(client)) {
                mapNameAndSocketConnect.get(client).getPrintWriter().println(HELLO_CLIENT_1 + name + HELLO_CLIENT_2 + "\n" + END_ID);
            }
        }
    }
}
