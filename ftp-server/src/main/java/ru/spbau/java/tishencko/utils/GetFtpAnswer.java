package ru.spbau.java.tishencko.utils;

import java.io.PrintWriter;

public class GetFtpAnswer extends FtpAnswer {
    private final long size;
    private final byte[] bytes;

    public GetFtpAnswer(long size, byte[] bytes) {
        this.size = size;
        this.bytes = bytes;
    }

    public long getSize() {
        return size;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void println(PrintWriter printStream) {
        for (int i = 0; i < (int)size; i++) {
            printStream.print((char)bytes[i]);
        }
        printStream.flush();
    }
}
