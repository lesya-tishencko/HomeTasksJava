import java.util.*;

import static java.lang.Integer.max;

/**
 * Created by lesya on 07.04.2017.
 */
public class HashMultiset<E> implements Multiset<E> {
    public HashMultiset() {
        map = new HashMap<E, Integer>(capacity);
    }

    public HashMultiset(Collection<? extends E> collection) {
        capacity = Math.max(collection.size() * 4, capacity);
        map = new HashMap<E, Integer>(capacity);
        for (E elem : collection) {
            Integer count = map.get(elem);
            map.put(elem, count + 1);
            size += 1;
        }
    }

    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    public boolean add(E e) {
        Integer count = map.get(e);
        map.put(e, count + 1);
        size += 1;
        return  true;
    }

    public int size() {
        return size;
    }

    public void clear() {
        map.clear();
    }

    public boolean remove(Object o) {
        Integer count = map.get(o);
        if (count == 0) return false;
        if (count > 1)
            map.put((E)o, count - 1);
        size -= 1;
        return true;
    }

    public int count(Object element) {
        return map.get(element);
    }

    public Set<E> elementSet() {
        return map.keySet();
    }

    final class Entry<E> implements Multiset.Entry<E> {
        Entry<E> after;

        public Entry(E obj, Integer count, Entry<E> next) {
            object = obj;
            count = count;
            after = next;
        }

        public E getElement() {
            return object;
        }

        public int getCount() {
            return count;
        }

        private Integer count = 0;
        private E object;
    }

    public Set<Entry<E>> entrySet() {
        return new LinkedSetEntry();
    }


    private HashMultiset.Entry<E> head;

    public class HashIterator {
        HashMultiset.Entry<E> next;
        HashMultiset.Entry<E> current;

        HashIterator() {
            next = head;
            current = null;
        }

        public final boolean hasNext() {
            return next != null;
        }

        final HashMultiset.Entry<E> nextNode() {
            HashMultiset.Entry<E> e = next;
            current = e;
            next = e.after;
            return e;
        }
    }

    final class LinkedSetEntry extends Set<Entry<E>> {
        public final int size() {
            return size;
        }

        public Iterator<Entry<E>> iterator() {
            return new HashIterator();
        }
    };

    private  HashMap<E, Integer> map;
    private int size = 0;
    private static int capacity = 1000;
}
