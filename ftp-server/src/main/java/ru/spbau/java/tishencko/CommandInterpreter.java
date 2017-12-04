package ru.spbau.java.tishencko;

import ru.spbau.java.tishencko.utils.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class CommandInterpreter {
    private final Scanner scanner;
    private DataInputStream in;
    private DataOutputStream out;
    private final String workingDirectory;

    public  CommandInterpreter(Socket socket, String workingDirectory, Scanner scanner)
            throws IOException {
        this.scanner = scanner;
        this.workingDirectory = workingDirectory;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public boolean interpret() throws IOException {
        FtpAnswer ftpAnswer;
        String command = scanner.next();
        switch (command) {
            case ":q":
                return false;
            case ":list":
                ftpAnswer = executeList(scanner.next());
                ftpAnswer.println(new PrintWriter(System.out));
                return true;
            case ":get":
                ftpAnswer = executeGet(scanner.next());
                System.out.println("Input file name: ");
                ftpAnswer.println(new PrintWriter(workingDirectory + "/" + scanner.next()));
                return true;
            default:
                System.out.println("Unknown command");
                return true;
        }
    }

    ListFtpAnswer executeList(String path) throws IOException {
        writeQuery(new ListFtpQuery(path));
        return readListAnswer();
    }

    GetFtpAnswer executeGet(String path) throws IOException {
        writeQuery(new GetFtpQuery(path));
        return readGetAnswer();
    }

    private void writeQuery(FtpQuery ftpQuery) throws IOException {
        out.writeInt(ftpQuery.getType().ordinal());
        out.writeUTF(ftpQuery.getPath());
    }

    private ListFtpAnswer readListAnswer() throws IOException {
        int size = in.readInt();
        List<String> names = new ArrayList<>();
        List<Boolean> isDirectoryMarks = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            names.add(in.readUTF());
            isDirectoryMarks.add(in.readBoolean());
        }
        return new ListFtpAnswer(size, names, isDirectoryMarks);
    }

    private GetFtpAnswer readGetAnswer() throws IOException {
        long size = in.readLong();
        byte[] bytes = new byte[(int) size];
        in.read(bytes);
        return new GetFtpAnswer(size, bytes);
    }
}
