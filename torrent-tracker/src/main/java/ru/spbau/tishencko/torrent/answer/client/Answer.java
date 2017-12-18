package ru.spbau.tishencko.torrent.answer.client;

import ru.spbau.tishencko.torrent.entity.Seed;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Answer {
    Seed seed;
    public abstract void write(DataOutputStream out) throws IOException;
    public abstract void read(DataInputStream in) throws IOException;

    public abstract void execute();
}
