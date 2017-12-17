package ru.spbau.tishencko.torrent.query.tracker;

import ru.spbau.tishencko.torrent.answer.tracker.UploadAnswer;
import ru.spbau.tishencko.torrent.entity.TrackerDatabase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UploadQuery extends Query {
    private String name;

    @Override
    public UploadAnswer execute() throws IOException {
        return new UploadAnswer(tracker.uploadFile(name, size));
    }

    private long size;

    public UploadQuery(String name, long size) {
        this.name = name;
        this.size = size;
    }

    public UploadQuery(TrackerDatabase tracker) {
        this.tracker = tracker;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeByte(2);
        out.writeUTF(name);
        out.writeLong(size);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        name = in.readUTF();
        size = in.readLong();
    }
}
