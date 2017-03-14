public class PrefixTree implements Trie {

    private static int upperIndOfCharTable = 127;

    private static class Vertex {
        Vertex[] next = new Vertex[upperIndOfCharTable + 1];
        int postfixCount = 0;
        boolean terminal;
    }

    private Vertex root;

    public PrefixTree() {
        root = new Vertex();
    }

    public boolean add(String element) {
        if (element.isEmpty())
            return false;
        Vertex curr = root;
        // this terrible atavism is included only to check invalid characters
        boolean notAdded = false;
        for (char ch: element.toCharArray()) {
            if (!Character.isLetter(ch) || ch > upperIndOfCharTable)
                return  false;
            if (curr.next[ch] == null) {
                notAdded = true;
                break;
            }
            curr = curr.next[ch];
        }
        if (!notAdded && curr.terminal)
            return false;

        curr = root;
        for (char ch: element.toCharArray()) {
            if (curr.next[ch] == null) {
                curr.next[ch] = new Vertex();
            }
            curr.postfixCount++;
            curr = curr.next[ch];
        }
        curr.postfixCount++;
        curr.terminal = true;
        return  true;
    }

    public boolean contains(String element) {
        if (element.isEmpty())
            return false;
        Vertex curr = root;
        for (char ch: element.toCharArray()) {
            if (!Character.isLetter(ch) || ch > upperIndOfCharTable || curr.next[ch] == null)
                return  false;
            curr = curr.next[ch];
        }
        return curr.terminal;
    }

    public boolean remove(String element) {
        if (!contains(element))
            return false;
        Vertex curr = root;
        curr.postfixCount--;
        for (char ch: element.toCharArray()) {
            curr.next[ch].postfixCount--;
            if (curr.next[ch].postfixCount == 0) {
                curr.next[ch] = null;
                return true;
            }
            curr = curr.next[ch];
        }
        curr.terminal = false;
        return true;
    }

    public int size() {
        return root.postfixCount;
    }

    public int howManyStartsWithPrefix(String prefix) {
        if (prefix.isEmpty())
            return root.postfixCount;
        Vertex curr = root;
        for (char ch: prefix.toCharArray()) {
            if (!Character.isLetter(ch) || ch > upperIndOfCharTable || curr.next[ch] == null)
                return  0;
            curr = curr.next[ch];
        }
        return curr.postfixCount;
    }
}