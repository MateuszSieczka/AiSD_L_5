package testing.comparators;

import java.util.Comparator;

public class IntegerComparator implements Comparator<Integer> {

	@Override
	public int compare(Integer lhs, Integer rhs) {
			return lhs - rhs;
	}
}
