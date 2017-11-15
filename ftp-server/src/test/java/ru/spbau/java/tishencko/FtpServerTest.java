package ru.spbau.java.tishencko;

import org.junit.*;
import ru.spbau.java.tishencko.utils.GetAnswer;
import ru.spbau.java.tishencko.utils.ListAnswer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class FtpServerTest {
    private static Server server = new Server(4999, 20);

    @BeforeClass
    public static void initialize() {
        server.start();
    }

    @AfterClass
    public static void close() throws IOException {
        server.close();
    }

    @Test
    public void testListCommand() throws Exception {
        Client client = new Client("localhost", 4999);
        client.connect();
        ListAnswer listAnswer = client.executeList("main");
        client.close();
        assertEquals(2, listAnswer.getSize());
        assertTrue(listAnswer.getNames().contains("hello.txt"));
        assertTrue(listAnswer.getNames().contains("goodbye.txt"));
        assertTrue(listAnswer.getNames().contains("directory"));
        assertFalse(listAnswer.getIsDirectoryMarks().get(1));
        assertFalse(listAnswer.getIsDirectoryMarks().get(2));
        assertTrue(listAnswer.getIsDirectoryMarks().get(0));
    }

    @Test
    public void testGetCommand() throws Exception {
        Client client = new Client("localhost", 4999);
        client.connect();
        GetAnswer getAnswer = client.executeGet("main/goodbye.txt");
        client.close();
        File file = new File(System.getProperty("user.dir") + "/ftp/main/goodbye.txt");
        assertEquals(file.length(), getAnswer.getSize());
        assertEquals(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/ftp/main/goodbye.txt")), getAnswer.getBytes());
    }
}