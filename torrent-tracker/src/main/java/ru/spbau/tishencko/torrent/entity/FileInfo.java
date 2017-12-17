package ru.spbau.tishencko.torrent.entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FileInfo {
    int id;
    long size;
    String name;

    public FileInfo(int id, String name, long size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

    public void writeToFile(DataOutputStream file) throws IOException {
        file.writeInt(id);
        file.writeUTF(name);
        file.writeLong(size);
    }

    public FileInfo(DataInputStream file) throws IOException {
        id = file.readInt();
        name = file.readUTF();
        size = file.readLong();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }
}