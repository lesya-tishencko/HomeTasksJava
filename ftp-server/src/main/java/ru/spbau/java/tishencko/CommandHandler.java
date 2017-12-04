package ru.spbau.java.tishencko;

import ru.spbau.java.tishencko.utils.*;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class CommandHandler {
    private final String workingDirectory;
    private DataInputStream in;
    private DataOutputStream out;

    public CommandHandler(Socket socket, String workingDirectory) throws IOException {
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        this.workingDirectory = workingDirectory;
    }

    public void executeCommand() throws IOException {
        FtpQuery ftpQuery;
        try {
            ftpQuery = readQuery();
        } catch (EOFException e) {
            return;
        }

        switch (ftpQuery.getType()) {
            case ListType: executeListCommand(ftpQuery.getPath());
                           break;
            case GetType: executeGetCommand(ftpQuery.getPath());
                          break;
            default: writeError("Type of ftpQuery undefined");
        }
    }

    private FtpQuery readQuery() throws IOException {
        int type = in.readInt();
        return type == 0 ?
                new ListFtpQuery(in.readUTF()) :
                new GetFtpQuery(in.readUTF());
    }

    private void writeError(String message) throws IOException {
        out.writeChars(message);
    }

    private void executeListCommand(String path) throws IOException {
        File directory = new File(workingDirectory + "/" + path);
        File[] listFiles = new File[0];
        if (directory.isDirectory()) {
            listFiles = directory.listFiles();
        }
        int size = listFiles.length;
        List<String> names = new ArrayList<>();
        List<Boolean> isDirectoryMarks = new ArrayList<>();
        for (File listFile : listFiles) {
            names.add(listFile.getName());
            isDirectoryMarks.add(listFile.isDirectory());
        }
        writeListAnswer(new ListFtpAnswer(size, names, isDirectoryMarks));
    }

    private void writeListAnswer(ListFtpAnswer listAnswer) throws IOException {
        int size = listAnswer.getSize();
        out.writeInt(size);
        List<String> names = listAnswer.getNames();
        List<Boolean> isDirectoryMarks = listAnswer.getIsDirectoryMarks();
        for (int i = 0; i < size; i++) {
            out.writeUTF(names.get(i));
            out.writeBoolean(isDirectoryMarks.get(i));
        }
    }

    private void executeGetCommand(String path) throws IOException {
        File file = new File(workingDirectory + "/" + path);
        long size = file.exists() ? file.length() : 0;
        byte[] bytes = new byte[0];
        if (size > 0) {
            bytes = Files.readAllBytes(Paths.get(workingDirectory + "/" + path));
        }
        writeGetAnswer(new GetFtpAnswer(size, bytes));
    }

    private void writeGetAnswer(GetFtpAnswer getAnswer) throws IOException {
        out.writeLong(getAnswer.getSize());
        out.write(getAnswer.getBytes());
    }
}
