package ru.spbau.tishencko.torrent.answer.client;

import ru.spbau.tishencko.torrent.entity.Seed;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Writer;

public class StatAnswer extends Answer {
    private int count;
    private int[] part;
    private Writer writer;

    public StatAnswer(int count, int[] part) {
        this.count = count;
        this.part = part;
    }

    public StatAnswer(Seed seed, Writer writer) {
        this.seed = seed;
        this.writer = writer;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(count);
        for (int i = 0; i < count; i++) {
            out.writeInt(part[i]);
        }
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        count = in.readInt();
        part = new int[count];
        for (int i = 0; i < count; i++) {
            part[i] = in.readInt();
        }
    }

    @Override
    public void execute() throws IOException {
        for (int i = 0; i < count; i++) {
            writer.write(part[i]);
        }
        writer.flush();
    }
}
