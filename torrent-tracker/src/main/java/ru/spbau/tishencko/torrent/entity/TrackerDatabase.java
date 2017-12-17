package ru.spbau.tishencko.torrent.entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TrackerDatabase {
    private Map<Short, Seed> seeds;
    private AtomicInteger globalId = new AtomicInteger(0);

    public Set<FileInfo> getFiles() {
        return files;
    }

    private Set<FileInfo> files;

    public TrackerDatabase() {
        seeds = new HashMap<>();
        files = new HashSet<>();
    }

    public void addSeed(Seed seed) {
        seeds.put(seed.getPort(), seed);
    }

    public void removeSeed(Seed seed) {
        seeds.remove(seed.getPort());
    }

    public void addFile(FileInfo info) {
        files.add(info);
    }

    public void writeToFile(DataOutputStream file) throws IOException {
        file.writeInt(files.size());
        for (FileInfo _file: files) {
            _file.writeToFile(file);
        }

    }

    public TrackerDatabase(DataInputStream file) throws IOException {
        int size = file.readInt();
        files = new HashSet<>();
        seeds = new HashMap<>();

        for (int i = 0; i < size; i++) {
            files.add(new FileInfo(file));
        }
    }

    public List<Seed> getAllSeedsForFile(int id) {
        List<Seed> result = new ArrayList<>();
        for (Seed seed : seeds.values()) {
            if (seed.findFile(id) != null) {
                result.add(seed);
            }
        }
        return result;
    }

    public boolean updateInfo(int port, int id) {
        Seed seed = seeds.getOrDefault(port, null);
        if (seed == null) return false;
        if (seed.findFile(id) == null) {
            seed.addFile(new File(id));
        }
        return true;
    }

    public int uploadFile(String name, long size) throws IOException {
        files.add(new FileInfo(globalId.get(), name, size));
        return globalId.getAndIncrement();
    }
}
