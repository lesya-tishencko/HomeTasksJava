package ru.spbau.tishencko.torrent.entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Seed {
    private final byte[] ip;
    private short port;

    private final Map<Integer, File> files = new HashMap<>();

    public Seed(byte[] ip, short port) {
        this.ip = ip;
        this.port = port;
    }

    public void addFile(File file) {
        files.put(file.getId(), file);
    }

    public void writeToFile(DataOutputStream file) throws IOException {
        file.write(ip);
        file.writeShort(port);
        file.writeInt(files.size());
        for (File _file: files.values()) {
            _file.writeToFile(file);
        }
    }

    public Seed(DataInputStream file) throws IOException {
        ip = new byte[4];
        for (int i = 0; i < 4; i++) {
            ip[4] = file.readByte();
        }
        port = file.readShort();
        int size = file.readInt();
        for (int i = 0; i < size; i++) {
            File _file = new File(file);
            files.put(_file.getId(), _file);
        }
    }

    public File findFile(int id) {
        return files.getOrDefault(id, null);
    }

    public byte[] getIp() {
        return ip;
    }

    public short getPort() {
        return port;
    }

    public int getCount() {
        return files.size();
    }

    public int[] getFileIds() {
        return files.values().stream().mapToInt(File::getId).toArray();
    }
}
