package ru.spbau.tishencko.torrent.main;

import ru.spbau.tishencko.torrent.client.Client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        try (Client client = new Client("localhost",4999)) {
            File workDirectory = new File(System.getProperty("user.dir") + "/torrent");
            workDirectory.mkdirs();
            client.connect(new Scanner(System.in), Paths.get(workDirectory.getAbsolutePath()));
            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
