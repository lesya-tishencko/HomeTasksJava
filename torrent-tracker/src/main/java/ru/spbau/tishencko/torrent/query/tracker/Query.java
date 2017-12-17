package ru.spbau.tishencko.torrent.query.tracker;

import ru.spbau.tishencko.torrent.answer.tracker.Answer;
import ru.spbau.tishencko.torrent.entity.TrackerDatabase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Query {
    protected TrackerDatabase tracker;

    public abstract void write(DataOutputStream out) throws IOException;

    public abstract void read(DataInputStream in) throws IOException;

    public abstract Answer execute() throws IOException;
}
