public class HashDictionary implements Dictionary {

    private int tableSize = 128;
    private List[] hashTable = new List[tableSize];
    private int countKey = 0;

    public HashDictionary() {
        for (int i = 0; i < tableSize; i++)
            hashTable[i] = new List();
    }

    public int size() {
        return countKey;
    }

    public boolean contains(String key) {
        int index = key.hashCode() % tableSize;
        String result = hashTable[index].find(key);
        return result != null;
    }

    public String get(String key) {
        int index = key.hashCode() % tableSize;
        return hashTable[index].find(key);
    }

    public String put(String key, String value) {
        int index = key.hashCode() % tableSize;
        String result = hashTable[index].add(key, value);
        if (result == null) {
            countKey++;
            rehash();
        }
        return result;
    }

    public String remove(String key) {
        int index = key.hashCode() % tableSize;
        String result = hashTable[index].delete(key);
        if (result != null)
            countKey--;
        return result;
    }

    public void clear() {
        for (List node : hashTable)
            node.eraseAll();
        countKey = 0;
    }

    private void rehash() {
        if (countKey > 4 * tableSize / 3) {
            tableSize *= 2;
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