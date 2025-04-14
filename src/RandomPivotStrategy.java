import java.util.List;
import java.util.Random;

class RandomPivotStrategy<T> implements PivotStrategy<T> {
    @Override
    public T choosePivot(List<T> list, int low, int high) {
        Random random = new Random();
        int pivotIndex = low + random.nextInt(high - low + 1);
        return list.get(pivotIndex);
    }
}