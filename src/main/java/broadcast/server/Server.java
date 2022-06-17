package broadcast.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {
    private static final int SIZE_HISTORY = 10;
    private static final int PORT = 8080;
    private static final int START = 0;
    private static final Logger LOGGER = LogManager.getLogger(Server.class.getName());
    public static void main(String[] argc) throws IOException {
        ConcurrentSkipListSet<String> names = new ConcurrentSkipListSet<>();
        ConcurrentHashMap<String, SocketConnect> mapNameAndSocketConnect = new ConcurrentHashMap<>();
        AtomicReference<String>[] messageHistory = new AtomicReference[SIZE_HISTORY];
        ExecutorService pool = Executors.newCachedThreadPool();
        for(int i=START; i<messageHistory.length; i++)
            messageHistory[i] = new AtomicReference<>();
        AtomicInteger number = new AtomicInteger(START);;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            LOGGER.info("Starting listen socket");
            while (true) {
                LOGGER.info("waiting for a new request");
                Socket socket = serverSocket.accept();
                LOGGER.warn("load new Thread");
                pool.execute(new ServerHandler(socket, mapNameAndSocketConnect, names, messageHistory, number));
            }
        }
    }
}
