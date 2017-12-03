package ru.spbau.java.tishencko;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

class Client implements AutoCloseable {
    private Socket clientSocket;
    private final String host;
    private final int port;
    CommandInterpreter interpreter;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect(Scanner scanner) throws IOException {
        String workingDirectory = System.getProperty("user.dir") + "/ftpclient";
        connect(scanner, workingDirectory);
    }

    public void connect(Scanner scanner, String workingDirectory) throws IOException {
        clientSocket = new Socket(host, port);
        File workDirectory = new File(workingDirectory);
        workDirectory.mkdirs();
        interpreter = new CommandInterpreter(clientSocket, workingDirectory, scanner);
    }

    public void run() throws IOException {
        boolean isContinue = true;
        while (isContinue) {
            interpreter.interpret();
        }
        close();
    }

    @Override
    public void close() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try (Client client = new Client("localhost", 4999)) {
            client.connect(new Scanner(System.in));
            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
