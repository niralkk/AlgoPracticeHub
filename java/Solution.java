import java.sql.Timestamp;
import java.lang.reflect.Array;
import java.util.concurrent.locks.*;

interface Cache<K, V> {
    public V get(K key);
    public void put(K key, V value);
}

interface Policy<V> {
    public int evictEntryIndex(Entry<V>[] entries, int startIndex, int endIndex);
}

class LRUPolicy<V> implements Policy<V> {
    public int evictEntryIndex(Entry<V>[] entries, int startIndex, int endIndex) {
        int index = 0;
        Timestamp earliest = new Timestamp(System.nanoTime());
        for(int i = startIndex; i <= endIndex; i++) {
            if(entries[i].isOccupied && entries[i].Timestamp.before(earliest)) {
                earliest = entries[i].Timestamp;
                index = i;
            }
        }
        return index;
    }
}

class NWayCache<K, V> implements Cache<K, V> {
    private Entry<V>[] entries;
    private int associativity;
    private int numSets;
    private Policy<V> replacementPolicy = new LRUPolicy<V>();
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();


    NWayCache(int associativity, int numSets, Policy<V> replacementPolicy) {
        this.associativity = associativity;
        this.numSets = numSets;
        this.entries = (Entry<V>[]) Array.newInstance(Entry.class, associativity * numSets);

        for(int i = 0; i < entries.length; i++) {
            entries[i] = new Entry<V>();
        }

        if(replacementPolicy != null) {
            this.replacementPolicy = replacementPolicy;
        }
    }

    public V get(K key) {
        Utility util = new Utility();
        int hashKey = util.getHashKey(key);
        int startIndex = util.getStartIndex(this.associativity, this.numSets, hashKey);
        int endIndex = util.getEndIndex(this.associativity, startIndex);
        V value = null;

        readLock.lock();
        try {
            for (int i = startIndex; i <= endIndex; i++) {
                if (entries[i].isOccupied && entries[i].tag == hashKey) {
                    entries[i].Timestamp = new Timestamp(System.currentTimeMillis());
                    value = entries[i].value;
                    break;
                }
            }
        }
        finally {
            readLock.unlock();
        }
        return value;
    }

    public void put(K key, V value) {
        Utility util = new Utility();
        int hashKey = util.getHashKey(key);
        int startIndex = util.getStartIndex(this.associativity, this.numSets, hashKey);
        int endIndex = util.getEndIndex(this.associativity, startIndex);

        Timestamp currentTime = new Timestamp(System.nanoTime());

        Entry<V> newEntry = new Entry<V>(value, hashKey, currentTime, false);

        writeLock.lock();
        try {
            for (int i = startIndex; i <= endIndex; i++) {
                //If key already present in the cache
                if (entries[i].isOccupied && entries[i].tag == hashKey) {
                    entries[i] = newEntry;
                    entries[i].isOccupied = true;
                }
            }

            for (int i = startIndex; i <= endIndex; i++) {
                // Key is not present, so find available block
                if (!entries[i].isOccupied) {
                    entries[i] = newEntry;
                    entries[i].isOccupied = true;
                }
            }

            // No space available, need eviction
            int evictIndex = replacementPolicy.evictEntryIndex(entries, startIndex, endIndex);
            entries[evictIndex] = newEntry;
            entries[evictIndex].isOccupied = true;
        }
        finally {
            writeLock.unlock();
        }

    }
}

class Utility {

    public int getStartIndex(int associativity, int numSets, int hashKey) {
        return (hashKey % numSets) * associativity;
    }

    public int getEndIndex(int associativity, int startIndex) {
        return startIndex + associativity - 1;
    }

    public int getHashKey(Object key) {
        return ((key.hashCode()*37) + 17);
    }
}

class CacheFactory {
    public static <K, V> Cache<K, V> newCache(int associativity, int numSets, Policy<V> replacementPolicy) {
        return new NWayCache<K, V>(associativity, numSets, replacementPolicy);
    }
}

class Entry<V> {
    public V value;
    public int tag;
    public Timestamp Timestamp;
    public boolean isOccupied;

    Entry() {
        this.value = null;
        this.tag = 0;
        this.Timestamp = null;
        this.isOccupied = false;
    }

    Entry(V value, int tag, Timestamp Timestamp, boolean isOccupied) {
        this.value = value;
        this.tag = tag;
        this.Timestamp = Timestamp;
        this.isOccupied = isOccupied;
    }
}


public class Solution implements Runnable {
    static NWayCache<Integer, String> nWayCache = new NWayCache<Integer, String>(2, 4, new LRUPolicy<>());;
    public void run() {
        int id = (int) Thread.currentThread().getId() - 21;
        String name = Thread.currentThread().getName();
        System.out.println(name + " and Thread ID: " + id);
        nWayCache.put(id, name);
        try {
            Thread.sleep(1000); //10 Seconds
        }
        catch (Exception e) {
        }

        System.out.println(nWayCache.get(id));
    }
    public static void main(String args[]) {
        for(int i = 0; i < 10; i++) {
            Thread object = new Thread(new Solution(), String.valueOf(i));
            object.start();
        }
    }
}