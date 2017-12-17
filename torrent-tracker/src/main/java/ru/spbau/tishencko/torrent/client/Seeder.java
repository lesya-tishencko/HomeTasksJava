package ru.spbau.tishencko.torrent.client;

import ru.spbau.tishencko.torrent.TorrentException;
import ru.spbau.tishencko.torrent.entity.Seed;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Seeder implements AutoCloseable {
    private final int port;
    private final ExecutorService clientPool;
    private final Seed seed;
    private boolean isWorking;

    public Seeder(int port, int maxNumberOfClients, Seed seed) {
        this.port = port;
        clientPool = Executors.newFixedThreadPool(maxNumberOfClients);
        this.seed = seed;
    }

    public void tryToStop() {
        isWorking = false;
    }

    public void start() {
        isWorking = true;
        Runnable seederTask = () -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (isWorking) {
                    Socket clientSocket = serverSocket.accept();
                    Runnable task = () -> {
                        try {
                            ClientHandler handler = new ClientHandler(clientSocket, seed);
                            while (isWorking) {
                                handler.execute();
                            }
                        } catch (IOException | TorrentException e) {
                            e.printStackTrace();
                        }
                    };
                    clientPool.submit(task);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Thread serverThread = new Thread(seederTask);
        serverThread.start();
    }

    @Override
    public void close() throws IOException {
        synchronized (this) {
            if (clientPool.isShutdown()) {
                return;
            }
            clientPool.shutdown();
        }
    }
}
