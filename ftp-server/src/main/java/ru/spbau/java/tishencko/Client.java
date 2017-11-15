package ru.spbau.java.tishencko;

import ru.spbau.java.tishencko.utils.GetAnswer;
import ru.spbau.java.tishencko.utils.ListAnswer;
import ru.spbau.java.tishencko.utils.Query;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client implements AutoCloseable {
    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;
    private String host;
    private int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        try {
            clientSocket = new Socket(host, port);
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            close();
        }
    }


    @Override
    public void close() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeList(String path) throws IOException {
        writeQuery(new Query(1, path));
        ListAnswer answer = readListAnswer();
        answer.println();
    }

    public void executeGet(String path) throws IOException {
        writeQuery(new Query(1, path));
        GetAnswer answer = readGetAnswer();
        answer.println();
    }

    private void writeQuery(Query query) throws IOException {
        out.writeInt(query.getType());
        out.writeUTF(query.getPath());
        out.flush();
    }

    public ListAnswer readListAnswer() throws IOException {
        int size = in.readInt();
        List<String> names = new ArrayList<>();
        List<Boolean> isDirectoryMarks = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            names.add(in.readUTF());
            isDirectoryMarks.add(in.readBoolean());
        }
        return new ListAnswer(size, names, isDirectoryMarks);
    }

    public GetAnswer readGetAnswer() throws IOException {
        long size = in.readLong();
        byte[] bytes = new byte[(int) size];
        in.read(bytes);
        return new GetAnswer(size, bytes);
    }

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        try (Client client = new Client("localhost", 21)) {
            client.connect();
            boolean isContinue = true;
            while (isContinue) {
                String command = scanner.next();
                switch (command) {
                    case ":q":
                        isContinue = false;
                    case ":list":
                        client.executeList(scanner.next());
                    case "get":
                        client.executeGet(scanner.next());
                    default:
                        System.out.println("Unknown command");
                }
            }
        } catch (IOException e) {
            System.out.println("Something went wrong");
        }
    }
}
