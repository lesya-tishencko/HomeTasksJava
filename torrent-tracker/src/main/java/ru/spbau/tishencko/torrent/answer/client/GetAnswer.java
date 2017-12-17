package ru.spbau.tishencko.torrent.answer.client;

import ru.spbau.tishencko.torrent.entity.Seed;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GetAnswer extends Answer {
    private byte[] content;
    private int id;
    private int part;

    public GetAnswer(byte[] content) {
        this.content = content;
    }

    public GetAnswer(Seed seed, int id, int part) {
        this.seed = seed;
        this.id = id;
        this.part = part;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.write(content);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        in.read(content);
    }

    @Override
    public void execute() {
        seed.findFile(id).writePart(part, content);
    }
}
