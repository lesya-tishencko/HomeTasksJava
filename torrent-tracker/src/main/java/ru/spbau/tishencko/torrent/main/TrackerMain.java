package ru.spbau.tishencko.torrent.main;

import ru.spbau.tishencko.torrent.tracker.Tracker;

import java.io.File;
import java.nio.file.Paths;

class TrackerMain {
    public static void main(String[] args) {
        File workDirectory = new File(System.getProperty("user.dir") + "/torrent");
        workDirectory.mkdirs();
        Tracker tracker = new Tracker(100, Paths.get(workDirectory.getAbsolutePath()));
        tracker.start();
    }
}
