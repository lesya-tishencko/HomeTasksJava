package ru.spbau.java.tishencko;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * Created by lesya on 17.10.2017.
 */
public class List<T> implements LockFreeList<T> {
    private final ListNode<T> head;
    private final ListNode<T> tail;

    public List() {
        head = new ListNode<T>(null);
        tail = new ListNode<T>(null);
        head.next.set(tail, false);
    }

    public boolean isEmpty() {
        Window<T> place = searchWindow(null, true);
        return place.left == head && place.right == tail;
    }

    public void append(T value) {
        ListNode<T> newNode = new ListNode<T>(value);
        while (true) {
            Window<T> place = searchWindow(value, true);
            ListNode<T> rightNode = place.right;
            ListNode<T> leftNode = place.left;
            newNode.next.set(rightNode, false);
            if (leftNode.next.compareAndSet(rightNode, newNode, false, false))
                break;
        }
    }

    public boolean remove(T value) {
        ListNode<T> leftNode, rightNode, rightNodeNext;
        while (true) {
            Window<T> place = searchWindow(value, false);
            rightNode = place.right;
            leftNode = place.left;
            if (rightNode == tail || !rightNode.key.equals(value))
                return false;
            rightNodeNext = rightNode.next.getReference();
            if (rightNode.next.compareAndSet(rightNodeNext, rightNodeNext, false, true))
                break;
        }
        if (!leftNode.next.compareAndSet(rightNode, rightNodeNext, false, false)) {
            searchWindow(rightNode.key, false);
        }
        return true;
    }

    public boolean contains(T value) {
        Window<T> place = searchWindow(value, false);
        ListNode<T> rightNode = place.right;
        return rightNode != tail && rightNode.key.equals(value);
    }

    private Window<T> searchWindow(T value, boolean searchLastElement) {
        ListNode<T> leftNode = null;
        ListNode<T> rightNode;
        ListNode<T> leftNodeNext = null;
        while (true) {
            boolean[] mark = {false};
            ListNode<T> curr = head;
            ListNode<T> currNext = head.next.getReference();
            do {
                if (!curr.next.isMarked()) {
                    leftNode = curr;
                    leftNodeNext = currNext;
                }
                curr = curr.next.getReference();
                if (curr == tail)
                    break;
                currNext = curr.next.get(mark);
            } while (mark[0] || (!searchLastElement && !curr.key.equals(value)));
                rightNode = curr;

            if (leftNode.next.compareAndSet(leftNodeNext, rightNode, false, false)) {
                if (rightNode == tail || !rightNode.next.isMarked()) {
                    return new Window<T>(rightNode, leftNode);
                }
            }
        }

    }

    private static class ListNode<U> {
        final U key;
        final AtomicMarkableReference<ListNode<U>> next;

        ListNode(U key) {
            this.key = key;
            next = new AtomicMarkableReference<ListNode<U>>(null, false);
        }
    }

    private static class Window<U> {
        final ListNode<U> right;
        final ListNode<U> left;

        Window(ListNode<U> right, ListNode<U> left) {
            this.right = right;
            this.left = left;
        }
    }
}
