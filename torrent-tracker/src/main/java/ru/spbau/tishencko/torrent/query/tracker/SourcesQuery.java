package ru.spbau.tishencko.torrent.query.tracker;

import ru.spbau.tishencko.torrent.answer.tracker.SourcesAnswer;
import ru.spbau.tishencko.torrent.entity.Seed;
import ru.spbau.tishencko.torrent.entity.TrackerDatabase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class SourcesQuery extends Query {
    private  int id;

    public SourcesQuery(int id) {
        this.id = id;
    }

    public SourcesQuery(TrackerDatabase tracker) {
        this.tracker = tracker;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeByte(3);
        out.writeInt(id);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        id = in.readInt();
    }

    @Override
    public SourcesAnswer execute() {
        List<Seed> seeds = tracker.getAllSeedsForFile(id);
        byte[][] bytes = new byte[seeds.size()][4];
        short[] ports = new short[seeds.size()];
        for (int i = 0; i < seeds.size(); i++) {
            bytes[i] = seeds.get(i).getIp();
            ports[i] = seeds.get(i).getPort();
        }
        return new SourcesAnswer(seeds.size(), bytes, ports);
    }
}
