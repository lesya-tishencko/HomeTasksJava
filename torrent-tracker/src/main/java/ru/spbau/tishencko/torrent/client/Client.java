package ru.spbau.tishencko.torrent.client;

import ru.spbau.tishencko.torrent.entity.Seed;
import ru.spbau.tishencko.torrent.tracker.Tracker;
import ru.spbau.tishencko.torrent.tracker.TrackerInterpreter;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Client implements AutoCloseable {
    private static final long UPDATE_TIMEOUT = 1000000;
    private final int port;
    private Socket clientSocket;
    private ClientType type;
    private Path storingDirectory;
    private Seed seed;
    private Seeder seeder;
    private TrackerInterpreter interpreter;
    private Timer updateTimer;
    private TimerTask timerTask;
    private Scanner scanner;

    public Client(String host, int port, Scanner scanner) throws IOException {
        clientSocket = new Socket(host, Tracker.port);
        this.port = port;
        type = ClientType.Leecher;
        this.scanner = scanner;
    }

    public void connect(Path storingDirectory) throws IOException {
        this.storingDirectory = storingDirectory;
        File file = new File(storingDirectory.toString());
        String name = storingDirectory.toString() + "/" + "client" + String.valueOf(port);
        if (file.exists()) {
            List<String> files = Arrays.asList(file.list());
            if (files.contains(name)) {
                seed = new Seed(new DataInputStream(new FileInputStream(name)));
                type = ClientType.Seeder;
            }
        }
        if (seed == null) {
            seed = new Seed(clientSocket.getInetAddress().getAddress(), (short) port);
        }
        seeder = new Seeder(port, 100, seed);
        interpreter = new TrackerInterpreter(clientSocket, scanner, seed);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    interpreter.executeUpdate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        updateTimer = new Timer();
    }

    public void run() throws IOException {
        while (true) {
            String statMessage;
            if (clientSocket.isClosed()) return;
            try {
                statMessage = interpreter.interpret();
            } catch (NoSuchElementException e) {
                return;
            }
            if (statMessage.startsWith("Query to seed:")) {
                int hostPos = statMessage.indexOf(':') + 1;
                int portPos = statMessage.indexOf(',') + 1;
                String host = statMessage.substring(hostPos, portPos - 1);
                int port = Integer.valueOf(statMessage.substring(portPos));
                Socket toSeed = new Socket(host, port);
                ClientInterpreter clientInterpreter = new ClientInterpreter(toSeed, scanner, seed);
                boolean isRun = true;
                while (isRun) {
                    try {
                        statMessage = clientInterpreter.interpret();
                    } catch (NoSuchElementException e) {
                        return;
                    }
                    switch (statMessage) {
                        case "Possible changing state":
                            if (type == ClientType.Leecher) {
                                type = ClientType.Seeder;
                                seeder.start();
                                updateTimer.schedule(timerTask, 0, UPDATE_TIMEOUT);
                            }
                            break;
                        case "false":
                            toSeed.close();
                            statMessage = "true";
                            isRun = false;
                    }
                }
            }
            switch (statMessage) {
                case "Possible changing state":
                    if (type == ClientType.Leecher) {
                        type = ClientType.Seeder;
                        seeder.start();
                        updateTimer.schedule(timerTask, 0, UPDATE_TIMEOUT);
                    }
                    break;
                case "false":
                    close();
            }
        }
    }

    @Override
    public void close() {
        try {
            clientSocket.close();
            if (type == ClientType.Seeder) {
                seeder.tryToStop();
                TimeUnit.SECONDS.sleep(2);
                seeder.close();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (type == ClientType.Seeder) {
                String name = storingDirectory.toString() + "/" + "client" + String.valueOf(port);
                try {
                    File store = new File(name);
                    store.createNewFile();
                    seed.writeToFile(new DataOutputStream(new FileOutputStream(name)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            timerTask.cancel();
            updateTimer.cancel();
        }
    }
}
