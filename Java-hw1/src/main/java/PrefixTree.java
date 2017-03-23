import java.io.*;
import java.util.Vector;

public class PrefixTree implements Trie, StreamSerializable {

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

    private static void writeNode(DataOutputStream out, Vertex node, Vector<Vertex> writer) throws IOException {
        out.writeInt(node.postfixCount);
        out.writeBoolean(node.terminal);
        for (int i = 0; i < upperIndOfCharTable + 1; i++) {
            if (node.next[i] != null) {
                writer.add(node.next[i]);
                out.writeChar(i);
            }
        }
        out.writeChar('.');
    }

    private static void readNode(DataInputStream in, Vertex node, Vector<Vertex> reader) throws IOException {
        node.postfixCount = in.readInt();
        node.terminal = in.readBoolean();

        while (true) {
            char current = in.readChar();
            if (current == '.')
                return;
            reader.add(new Vertex());
            node.next[current] = reader.get(reader.size() - 1);
        }
    }

    public void serialize(OutputStream out) throws IOException {
        DataOutputStream stream = new DataOutputStream(out);
        int counter = 0;
        Vector<Vertex> writer = new Vector<Vertex>();
        writer.add(root);

        while (counter < writer.size()) {
            writeNode(stream, writer.get(counter), writer);
            counter++;
        }
    }

    public void deserialize(InputStream in) throws IOException {
        DataInputStream stream = new DataInputStream(in);
        int counter = 0;
        Vector<Vertex> reader = new Vector<Vertex>();
        reader.add(new Vertex());

        while (counter < reader.size()) {
            readNode(stream, reader.get(counter), reader);
            counter++;
        }
        root = reader.get(0);
    }
}