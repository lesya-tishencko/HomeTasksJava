package ru.spbau.java.tishencko;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new Server(21, 20);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            if (command == ":q")
                break;
        }
    }
}
