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

class Client implements AutoCloseable {
    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;
    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        clientSocket = new Socket(host, port);
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());
    }


    @Override
    public void close() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ListAnswer executeList(String path) throws IOException {
        writeQuery(new Query(1, path));
        ListAnswer answer = readListAnswer();
        answer.println();
        return answer;
    }

    public GetAnswer executeGet(String path) throws IOException {
        writeQuery(new Query(2, path));
        GetAnswer answer = readGetAnswer();
        answer.println();
        return answer;
    }

    private void writeQuery(Query query) throws IOException {
        out.writeInt(query.getType());
        out.writeUTF(query.getPath());
    }

    private ListAnswer readListAnswer() throws IOException {
        int size = in.readInt();
        List<String> names = new ArrayList<>();
        List<Boolean> isDirectoryMarks = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            names.add(in.readUTF());
            isDirectoryMarks.add(in.readBoolean());
        }
        return new ListAnswer(size, names, isDirectoryMarks);
    }

    private GetAnswer readGetAnswer() throws IOException {
        long size = in.readLong();
        byte[] bytes = new byte[(int) size];
        in.read(bytes);
        return new GetAnswer(size, bytes);
    }

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        try (Client client = new Client("localhost", 4999)) {
            client.connect();
            boolean isContinue = true;
            while (isContinue) {
                String command = scanner.next();
                switch (command) {
                    case ":q":
                        isContinue = false;
                        break;
                    case ":list":
                        client.executeList(scanner.next());
                        break;
                    case ":get":
                        client.executeGet(scanner.next());
                        break;
                    default:
                        System.out.println("Unknown command");
                }
            }
        } catch (IOException e) {
            System.out.println("Something went wrong");
        }
    }
}
