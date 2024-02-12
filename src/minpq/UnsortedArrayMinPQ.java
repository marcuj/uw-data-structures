package minpq;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Unsorted array (or {@link ArrayList}) implementation of the {@link ExtrinsicMinPQ} interface.
 *
 * @param <T> the type of elements in this priority queue.
 * @see ExtrinsicMinPQ
 */
public class  UnsortedArrayMinPQ<T> implements ExtrinsicMinPQ<T> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the item-priority pairs in no specific order.
     */
    private final List<PriorityNode<T>> items;

    /**
     * Constructs an empty instance.
     */
    public UnsortedArrayMinPQ() {
        items = new ArrayList<>();
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        }
        items.add(new PriorityNode(item, priority));
    }

    @Override
    public boolean contains(T item) {
        return indexOf(item) >= 0;
    }

    // Helper method for contains, return 0+ if the item exist, else return -1;
    public int indexOf(T item) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).item().equals(item)) return i;
        }
        return -1;
    }

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return items.get(minNodeIndex()).item();
    }

    // Helper method AND must ensure the array is not empty
    public int minNodeIndex() {
        int minValue = 0;
        PriorityNode<T> holder = items.get(0);
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).priority() < holder.priority()) {
                holder = items.get(i);
                minValue = i;
            }
        }
        return minValue;
    }

    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        // runtime is N
        return items.remove(minNodeIndex()).item();
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        items.get(indexOf(item)).setPriority(priority);
    }

    @Override
    public int size() {
        return items.size();
    }
}
