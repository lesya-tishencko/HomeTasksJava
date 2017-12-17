package ru.spbau.tishencko.torrent.tracker;

import ru.spbau.tishencko.torrent.TorrentException;
import ru.spbau.tishencko.torrent.entity.TrackerDatabase;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tracker implements AutoCloseable {
    private static final int port = 80;
    private final Path storingDirectory;
    private final ExecutorService clientPool;
    private TrackerDatabase trackerDatabase;

    Tracker(int maxNumberOfClients, Path storingDirectory) {
        this.storingDirectory = storingDirectory;
        clientPool = Executors.newFixedThreadPool(maxNumberOfClients);
    }

    public void start() {
        File file = new File(storingDirectory.toString());
        String name = storingDirectory.toString() + "/" + "tracker";
        if (file.exists()) {
            List<String> files = Arrays.asList(file.list());
            if (files.contains(name)) {
                try {
                    trackerDatabase = new TrackerDatabase(new DataInputStream(new FileInputStream(name)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                File newFile = new File(name);
                try {
                    newFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (trackerDatabase == null) {
            trackerDatabase = new TrackerDatabase();
        }

        Runnable trackerTask = () -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    Runnable task = () -> {
                        try {
                            TrackerHandler handler = new TrackerHandler(clientSocket, trackerDatabase);
                            while (true) {
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
        Thread serverThread = new Thread(trackerTask);
        serverThread.start();
    }

    @Override
    public void close() throws IOException {
        synchronized (this) {
            if (clientPool.isShutdown()) {
                return;
            }
            clientPool.shutdown();

            String name = storingDirectory.toString() + "/" + "tracker";
            try {
                File store = new File(name);
                store.createNewFile();
                trackerDatabase.writeToFile(new DataOutputStream(new FileOutputStream(name)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
