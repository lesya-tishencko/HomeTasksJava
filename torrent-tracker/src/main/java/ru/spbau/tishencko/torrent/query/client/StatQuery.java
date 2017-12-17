package ru.spbau.tishencko.torrent.query.client;

import ru.spbau.tishencko.torrent.answer.client.StatAnswer;
import ru.spbau.tishencko.torrent.entity.Seed;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class StatQuery extends Query {
    private Seed seed;

    public StatQuery(int id) {
        this.id = id;
    }

    @Override
    public StatAnswer execute() {
        List<Integer> result = seed.findFile(id).availableParts();
        return new StatAnswer(result.size(), result.stream().mapToInt(i -> i).toArray());
    }

    public StatQuery(Seed seed) {
        this.seed = seed;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeByte(1);
        out.writeInt(id);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        id = in.readInt();
    }
}
