import core.AbstractSortingAlgorithm;
import java.util.*;

public class ThreeWayMergeSort<T> extends AbstractSortingAlgorithm<T> {

    public ThreeWayMergeSort(Comparator<? super T> comparator) {
        super(comparator);
    }

    @Override
    public List<T> sort(List<T> list) {
        if (list == null || list.size() <= 1) {
            return list;
        }


        return mergesort(list, 0, list.size() - 1);
    }

    private List<T> mergesort(List<T> list, int startIndex, int endIndex) {
        // Base case: single element
        if (startIndex == endIndex) {
            List<T> result = new ArrayList<>();
            result.add(list.get(startIndex));
            return result;
        }
        if (startIndex > endIndex) {
            return new ArrayList<>();
        }

        // Calculate division points for three segments
        int range = endIndex - startIndex;
        int firstSplit = startIndex + range / 3;
        int secondSplit = startIndex + 2 * range / 3;

        // Recursively sort segments
        List<T> first = mergesort(list, startIndex, firstSplit);
        List<T> second = mergesort(list, firstSplit + 1, secondSplit);
        List<T> third = mergesort(list, secondSplit + 1, endIndex);

        return merge(first, second, third);
    }

    @SuppressWarnings("unchecked")
    private List<T> merge(List<T> first, List<T> second, List<T> third) {
        List<T> result = new ArrayList<>();

        // Create an array of iterators for the three lists
        Iterator<T>[] iterators = new Iterator[] { first.iterator(), second.iterator(), third.iterator() };
        // Create an array to store the current element of each iterator
        T[] current = (T[]) new Object[iterators.length];

        // Initialize the current elements for each iterator
        for (int i = 0; i < iterators.length; i++) {
            if (iterators[i].hasNext()) {
                current[i] = iterators[i].next();
            }
        }


        while (true) {
            int minIndex = -1;
            T minElement = null;

            // Find the smallest element
            for (int i = 0; i < iterators.length; i++) {
                if (current[i] == null) continue;

                if (minIndex == -1) {
                        minIndex = i;
                        minElement = current[i];
                } else if (compare(current[i], minElement) < 0) {
                        minIndex = i;
                        minElement = current[i];
                }

            }

            // If all lists are done, break
            if (minIndex == -1) {
                break;
            }

            // Add the chosen element
            result.add(minElement);

            // Advance the iterator for the list from which we took the element.
            if (iterators[minIndex].hasNext()) {
                current[minIndex] = iterators[minIndex].next();
            } else {
                current[minIndex] = null;
            }
        }

        return result;
    }
}