package ru.spbau.java.tishencko;

import ru.spbau.java.tishencko.handlers.CommandHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class Server implements AutoCloseable {
    private int port;
    private String workingDirectory;
    private ExecutorService clientPool;
    private Thread serverThread;
    private ServerSocket serverSocket;

    Server(int portNumber, int maxNumberOfClients) {
        port = portNumber;
        workingDirectory = System.getProperty("user.dir") + "/ftp";
        clientPool = Executors.newFixedThreadPool(maxNumberOfClients);
    }

    public synchronized void start() {
        if (!clientPool.isShutdown()) {
            try {
                serverSocket = new ServerSocket(port);
                serverThread = new Thread(() -> {
                    try {
                        serverTask();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                serverThread.start();
            } catch (IOException e) {
                stop();
                serverSocket = null;
                serverThread = null;
            }
        }
    }

    private void stop() {
        try {
            serverSocket.close();
        } catch (IOException ignored) {
        }
        if (serverThread != null) {
            serverThread.interrupt();
        }
    }

    private void serverTask() throws IOException {
        while (!Thread.interrupted()) {
            Socket socket = serverSocket.accept();
            try {
                Runnable task = () -> {
                    try (Socket currentSocket = socket) {
                        CommandHandler handler = new CommandHandler(socket, workingDirectory);
                        while (true) {
                            handler.executeCommand();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };
                clientPool.submit(task);
            } catch (RejectedExecutionException e) {
                socket.close();
            }
        }
    }

    @Override
    public void close() throws IOException {
        synchronized (this) {
            if (clientPool.isShutdown() || serverThread == null) {
                return;
            }
            stop();
            clientPool.shutdown();

            try {
                while (serverSocket != null) {
                    wait();
                }
                if (serverThread != null) {
                    serverThread.join();
                }
            } catch (InterruptedException ignored) {
            } finally {
                serverThread = null;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server(21, 20);
        server.start();
    }
}
