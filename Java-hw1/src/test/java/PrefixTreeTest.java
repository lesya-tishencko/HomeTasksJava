import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by lesya on 22.02.2017.
 */
public class PrefixTreeTest {

    @Test
    public void addGeneral() throws Exception {
        PrefixTree trie = new PrefixTree();

        assertTrue(trie.add("He"));
        assertTrue(trie.add("She"));
        assertTrue(trie.add("His"));
        assertTrue(trie.add("Her"));
        assertFalse(trie.add("He"));
        assertFalse(trie.add("H!s"));
        assertFalse(trie.add(""));
        assertFalse(trie.add("H1s"));
        assertFalse(trie.add("Привет"));
        assertTrue(trie.add("They"));
        assertTrue(trie.add("Their"));
    }

    @Test
    public void containsGeneral() throws Exception {
        PrefixTree trie = new PrefixTree();
        trie.add("He");
        trie.add("She");
        trie.add("His");
        trie.add("Her");
        trie.add("They");
        trie.add("Their");

        assertTrue(trie.contains("He"));
        assertTrue(trie.contains("She"));
        assertTrue(trie.contains("His"));
        assertTrue(trie.contains("Her"));
        assertFalse(trie.contains("Here"));
        assertFalse(trie.contains("H!s"));
        assertTrue(trie.contains("They"));
        assertTrue(trie.contains("Their"));

        trie.remove("He");
        trie.remove("They");
        trie.remove("He");
        trie.remove("Her");

        assertFalse(trie.contains("He"));
        assertTrue(trie.contains("She"));
        assertTrue(trie.contains("His"));
        assertFalse(trie.contains("Her"));
        assertFalse(trie.contains("They"));
        assertTrue(trie.contains("Their"));
    }

    @Test
    public void sizeGeneral() throws Exception {
        PrefixTree trie = new PrefixTree();
        trie.add("He");
        trie.add("She");
        trie.add("His");
        trie.add("Her");
        trie.add("Her");
        trie.add("They");
        trie.add("Their");

        assertEquals(6, trie.size());

        trie.remove("He");
        trie.remove("They");
        trie.remove("He");

        assertEquals(4, trie.size());
    }

    @Test
    public void removeGeneral() throws Exception {
        PrefixTree trie = new PrefixTree();
        trie.add("He");
        trie.add("She");
        trie.add("His");
        trie.add("Her");
        trie.add("Her");
        trie.add("They");
        trie.add("Their");

        assertTrue(trie.remove("He"));
        assertFalse(trie.remove("He"));
        assertFalse(trie.remove("H!s"));
        assertTrue(trie.remove("They"));
    }

    @Test
    public void howManyStartsWithPrefixGeneral() throws Exception {
        PrefixTree trie = new PrefixTree();
        trie.add("He");
        trie.add("Her");
        trie.add("She");
        trie.add("His");
        trie.add("They");
        trie.add("Their");

        assertEquals(2, trie.howManyStartsWithPrefix("He"));
        assertEquals(3, trie.howManyStartsWithPrefix("H"));
        assertEquals(2, trie.howManyStartsWithPrefix("The"));

        trie.remove("He");
        trie.remove("They");
        trie.remove("Her");

        assertEquals(0, trie.howManyStartsWithPrefix("He"));
        assertEquals(1, trie.howManyStartsWithPrefix("The"));
        assertEquals(1, trie.howManyStartsWithPrefix("H"));

        trie.add("Here");
        trie.add("Her");

        assertEquals(2, trie.howManyStartsWithPrefix("Her"));
    }

    @Test
    public void howManyStartsWithPrefixSpec() throws Exception {
        PrefixTree trie = new PrefixTree();

        trie.add("blue");
        trie.add("blueberry");
        trie.add("strawberry");
        trie.add("bluebird");
        trie.add("blueberries");

        assertEquals(4, trie.howManyStartsWithPrefix("blue"));
        assertEquals(2, trie.howManyStartsWithPrefix("blueberr"));
        assertEquals(3, trie.howManyStartsWithPrefix("blueb"));
        assertEquals(1, trie.howManyStartsWithPrefix("straw"));
    }

    @Test
    public void howManyStartsWithPrefixTrieSpec() throws Exception {
        PrefixTree trie = new PrefixTree();

        trie.add("blue");
        trie.add("blueberry");
        trie.add("bluebird");
        trie.add("blueberries");

        trie.remove("blueberries");
        trie.remove("bluebird");

        assertEquals(2, trie.howManyStartsWithPrefix("blue"));
        assertEquals(1, trie.howManyStartsWithPrefix("blueberr"));
        assertEquals(0, trie.howManyStartsWithPrefix("blueberri"));
        assertEquals(1, trie.howManyStartsWithPrefix("blueb"));
    }

    @Test
    public void addDouble() throws Exception {
        PrefixTree trie = new PrefixTree();

        assertTrue(trie.add("blue"));
        assertTrue(trie.add("blueberry"));
        assertFalse(trie.add("blueberry"));

        trie.remove("blueberry");
        assertTrue(trie.add("blueberry"));
    }

    @Test
    public void sizeDouble() throws Exception {
        PrefixTree trie = new PrefixTree();

        trie.add("blue");
        trie.add("blueberry");
        trie.add("bluebird");
        trie.add("blueberry");

        assertEquals(3, trie.size());

        trie.remove("blue");
        trie.remove("bluebird");
        trie.remove("bluebird");

        assertEquals(1, trie.size());

        trie.add("blue");
        assertEquals(2, trie.size());
    }

    public void howManyStartsWithPrefixDouble() throws Exception {
        PrefixTree trie = new PrefixTree();

        trie.add("blue");
        trie.add("blueberry");
        trie.add("bluebird");
        trie.add("blueberry");

        assertEquals(3, trie.howManyStartsWithPrefix("blue"));

        trie.remove("blue");
        trie.remove("bluebird");
        trie.remove("bluebird");

        assertEquals(1, trie.howManyStartsWithPrefix("blue"));

        trie.add("blue");
        assertEquals(2, trie.howManyStartsWithPrefix("blue"));
    }
}