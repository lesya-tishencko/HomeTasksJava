package ru.spbau.tishencko.torrent.answer.tracker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Answer {
    public abstract void write(DataOutputStream out) throws IOException;
    public abstract void read(DataInputStream in) throws IOException;

    public abstract void execute() throws IOException;
}
