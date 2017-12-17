package ru.spbau.tishencko.torrent.query.tracker;

import ru.spbau.tishencko.torrent.answer.tracker.UpdateAnswer;
import ru.spbau.tishencko.torrent.entity.TrackerDatabase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UpdateQuery extends Query {
    private short port;
    private int count;
    private int[] fileIds;

    public UpdateQuery(short port, int count, int[] fileIds) {
        this.port = port;
        this.count = count;
        this.fileIds = fileIds;
    }

    public UpdateQuery(TrackerDatabase tracker) {
        this.tracker = tracker;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeByte(4);
        out.writeShort(port);
        out.writeInt(count);
        for (int i = 0; i < count; i++) {
            out.writeInt(fileIds[i]);
        }
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        port = in.readShort();
        count = in.readInt();
        fileIds = new int[count];
        for (int i = 0; i < count; i++) {
            fileIds[i] = in.readInt();
        }
    }

    @Override
    public UpdateAnswer execute() {
        for (int i = 0; i < count; i++) {
            if (!tracker.updateInfo(port, fileIds[i])) {
                return new UpdateAnswer(false);
            }
        }
        return new UpdateAnswer(true);
    }
}
