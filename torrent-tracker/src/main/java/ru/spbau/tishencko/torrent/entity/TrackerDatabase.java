package ru.spbau.tishencko.torrent.entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TrackerDatabase {
    private static final long DEFAULT_TIMEOUT = 5 * 60000;
    private Map<Short, Seed> seeds;
    private Map<Short, Long> history;
    private AtomicInteger globalId;

    public Set<FileInfo> getFiles() {
        return files;
    }

    private Set<FileInfo> files;

    public TrackerDatabase() {
        seeds = new HashMap<>();
        files = new HashSet<>();
        history = new HashMap<>();
        globalId = new AtomicInteger(0);
    }

    public void addSeed(Seed seed) {
        seeds.put(seed.getPort(), seed);
        history.put(seed.getPort(), System.currentTimeMillis());
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
        history = new HashMap<>();
        globalId = new AtomicInteger(size);

        for (int i = 0; i < size; i++) {
            files.add(new FileInfo(file));
        }
    }

    public List<Seed> getAllSeedsForFile(int id) {
        List<Seed> result = new ArrayList<>();
        for (Seed seed : seeds.values()) {
            if (System.currentTimeMillis() - history.get(seed.getPort()) > DEFAULT_TIMEOUT) {
                seeds.remove(seed.getPort());
                history.remove(seed.getPort());
            } else if (seed.findFile(id) != null) {
                result.add(seed);
            }
        }
        return result;
    }

    public boolean updateInfo(int port, int id) {
        Seed seed = seeds.getOrDefault(port, null);
        if (seed == null) return false;
        history.put((short)port, System.currentTimeMillis());
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
