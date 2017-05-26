package ru.spbau.mit;

import java.util.*;
import java.util.function.UnaryOperator;

/**
 * Created by lesya on 26.05.2017.
 */
@SuppressWarnings("unchecked")
public class SmartList<E> extends AbstractList<E> implements List<E> {
    public SmartList() {
        size = 0;
        data = null;
    }

    public SmartList(Collection<E> collection) {
        for (E elem: collection) {
            add(elem);
        }
    }

    @Override
    public void add(int index, E element) {
        add(element);
        for (int i = size - 1; i > index; i--) {
            set(i, get(i - 1));
        }
        set(index, element);
    }

    @Override
    public boolean add(E e) {
        if (size == 0) {
            data = e;
        }
        if (size == 1) {
            Object[] arr = new Object[5];
            arr[0] = (E)data;
            arr[1] = e;
            data = arr;
        }
        if (size >= 2 && size < 5) {
            ((Object[])data)[size] = e;
        }
        if (size == 5) {
            data = new ArrayList<>(Arrays.asList((E[])data));
            ((ArrayList<E>)data).add(e);
        }
        if (size > 5) {
            ((ArrayList<E>)data).add(e);
        }
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        E result = null;
        if (size == 1 && index == 0) {
            result = (E)data;
            data = null;
        }
        if (size == 2 && index >= 0 && index < 2) {
            result = ((E[])data)[index];
            data = index == 1 ? ((E[])data)[0] : ((E[])data)[1];
        }
        if (size > 2 && size <= 5 && index >= 0 && index < 5) {
            E[] arr = (E[]) data;
            result = arr[index];
            System.arraycopy(arr, index + 1, arr, index, arr.length - 1 - index);
            data = arr;
        }
        if (size == 6 && index >= 0 && index < 6) {
            result = ((ArrayList<E>)data).remove(index);
            data = ((ArrayList<E>)data).toArray();
        }
        if (size > 6 && index >= 0) {
            result = ((ArrayList<E>)data).remove(index);
        }
        size--;
        if (result == null)
            throw new NoSuchElementException();
        return result;
    }

    @Override
    public E get(int index) {
        E result = null;
        if (size == 1 && index == 0) {
            result = (E)data;
        }
        if (size >= 2 && size <= 5 && index >= 0 && index < 5) {
            result = ((E[])data)[index];
        }
        if (size > 5 && index >= 0) {
            result = ((ArrayList<E>)data).remove(index);
        }
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E set(int index, E element) {
        E result = null;
        if (size == 1 && index == 0) {
            result = (E)data;
            data = element;
        }
        if (size >= 2 && size <= 5 && index >= 0 && index < 5) {
            result = ((E[])data)[index];
            ((E[])data)[index] = element;
        }
        if (size > 5 && index >= 0) {
            result = ((ArrayList<E>)data).set(index, element);
        }
        return result;
    }

    private int size;
    private Object data;
}

