package testing.generation;

import java.util.ArrayList;

public class OrderedIntegerArrayGenerator implements Generator<Integer> {

	@Override
	public ArrayList<Integer> generate(int size) {
		ArrayList<Integer> list = new ArrayList<Integer>(size);
		
		for(int i = 0; i < size; ++i) {
			list.add(i);
		}
		
		return list;
	}

	@Override
	public String getName() {
		return "Ordered";
	}
}
