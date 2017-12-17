package ru.spbau.tishencko.torrent.client;

import ru.spbau.tishencko.torrent.entity.Seed;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client implements AutoCloseable {
    private Socket clientSocket;
    private ClientType type;
    private final String host;
    private final int port;
    private Path storingDirectory;
    private Seed seed;
    private ClientInterpreter interpreter;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        type = ClientType.Leecher;
    }

    public void connect(Scanner scanner, Path storingDirectory) throws IOException {
        clientSocket = new Socket(host, port);
        this.storingDirectory = storingDirectory;
        File file = new File(this.storingDirectory.toString());
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
        interpreter = new ClientInterpreter(clientSocket, scanner, seed);
    }

    public void run() throws IOException {
        while (true) {
            String statMessage = interpreter.interpret();
            switch (statMessage) {
                case "Possible changing state":
                    if (type == ClientType.Leecher) {
                        type = ClientType.Seeder;
                    }
                    break;
                case "false":
                    close();
            }
            if (clientSocket.isClosed()) return;
        }
    }

    @Override
    public void close() {
        try {
            clientSocket.close();
        } catch (IOException e) {
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
        }
    }
}
