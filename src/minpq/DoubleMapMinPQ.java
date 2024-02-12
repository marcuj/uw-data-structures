package minpq;

import java.util.*;

/**
 * {@link TreeMap} and {@link HashMap} implementation of the {@link ExtrinsicMinPQ} interface.
 *
 * @param <T> the type of elements in this priority queue.
 * @see ExtrinsicMinPQ
 */
public class DoubleMapMinPQ<T> implements ExtrinsicMinPQ<T> {
    /**
     * {@link NavigableMap} of priority values to all items that share the same priority values.
     */
    private final NavigableMap<Double, Set<T>> priorityToItem;
    /**
     * {@link Map} of items to their associated priority values.
     */
    private final Map<T, Double> itemToPriority;

    /**
     * Constructs an empty instance.
     */
    public DoubleMapMinPQ() {
        priorityToItem = new TreeMap<>();
        itemToPriority = new HashMap<>();
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        }
        if (!priorityToItem.containsKey(priority)) {
            priorityToItem.put(priority, new HashSet<>());
        }
        Set<T> itemsWithPriority = priorityToItem.get(priority);
        itemsWithPriority.add(item);
        itemToPriority.put(item, priority);
    }

    @Override
    public boolean contains(T item) {
        return itemToPriority.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        double minPriority = priorityToItem.firstKey();
        Set<T> itemsWithMinPriority = priorityToItem.get(minPriority);
        return firstOf(itemsWithMinPriority);
    }

    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        double minPriority = priorityToItem.firstKey();                 // has look all the way down left side of tree worstcase levels it looks at is the height of tree (logN height)
        Set<T> itemsWithMinPriority = priorityToItem.get(minPriority);
        T item = firstOf(itemsWithMinPriority);       // constant time gets first item of hashset
        itemsWithMinPriority.remove(item);
        if (itemsWithMinPriority.isEmpty()) {
            priorityToItem.remove(minPriority); // lowest priority again all the way at bottom of tree worst case
        }
        itemToPriority.remove(item);
        return item;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        double oldPriority = itemToPriority.get(item); // hashMap constant
        if (priority != oldPriority) {
            Set<T> itemsWithOldPriority = priorityToItem.get(oldPriority); // get TreeMap worst case logN
            itemsWithOldPriority.remove(item); // constant since HashSet
            if (itemsWithOldPriority.isEmpty()) {
                priorityToItem.remove(oldPriority);                        // remove TreeMap worst case logN
            }
            itemToPriority.remove(item); // hashmap constant
            add(item, priority); // treemap LogN
        }
    }

    @Override
    public int size() {
        return itemToPriority.size();
    }

    /**
     * Returns any one element from the given iterable.
     *
     * @param it the iterable of elements.
     * @return any one element from the given iterable.
     */
    private T firstOf(Iterable<T> it) {
        return it.iterator().next();
    }
}
