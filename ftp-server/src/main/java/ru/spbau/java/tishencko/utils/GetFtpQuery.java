package ru.spbau.java.tishencko.utils;

public class GetFtpQuery extends FtpQuery {
    public GetFtpQuery(String path) {
        super(QueryType.GetType, path);
    }
}
