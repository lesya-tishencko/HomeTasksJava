package ru.spbau.tishencko.torrent.query.tracker;

import ru.spbau.tishencko.torrent.answer.tracker.ListAnswer;
import ru.spbau.tishencko.torrent.entity.FileInfo;
import ru.spbau.tishencko.torrent.storer.TrackerDatabase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

public class ListQuery extends Query {
    public ListQuery(TrackerDatabase tracker) {
        this.tracker = tracker;
    }

    public ListQuery() {

    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeByte(1);
    }

    @Override
    public void read(DataInputStream in) throws IOException {

    }

    @Override
    public ListAnswer execute() {
        Set<FileInfo> files = tracker.getFiles();
        int[] id = new int[files.size()];
        String[] name = new String[files.size()];
        long[] size = new long[files.size()];
        int i = 0;
        for (FileInfo info : files) {
            id[i] = info.getId();
            name[i] = info.getName();
            size[i] = info.getSize();
            i++;
        }
        return new ListAnswer(files.size(), id, name, size);
    }
}
