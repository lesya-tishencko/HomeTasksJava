package ru.spbau.tishencko.torrent.client;

import ru.spbau.tishencko.torrent.answer.client.GetAnswer;
import ru.spbau.tishencko.torrent.answer.client.StatAnswer;
import ru.spbau.tishencko.torrent.entity.Seed;
import ru.spbau.tishencko.torrent.query.client.GetQuery;
import ru.spbau.tishencko.torrent.query.client.StatQuery;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class ClientInterpreter {
    private final Scanner scanner;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final Seed seed;

    public ClientInterpreter(Socket socket, Scanner scanner, Seed seed) throws IOException {
        this.scanner = scanner;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        this.seed = seed;
    }

    public String interpret() throws IOException {
        String command = scanner.next();
        int id;
        switch (command) {
            case ":q":
                return String.valueOf(false);
            case ":stat":
                try {
                    id = Integer.valueOf(scanner.next());
                } catch (NumberFormatException e) {
                    System.out.println("Unknown type of file's id");
                    return String.valueOf(true);
                }
                StatQuery stat = new StatQuery(id);
                stat.write(out);
                StatAnswer statAnswer = new StatAnswer(seed, new PrintWriter(System.out));
                statAnswer.read(in);
                statAnswer.execute();
                return String.valueOf(true);
            case ":get":
                int part;
                try {
                    id = Integer.valueOf(scanner.next());
                    part = Integer.valueOf(scanner.next());
                } catch (NumberFormatException e) {
                    System.out.println("Unknown type of file's id or file's part id");
                    return String.valueOf(true);
                }
                GetQuery get = new GetQuery(id, part);
                get.write(out);
                GetAnswer getAnswer = new GetAnswer(seed, id, part);
                getAnswer.read(in);
                getAnswer.execute();
                return "Possible changing state";
            default:
                System.out.println("Unknown command");
                return String.valueOf(true);
        }
    }
}
