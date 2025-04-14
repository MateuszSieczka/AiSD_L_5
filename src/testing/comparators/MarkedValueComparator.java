package testing.comparators;

import java.util.Comparator;

import testing.MarkedValue;

public class MarkedValueComparator<T> implements Comparator<MarkedValue<T>> {

	private Comparator<? super T> comparator;
	
	public MarkedValueComparator(Comparator<? super T> comparator) {
		this.comparator = comparator;
	}
	
	public Comparator<? super T> baseComparator() {
		return comparator;
	}
	
	@Override
	public final int compare(MarkedValue<T> lhs, MarkedValue<T> rhs) {
		if (lhs.value() == null && rhs.value() == null) {
			return 0;
		} else if (lhs.value() == null) {
			return -1;
		} else if (rhs.value() == null) {
			return 1;
		} else {
			return comparator.compare(lhs.value(), rhs.value());
		}
	}
}
