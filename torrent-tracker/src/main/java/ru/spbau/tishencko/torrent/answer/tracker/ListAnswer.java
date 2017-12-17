package ru.spbau.tishencko.torrent.answer.tracker;

import java.io.*;

public class ListAnswer extends Answer {
    private int count;
    private int[] id;
    private String[] name;
    private long[] size;

    private Writer writer;

    public ListAnswer(int count, int[] id, String[] name, long[] size) {
        this.count = count;
        this.id = id;
        this.name = name;
        this.size = size;
    }

    public ListAnswer() {

    }

    public ListAnswer(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(count);
        for (int i = 0; i < count; i++) {
            out.writeInt(id[i]);
            out.writeUTF(name[i]);
            out.writeLong(size[i]);
        }
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        count = in.readInt();
        id = new int[count];
        name = new String[count];
        size = new long[count];
        for (int i = 0; i < count; i++) {
            id[i] = in.readInt();
            name[i] = in.readUTF();
            size[i] = in.readLong();
        }
    }

    @Override
    public void execute() throws IOException {
        for (int i = 0; i < count; i++) {
            writer.write(id[i]);
            writer.write(name[i]);
            writer.write((int)size[i]);
            writer.flush();
        }
    }
}
