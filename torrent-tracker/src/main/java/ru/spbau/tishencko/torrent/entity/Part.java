package ru.spbau.tishencko.torrent.entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Part {
    public byte[] getContent() {
        return content;
    }

    private byte[] content;
    private int size;

    public Part(byte[] content, int size) {
        this.size = size;
        this.content = content;
    }

    public Part(DataInputStream file) throws IOException {
        size = file.readInt();
        content = new byte[size];
        for (int i = 0; i < size; i++) {
            content[i] = file.readByte();
        }
    }

    public int getSize() {
        return size;
    }

    public void writeToFile(DataOutputStream file) throws IOException {
        file.writeInt(size);
        file.write(content);
    }
}