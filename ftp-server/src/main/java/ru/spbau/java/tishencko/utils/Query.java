package ru.spbau.java.tishencko.utils;

public class Query {
    private int type;
    private String path;

    public Query(int type, String path) {
        this.type = type;
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public String getPath() {
        return path;
    }
}
