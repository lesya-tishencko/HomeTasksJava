package ru.spbau.tishencko.torrent.tracker;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.spbau.tishencko.torrent.client.Client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class TorrentTest {
    private static final String pathSnapshots = System.getProperty("user.dir") + "\\torrent";
    private static final String pathResources = pathSnapshots + "\\codes.pdf";
    private static final Tracker tracker = new Tracker(20, Paths.get(pathSnapshots));

    @BeforeClass
    public static void initialize() throws InterruptedException {
        tracker.start();
        TimeUnit.SECONDS.sleep(2);
    }

    @AfterClass
    public static void close() throws IOException {
        tracker.close();
        String path = pathSnapshots + "\\tracker";
        File file = new File(path);
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void testUploadCommand() throws Exception {
        String query = ":upload " + pathResources;
        Scanner scanner = new Scanner(new ByteArrayInputStream(query.getBytes()));
        Client peer1 = new Client("localhost", 4999, scanner);
        peer1.connect(Paths.get(pathSnapshots));
        peer1.run();
        TimeUnit.SECONDS.sleep(2);
        assertTrue(tracker.trackerDatabase.getFiles().size() > 0);
        TimeUnit.SECONDS.sleep(2);
        assertEquals(4999, tracker.trackerDatabase.getAllSeedsForFile(0).get(0).getPort());
        peer1.close();
        String path = pathSnapshots + "\\client4999";
        File file = new File(path);
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void testPeerCommunication() throws Exception {
        TimeUnit.SECONDS.sleep(2);
        String query = ":upload " + pathResources;
        Scanner scanner = new Scanner(new ByteArrayInputStream(query.getBytes()));
        Client peer1 = new Client("localhost", 4998, scanner);
        peer1.connect(Paths.get(pathSnapshots));
        peer1.run();
        TimeUnit.SECONDS.sleep(2);

        String query2 = ":seed localhost 4998 \n :get 1 0 \n";
        Scanner scanner2 = new Scanner(new ByteArrayInputStream(query2.getBytes()));
        Client peer2 = new Client("localhost", 4997, scanner2);
        peer2.connect(Paths.get(pathSnapshots));
        peer2.run();

        assertTrue(tracker.trackerDatabase.getFiles().size() > 0);
        assertEquals(2, tracker.trackerDatabase.getAllSeedsForFile(1));
        assertEquals(4998, tracker.trackerDatabase.getAllSeedsForFile(1).get(0).getPort());
        assertEquals(4997, tracker.trackerDatabase.getAllSeedsForFile(1).get(1).getPort());
        assertEquals(2, tracker.trackerDatabase.getAllSeedsForFile(1).get(0).findFile(1).availableParts().size());
        assertEquals(1, tracker.trackerDatabase.getAllSeedsForFile(1).get(1).findFile(1).availableParts().size());

        peer1.close();
        peer2.close();
        String path = pathSnapshots + "\\client4998";
        File file = new File(path);
        assertTrue(file.exists());
        file.delete();
        path = pathSnapshots + "\\client4997";
        file = new File(path);
        assertTrue(file.exists());
        file.delete();
    }
}