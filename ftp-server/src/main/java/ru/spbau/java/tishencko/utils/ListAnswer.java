package ru.spbau.java.tishencko.utils;

import java.util.List;

public class ListAnswer {
    private int size;
    private List<String> names;
    private List<Boolean> isDirectoryMarks;

    public ListAnswer(int size, List<String> names, List<Boolean> isDirectoryMarks) {
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

    public void println() {
        System.out.println(size);
        for (int i = 0; i < size; i++) {
            System.out.println(names.get(i) + " is dir: " + String.valueOf(isDirectoryMarks.get(i)));
        }
    }
}
