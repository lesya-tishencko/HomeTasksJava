package ru.spbau.tishencko.torrent.client;

import ru.spbau.tishencko.torrent.answer.client.Answer;
import ru.spbau.tishencko.torrent.answer.client.GetAnswer;
import ru.spbau.tishencko.torrent.answer.client.StatAnswer;
import ru.spbau.tishencko.torrent.answer.tracker.ListAnswer;
import ru.spbau.tishencko.torrent.answer.tracker.SourcesAnswer;
import ru.spbau.tishencko.torrent.answer.tracker.UpdateAnswer;
import ru.spbau.tishencko.torrent.answer.tracker.UploadAnswer;
import ru.spbau.tishencko.torrent.entity.File;
import ru.spbau.tishencko.torrent.entity.Seed;
import ru.spbau.tishencko.torrent.query.client.GetQuery;
import ru.spbau.tishencko.torrent.query.client.StatQuery;
import ru.spbau.tishencko.torrent.query.tracker.ListQuery;
import ru.spbau.tishencko.torrent.query.tracker.SourcesQuery;
import ru.spbau.tishencko.torrent.query.tracker.UpdateQuery;
import ru.spbau.tishencko.torrent.query.tracker.UploadQuery;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientInterpreter {
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
            case ":list":
                ListQuery list = new ListQuery();
                list.write(out);
                ListAnswer listAnswer = new ListAnswer(new PrintWriter(System.out));
                listAnswer.read(in);
                listAnswer.execute();
                return String.valueOf(true);
            case ":upload":
                String name = scanner.next();
                java.io.File newFile = new java.io.File(name);
                long size = newFile.length();
                File file;
                try {
                    file = new File(name, size);
                } catch (IOException e) {
                    System.out.println("File with this name doesn't exist");
                    return String.valueOf(true);
                }
                UploadQuery upload = new UploadQuery(name, size);
                upload.write(out);
                UploadAnswer uploadAnswer = new UploadAnswer(seed, file);
                uploadAnswer.read(in);
                uploadAnswer.execute();
                return "Possible changing state";
            case ":sources":
                try {
                    id = Integer.valueOf(scanner.next());
                } catch (NumberFormatException e) {
                    System.out.println("Unknown type of file's id");
                    return String.valueOf(true);
                }
                SourcesQuery sources = new SourcesQuery(id);
                sources.write(out);
                SourcesAnswer sourcesAnswer = new SourcesAnswer(new PrintWriter(System.out));
                sourcesAnswer.read(in);
                sourcesAnswer.execute();
                return String.valueOf(true);
            default:
                System.out.println("Unknown command");
                return String.valueOf(true);
        }
    }

    public void executeUpdate() throws IOException {
        UpdateQuery update = new UpdateQuery(seed.getPort(), seed.getCount(), seed.getFileIds());
        update.write(out);
        UpdateAnswer updateAnswer = new UpdateAnswer(new PrintWriter(System.out));
        updateAnswer.read(in);
        updateAnswer.execute();
    }
}
