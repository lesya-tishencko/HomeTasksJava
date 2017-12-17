package ru.spbau.tishencko.torrent.answer.tracker;

import ru.spbau.tishencko.torrent.entity.*;
import ru.spbau.tishencko.torrent.entity.File;

import java.io.*;

public class UploadAnswer extends Answer {
    private int id;
    private Seed seed;
    private File file;

    public UploadAnswer(int id) {
        this.id = id;
    }

    public UploadAnswer(Seed seed, File file) {
        this.seed = seed;
        this.file = file;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(id);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        id = in.readInt();
    }

    @Override
    public void execute() throws IOException {
        seed.addFile(file);
        file.setId(id);
    }
}
