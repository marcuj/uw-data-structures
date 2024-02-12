package minpq;

import java.util.*;

// Assume smaller priority is at the overallRoot.
/**
 * Optimized binary heap implementation of the {@link ExtrinsicMinPQ} interface.
 *
 * @param <T> the type of elements in this priority queue.
 * @see ExtrinsicMinPQ
 */
public class OptimizedHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the heap of item-priority pairs.
     */
    private final List<PriorityNode<T>> items;
    /**
     * {@link Map} of each item to its associated index in the {@code items} heap.
     */
    private final Map<T, Integer> itemToIndex;
    /**
     * The number of elements in the heap.
     */
    private int size;

    /**
     * Constructs an empty instance.
     */
    public OptimizedHeapMinPQ() {
        items = new ArrayList<>();
        itemToIndex = new HashMap<>();
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        }
        items.add(new PriorityNode<> (item, priority));
        size += 1;
        itemToIndex.put(item, size - 1); //operation put the item into Hashmap and recall the location
        swim(size - 1);
    }

    @Override
    public boolean contains(T item) {
        // TODO: Replace with your code
        return itemToIndex.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return items.get(0).item(); // I think this is correct bc isGreater in swim already take this into account.
    }

    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        T min = peekMin();
        swap(0, size - 1);
        items.remove(size - 1);
        itemToIndex.remove(min);
        size -= 1;
        sink(0);
        return min;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }

        int index = itemToIndex.get(item);
        items.get(index).setPriority(priority);

        swim(index);
        sink(index);

//        double oldPriority = itemToPriority.get(item);
//        if (priority != oldPriority) {
//            Set<T> itemsWithOldPriority = priorityToItem.get(oldPriority);
//            itemsWithOldPriority.remove(item);
//            if (itemsWithOldPriority.isEmpty()) {
//                priorityToItem.remove(oldPriority);
//            }
//            itemToPriority.remove(item);
//            add(item, priority);
//        }
    }

    @Override
    public int size() {
        return size;
    }


    // Modified Swim the item at the given index until the heap invariant is satisfied.
    private void swim(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;

            // If the item at the parent index is greater, swap the two items.
            if (isGreater(parentIndex, index)) {
                swap(parentIndex, index);
            } else {
                return;
            }
            index = parentIndex;
        }
    }

    // Returns true if and only if the item at index i is strictly greater
    // than the item at index j. i or j must be valid indices.
    private boolean isGreater(int i, int j) {
        boolean isValidI = 0 <= i && i < size;
        boolean isValidJ = 0 <= j && j < size;
        if (isValidI && isValidJ) {
            // If both i and j are valid, return whether i is greater than j.
            return items.get(i).priority() > items.get(j).priority();
        } else if (isValidI || isValidJ) {
            // Only one of i or j is valid, so return whether i is valid.
            // If i is valid, then it is greater than j.
            // If i is not valid, then it is less than j.
            return isValidI;
        } else {
            throw new IllegalArgumentException("i or j must be a valid index");
        }
    }

    // Swaps the items at the given indices i and j.
    private void swap(int i, int j) {
        PriorityNode<T> temp = items.get(i);
        items.set(i, items.get(j));
        items.set(j, temp);
        itemToIndex.replace(temp.item(), j);
        itemToIndex.replace(items.get(j).item(), i);
    }

    // Sink the item at the given index until the heap invariant is satisfied.
    private void sink(int index) {
        while ((2 * index) + 2 < size) {
            int leftIndex = (2 * index) + 1;
            int rightIndex = leftIndex + 1;

            // Assign the smaller of the children as the swap candidate.
            int swapCandidate = leftIndex;
            if (rightIndex < size && isGreater(leftIndex, rightIndex)) {
                swapCandidate = rightIndex;
            }

            // If the item at the current index is greater, swap the two items.
            if (isGreater(index, swapCandidate)) {
                swap(index, swapCandidate);
            } else {
                return;
            }
            index = swapCandidate;
        }
    }
}