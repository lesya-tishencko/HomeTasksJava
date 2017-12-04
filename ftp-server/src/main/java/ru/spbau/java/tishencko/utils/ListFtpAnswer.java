package ru.spbau.java.tishencko.utils;

import java.io.PrintWriter;
import java.util.List;

public class ListFtpAnswer extends FtpAnswer {
    private final int size;
    private final List<String> names;
    private final List<Boolean> isDirectoryMarks;

    public ListFtpAnswer(int size, List<String> names, List<Boolean> isDirectoryMarks) {
        this.size = size;
        this.names = names;
        this.isDirectoryMarks = isDirectoryMarks;
    }

    public int getSize() {
        return size;
    }

    public List<String> getNames() {
        return names;
    }

    public List<Boolean> getIsDirectoryMarks() {
        return isDirectoryMarks;
    }

    public void println(PrintWriter printStream) {
        printStream.println(size);
        for (int i = 0; i < size; i++) {
            printStream.println(names.get(i) + " is dir: " + String.valueOf(isDirectoryMarks.get(i)));
        }
        printStream.flush();
    }
}
