package ru.spbau.tishencko.torrent.entity;

import java.io.*;
import java.util.*;

public class File {
    public static final int PART_SIZE = 10000000;
    private FileInfo info = new FileInfo();
    private Part[] parts;

    public File(String name, long size) throws IOException {
        info.name = name;
        info.size = size;
        parts = new Part[0];
        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(name));
        while (parts.length * PART_SIZE < size) {
            byte[] part = new byte[PART_SIZE];
            int sizePart = stream.read(part, 0, PART_SIZE);
            parts = Arrays.copyOf(parts, parts.length + 1);
            parts[parts.length - 1] = new Part(part, sizePart);
        }
    }

    public File(int id) {
        info.id = id;
        parts = new Part[0];
    }

    public File(DataInputStream file) throws IOException {
        info = new FileInfo(file);
        parts = new Part[(int)(info.size / PART_SIZE)];
        for (int i = 0; i < parts.length; i++) {
            parts[i] = new Part(file);
        }
    }

    public void writePart(int number, byte[] content, int sizePart) {
        if (parts.length < number) {
            parts = Arrays.copyOf(parts, number + 1);
        }
        parts[number] = new Part(content, sizePart);
    }

    public int getId() {
        return info.getId();
    }

    public void setId(int id) {
        info.id = id;
    }

    public void writeToFile(DataOutputStream file) throws IOException {
        info.writeToFile(file);
        for (Part part : parts) {
            part.writeToFile(file);
        }
    }

    public List<Integer> availableParts() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < parts.length; i++) {
           if (parts[i].getSize() > 0) {
               result.add(i);
           }
        }
        return result;
    }

    public Part getPart(int number) {
        return parts[number];
    }
}
