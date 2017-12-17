package ru.spbau.tishencko.torrent.answer.tracker;

import java.io.*;

public class UpdateAnswer extends Answer {
    private boolean status;

    public UpdateAnswer(boolean status) {
        this.status = status;
    }

    public UpdateAnswer() {

    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeBoolean(status);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        status = in.readBoolean();
    }

    @Override
    public void execute() throws IOException {

    }
}
