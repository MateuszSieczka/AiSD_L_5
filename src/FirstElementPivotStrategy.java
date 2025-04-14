import java.util.List;

class FirstElementPivotStrategy<T> implements PivotStrategy<T> {
    @Override
    public T choosePivot(List<T> list, int low, int high) {
        return list.get(low);
    }
}