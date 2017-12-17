package ru.spbau.tishencko.torrent.answer.client;

import ru.spbau.tishencko.torrent.entity.File;
import ru.spbau.tishencko.torrent.entity.Part;
import ru.spbau.tishencko.torrent.entity.Seed;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class GetAnswer extends Answer {
    private byte[] content;
    private int id;
    private int part;

    public GetAnswer(byte[] content) {
        this.content = content;
    }

    public GetAnswer(Seed seed, int id, int part) {
        this.seed = seed;
        this.id = id;
        this.part = part;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.write(content);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        content = new byte[File.PART_SIZE];
        in.read(content);
        int i = File.PART_SIZE - 1;
        while (i >= 0 && content[i] == 0) {
            i--;
        }
        if (i != File.PART_SIZE - 1) {
            content = Arrays.copyOf(content, i + 1);
        }
    }

    @Override
    public void execute() {
        File file = seed.findFile(id);
        if (file == null) {
            seed.addFile(new File(0));
        }

        seed.findFile(id).writePart(part, content, content.length);
    }
}
