import core.AbstractSortingAlgorithm;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class LinkedListQuickSort<T> extends AbstractSortingAlgorithm<T> {

    private final PivotStrategy<T> pivotStrategy;

    @SuppressWarnings("ClassEscapesDefinedScope")
    public LinkedListQuickSort(Comparator<? super T> comparator, PivotStrategy<T> pivotStrategy) {
        super(comparator);
        this.pivotStrategy = pivotStrategy;
    }

    @Override
    public List<T> sort(List<T> list) {
        if (list == null || list.size() <= 1) {
            return list;
        }

        return quickSortLinkedList(list);
    }

    private List<T> quickSortLinkedList(List<T> list) {
        if (list == null || list.size() <= 1) {
            return list;
        }

        T pivotValue = pivotStrategy.choosePivot(list, 0, list.size() - 1);

        LinkedList<T> less = new LinkedList<>();
        LinkedList<T> equal = new LinkedList<>();
        LinkedList<T> greater = new LinkedList<>();

        for (T element : list) {
            if (compare(element, pivotValue) < 0) {
                less.add(element);
            } else if (compare(element, pivotValue) > 0) {
                greater.add(element);
            } else {
                equal.add(element);
            }
        }

        List<T> sortedLess = quickSortLinkedList(less);
        List<T> sortedGreater = quickSortLinkedList(greater);

        LinkedList<T> result = new LinkedList<>(sortedLess);
        result.addAll(equal);
        result.addAll(sortedGreater);

        return result;
    }
}