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
        return head.next.getReference().equals(tail);
    }

    public void append(T value) {
        ListNode<T> newNode = new ListNode<T>(value);
        while (true) {
            RightLeftNodes<T> place = search(value, false);
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
            RightLeftNodes<T> place = search(value, true);
            rightNode = place.right;
            leftNode = place.left;
            if (rightNode.equals(tail) || !rightNode.key.equals(value))
                return false;
            rightNodeNext = rightNode.next.getReference();
            if (rightNode.next.compareAndSet(rightNodeNext, rightNodeNext, false, true))
                break;
        }
        if (!leftNode.next.compareAndSet(rightNode, rightNodeNext, false, false)) {
            search(rightNode.key, true);
        }
        return true;
    }

    public boolean contains(T value) {
        RightLeftNodes<T> place = search(value, true);
        ListNode<T> rightNode = place.right;
        return !(rightNode.equals(tail) || !rightNode.key.equals(value));
    }

    private RightLeftNodes<T> search(T value, boolean forSearch) {
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
                if (curr.equals(tail))
                    break;
                currNext = curr.next.get(mark);
            } while (mark[0] || (forSearch && !curr.key.equals(value)));
                rightNode = curr;

            if (leftNode.next.compareAndSet(leftNodeNext, rightNode, false, false)) {
                if (rightNode.equals(tail) || !rightNode.next.isMarked()) {
                    return new RightLeftNodes<T>(rightNode, leftNode);
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

    private static class RightLeftNodes<U> {
        final ListNode<U> right;
        final ListNode<U> left;

        RightLeftNodes(ListNode<U> right, ListNode<U> left) {
            this.right = right;
            this.left = left;
        }
    }
}
