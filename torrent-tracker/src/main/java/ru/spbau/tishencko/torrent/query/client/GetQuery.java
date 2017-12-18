package ru.spbau.tishencko.torrent.query.client;

import ru.spbau.tishencko.torrent.answer.client.GetAnswer;
import ru.spbau.tishencko.torrent.entity.Part;
import ru.spbau.tishencko.torrent.entity.Seed;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GetQuery extends Query {
    private int part;
    private Seed seed;

    public GetQuery(int id, int part) {
        this.id = id;
        this.part = part;
    }

    public GetQuery(Seed seed) {
        this.seed = seed;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeByte(2);
        out.writeInt(id);
        out.writeInt(part);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        id = in.readInt();
        part = in.readInt();
    }

    @Override
    public GetAnswer execute() {
        Part partOfFile = seed.findFile(id).getPart(part);
        return new GetAnswer(partOfFile.getContent());
    }
}
