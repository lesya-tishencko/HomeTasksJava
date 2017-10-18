package ru.spbau.java.tishencko;

/**
 * Created by lesya on 17.10.2017.
 */
public interface LockFreeList<T> {
    boolean isEmpty();
    /**
     * Appends value to the end of list
     */
    void append(T value);
    boolean remove(T value);
    boolean contains(T value);
}
