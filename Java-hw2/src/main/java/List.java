public class List {

    public static class Pair {
        String key = "", value = "";

        public Pair(Node node) {
            key = node.key;
            value = node.value;
        }
    }

    private static class Node {
        String key = "", value = "";
        Node next = null;
    }

    private Node root = new Node();

    public String add(String key, String value) {
        Node current = root;
        while (current.next != null && !current.key.equals(key))
            current = current.next;

        if (current.next == null) {
            current.key = key;
            current.value = value;
            current.next = new Node();
            return null;
        }

        String oldValue = current.value;
        current.value = value;
        return oldValue;
    }

    public String delete(String key) {
        String value = null;
        if (root.key.equals(key)) {
            value = root.value;
            if (root.next != null)
                root = root.next;
            else
                root.key = root.value = "";
            return value;
        }

        Node current = root;
        while (current.next != null && !current.next.key.equals(key))
            current = current.next;

        if (current.next == null)
            return value;
        value = current.next.value;
        current.next = current.next.next;
        return value;
    }

    public String find(String key) {
        Node current = root;
        while (current.next != null && !current.key.equals(key))
            current = current.next;

        if (current.value.isEmpty())
            return null;

        return current.value;
    }

    public void eraseAll() {
        root.key = root.value = "";
        root.next = null;
    }

    public Pair getNextAndRemove() {
        if (root.next == null)
            return null;

        Pair result = new Pair(root);
        root = root.next;
        return result;
    }
}