package ru.spbau.java.tishencko.utils;

public class ListFtpQuery extends FtpQuery {
    public ListFtpQuery(String path) {
        super(QueryType.ListType, path);
    }
}
