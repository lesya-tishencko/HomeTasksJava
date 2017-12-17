package ru.spbau.tishencko.torrent.entity;

import java.io.*;
import java.util.*;

public class File {
    public static int PART_SIZE = 10000000;
    private FileInfo info = new FileInfo();
    private List<Part> parts;

    public File(String name, long size) throws IOException {
        info.name = name;
        info.size = size;
        parts = new ArrayList<Part>();
        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(name));
        while (parts.size() * PART_SIZE < size) {
            byte[] part = new byte[PART_SIZE];
            int sizePart = stream.read(part, 0, PART_SIZE);
            parts.add(new Part(part, sizePart));
        }
    }

    public File(int id) {
        info.id = id;
    }

    public void setId(int id) {
        info.id = id;
    }

    public void writePart(int number, byte[] content, int sizePart) {
        parts.set(number, new Part(content, sizePart));
    }

    public int getId() {
        return info.getId();
    }

    public void writeToFile(DataOutputStream file) throws IOException {
        info.writeToFile(file);
        for (int i = 0; i < parts.size(); i++) {
            parts.get(i).writeToFile(file);
        }
    }

    public File(DataInputStream file) throws IOException {
        info = new FileInfo(file);
        parts = new ArrayList<>((int)(info.size / PART_SIZE));
        for (int i = 0; i < parts.size(); i++) {
            parts.set(i, new Part(file));
        }
    }

    public List<Integer> availableParts() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < parts.size(); i++) {
           if (parts.get(i).getSize() > 0) {
               result.add(i);
           }
        }
        return result;
    }

    public Part getPart(int number) {
        return parts.get(number);
    }
}
