package ru.spbau.java.tishencko.handlers;

import ru.spbau.java.tishencko.utils.GetAnswer;
import ru.spbau.java.tishencko.utils.ListAnswer;
import ru.spbau.java.tishencko.utils.Query;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CommandHandler {
    private String workingDirectory;
    private final DataInputStream in;
    private final DataOutputStream out;

    public CommandHandler(Socket socket, String workingDirectory) throws IOException {
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        this.workingDirectory = workingDirectory;
    }

    public void executeCommand() throws IOException {
        Query query = readQuery();
        switch (query.getType()) {
            case 1: executeListCommand(query.getPath());
                        break;
            case 2: executeGetCommand(query.getPath());
                         break;
            default: writeError("Type of query undefined");
        }
    }

    private Query readQuery() throws IOException {
        return new Query(in.readInt(), in.readUTF());
    }

    private void writeError(String message) throws IOException {
        out.writeChars(message);
        out.flush();
    }

    private void executeListCommand(String path) throws IOException {
        File directory = new File(workingDirectory + "/path");
        File[] listFiles = directory.listFiles();
        int size = listFiles.length;
        List<String> names = new ArrayList<>();
        List<Boolean> isDirectoryMarks = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            names.add(listFiles[i].getAbsolutePath());
            isDirectoryMarks.add(listFiles[i].isDirectory());
        }
        writeListAnswer(new ListAnswer(size, names, isDirectoryMarks));
    }

    private void writeListAnswer(ListAnswer listAnswer) throws IOException {
        int size = listAnswer.getSize();
        out.writeInt(size);
        List<String> names = listAnswer.getNames();
        List<Boolean> isDirectoryMarks = listAnswer.getIsDirectoryMarks();
        for (int i = 0; i < size; i++) {
            out.writeChars(names.get(i));
            out.writeBoolean(isDirectoryMarks.get(i));
        }
        out.flush();
    }

    private void executeGetCommand(String path) throws IOException {
        File file = new File(workingDirectory + "/path");
        long size = file.exists() ? file.length() : 0;
        byte[] bytes = new byte[0];
        if (size > 0) {
            bytes = Files.readAllBytes(Paths.get(workingDirectory + "/path"));
        }
        writeGetAnswer(new GetAnswer(size, bytes));
    }

    private void writeGetAnswer(GetAnswer getAnswer) throws IOException {
        out.writeLong(getAnswer.getSize());
        out.write(getAnswer.getBytes());
        out.flush();
    }
}
