package ru.spbau.java.tishencko;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Server implements AutoCloseable {
    private final int port;
    private final String workingDirectory;
    private final ExecutorService clientPool;

    Server(int portNumber, int maxNumberOfClients, String workingDirectory) {
        port = portNumber;
        this.workingDirectory = workingDirectory;
        File workDirectory = new File(workingDirectory);
        workDirectory.mkdirs();
        clientPool = Executors.newFixedThreadPool(maxNumberOfClients);
    }

    public void start() {
        Runnable serverTask = () -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    Runnable task = () -> {
                        try {
                            CommandHandler handler = new CommandHandler(clientSocket, workingDirectory);
                            while (true) {
                                handler.executeCommand();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    };
                    clientPool.submit(task);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Thread serverThread = new Thread(serverTask);
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

    public static void main(String[] args) {
        Server server = new Server(4999, 20, System.getProperty("user.dir") + "/ftp");
        server.start();
    }
}
