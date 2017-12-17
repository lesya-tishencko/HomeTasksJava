package ru.spbau.tishencko.torrent.answer.tracker;

import java.io.*;
import java.net.InetAddress;

public class SourcesAnswer extends Answer {
    private int size;
    private byte[][] ip;
    private short[] clientPort;

    private PrintWriter writer;

    public SourcesAnswer(int size, byte[][] ip, short[] clientPort) {
        this.size = size;
        this.ip = ip;
        this.clientPort = clientPort;
    }

    public SourcesAnswer() {

    }

    public SourcesAnswer(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < 4; j++) {
                out.writeByte(ip[i][j]);
            }
            out.writeShort(clientPort[i]);
        }
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        size = in.readInt();
        ip = new byte[size][4];
        clientPort = new short[size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < 4; j++) {
                ip[i][j] = in.readByte();
            }
            clientPort[i] = in.readShort();
        }
    }

    @Override
    public void execute() throws IOException {
        for (int i = 0; i < size; i++) {
            writer.print(InetAddress.getByAddress(ip[i]));
            writer.print(' ');
            writer.println(clientPort[i]);
        }
        writer.flush();
    }
}
