package ru.spbau.java.tishencko.utils;

public class FtpQuery {
    private final QueryType type;
    private final String path;

    FtpQuery(QueryType type, String path) {
        this.type = type;
        this.path = path;
    }

    public QueryType getType() {
        return type;
    }

    public String getPath() {
        return path;
    }
}
