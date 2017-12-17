package ru.spbau.tishencko.torrent.answer.tracker;

import java.io.*;

public class UpdateAnswer extends Answer {
    private boolean status;
    private Writer writer;

    public UpdateAnswer(boolean status) {
        this.status = status;
    }

    public UpdateAnswer() {

    }

    public UpdateAnswer(PrintWriter printWriter) {
        this.writer = writer;
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
        writer.write(String.valueOf(status));
        writer.flush();
    }
}
