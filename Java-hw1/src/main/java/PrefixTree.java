public class PrefixTree implements Trie {

    static int upperIndOfCharTable = 127;

    static class Vertex {
        Vertex[] next = new Vertex[upperIndOfCharTable + 1];
        int postfixCount = 0;
        boolean terminal;
    }

    private Vertex root;
    private int count;

    public PrefixTree() {
        root = new Vertex();
        count = 0;
    }

    public boolean add(String element) {
        if (element == "")
            return false;
        Vertex curr = root;
        // this terrible atavism is included only to check invalid characters
        boolean flag = false;
        for (char ch: element.toCharArray()) {
            if (!Character.isLetter(ch) || ch > upperIndOfCharTable)
                return  false;
            if (curr.next[ch] == null) {
                flag = true;
                break;
            }
            curr = curr.next[ch];
        }
        if (!flag && curr.terminal)
            return false;

        curr = root;
        for (char ch: element.toCharArray()) {
            if (curr.next[ch] != null) {
                curr = curr.next[ch];
                curr.postfixCount++;
                continue;
            }
            curr = curr.next[ch] = new Vertex();
            curr.postfixCount++;
        }
        count++;
        curr.terminal = true;
        return  true;
    }

    public boolean contains(String element) {
        if (element == "")
            return false;
        Vertex curr = root;
        for (char ch: element.toCharArray()) {
            if (!Character.isLetter(ch) || ch > upperIndOfCharTable || curr.next[ch] == null)
                return  false;
            curr = curr.next[ch];
        }
        if (!curr.terminal)
            return false;
        return true;
    }

    public boolean remove(String element) {
        if (!contains(element))
            return false;
        Vertex curr = root;
        for (char ch: element.toCharArray()) {
            if (curr.postfixCount == 0) {
                Vertex pred = curr;
                curr = curr.next[ch];
                curr.postfixCount--;
                pred = null;
                continue;
            }
            curr = curr.next[ch];
            curr.postfixCount--;
        }
        curr.terminal = false;
        if (curr.postfixCount == 0) {
            curr = null;
        }
        count--;
        return true;
    }

    public int size() {
        return count;
    }

    public int howManyStartsWithPrefix(String prefix) {
        if (prefix == "")
            return count;
        Vertex curr = root;
        for (char ch: prefix.toCharArray()) {
            if (!Character.isLetter(ch) || ch > upperIndOfCharTable || curr.next[ch] == null)
                return  0;
            curr = curr.next[ch];
        }
        return curr.postfixCount;
    }
}