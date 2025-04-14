import java.util.List;

interface PivotStrategy<T> {
    T choosePivot(List<T> list, int low, int high);
}