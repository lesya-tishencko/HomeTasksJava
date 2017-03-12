public class HashDictionary implements Dictionary {

    private List[] hashTable = new List[128];
    private int size = 0;

    public HashDictionary() {
        for (int i = 0; i < hashTable.length; i++)
            hashTable[i] = new List();
    }

    public int size() {
        return size;
    }

    public boolean contains(String key) {
        int index = key.hashCode() % hashTable.length;
        return hashTable[index].find(key) != null;
    }

    public String get(String key) {
        int index = key.hashCode() % hashTable.length;
        return hashTable[index].find(key);
    }

    public String put(String key, String value) {
        int index = key.hashCode() % hashTable.length;
        String result = hashTable[index].add(key, value);
        if (result == null) {
            size++;
            rehash();
        }
        return result;
    }

    public String remove(String key) {
        int index = key.hashCode() % hashTable.length;
        String result = hashTable[index].delete(key);
        if (result != null)
            size--;
        return result;
    }

    public void clear() {
        for (List node : hashTable)
            node.eraseAll();
        hashTable = new List[128];
        for (int i = 0; i < hashTable.length; i++)
            hashTable[i] = new List();
        size = 0;
    }

    private void rehash() {
        if (size > 4 * hashTable.length / 3) {
            int tableSize = hashTable.length *  2;
            List[] newHashTable = new List[tableSize];
            for (int i = 0; i < tableSize; i++)
                newHashTable[i] = new List();

            for (List node : hashTable) {
                List.Pair current = node.getNextAndRemove();
                while (current != null) {
                    int index = current.key.hashCode() % tableSize;
                    newHashTable[index].add(current.key, current.value);
                    current = node.getNextAndRemove();
                }
            }
            hashTable = newHashTable;
        }
    }
}