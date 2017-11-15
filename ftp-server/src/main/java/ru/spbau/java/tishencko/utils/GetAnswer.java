package ru.spbau.java.tishencko.utils;

public class GetAnswer {
    private long size;
    private byte[] bytes;

    public GetAnswer(long size, byte[] bytes) {
        this.size = size;
        this.bytes = bytes;
    }

    public long getSize() {
        return size;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void println() {
        System.out.println(size);
        for (int i = 0; i < (int)size; i++) {
            System.out.print((char)bytes[i]);
        }
    }
}
