package ru.spbau.tishencko.torrent.query.client;

import ru.spbau.tishencko.torrent.answer.client.Answer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Query {
    int id;

    public abstract void write(DataOutputStream out) throws IOException;

    public abstract void read(DataInputStream in) throws IOException;

    public abstract Answer execute();
}