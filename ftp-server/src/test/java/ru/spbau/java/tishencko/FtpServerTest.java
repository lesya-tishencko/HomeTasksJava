package ru.spbau.java.tishencko;

import org.junit.*;
import ru.spbau.java.tishencko.utils.GetFtpAnswer;
import ru.spbau.java.tishencko.utils.ListFtpAnswer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class FtpServerTest {
    private static final String pathResources = System.getProperty("user.dir") + "/ftp";
    private static final Server server = new Server(4999, 20, pathResources);
    private static final String mainDirectoryPath = "main";


    @BeforeClass
    public static void initialize() throws InterruptedException {
        server.start();
        TimeUnit.SECONDS.sleep(2);
    }

    @AfterClass
    public static void close() throws IOException {
        server.close();
    }

    @Test
    public void testListCommand() throws Exception {
        Client client = new Client("localhost", 4999);
        client.connect(new Scanner(System.in));
        ListFtpAnswer listAnswer = client.interpreter.executeList(mainDirectoryPath);
        client.close();
        assertEquals(2, listAnswer.getSize());
        assertTrue(listAnswer.getNames().contains("hello.txt"));
        assertTrue(listAnswer.getNames().contains("goodbye.txt"));
        assertFalse(listAnswer.getIsDirectoryMarks().get(1));
        assertFalse(listAnswer.getIsDirectoryMarks().get(0));
    }

    @Test
    public void testGetCommand() throws Exception {
        Client client = new Client("localhost", 4999);
        client.connect(new Scanner(System.in));
        GetFtpAnswer getAnswer = client.interpreter.executeGet(mainDirectoryPath + "/goodbye.txt");
        client.close();
        File file = new File(pathResources + "/" + mainDirectoryPath + "/goodbye.txt");
        assertEquals(file.length(), getAnswer.getSize());
        byte[] bytesExpected = Files.readAllBytes(Paths.get(pathResources + "/" + mainDirectoryPath + "/goodbye.txt"));
        byte[] bytesActual = getAnswer.getBytes();
        for (int i = 0; i < getAnswer.getSize(); i++) {
            assertEquals(bytesExpected[i], bytesActual[i]);
        }
    }

    @Test
    public void testDirectory() throws Exception {
        Client client = new Client("localhost", 4999);
        client.connect(new Scanner(System.in));
        ListFtpAnswer listAnswer = client.interpreter.executeList("/");
        client.close();
        assertEquals(1, listAnswer.getSize());
        assertTrue(listAnswer.getNames().contains(mainDirectoryPath));
        assertTrue(listAnswer.getIsDirectoryMarks().get(0));
    }

    @Test
    public void testNotTextFileExecution() throws Exception {
        Client client = new Client("localhost", 4999);
        client.connect(new Scanner(System.in));
        GetFtpAnswer getAnswer = client.interpreter.executeGet(mainDirectoryPath + "/some.txt");
        assertEquals(0, getAnswer.getSize());
        getAnswer = client.interpreter.executeGet("/");
        assertEquals(0, getAnswer.getSize());
    }
}