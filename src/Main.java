import core.AbstractSortingAlgorithm;
import testing.MarkedValue;
import testing.Tester;
import testing.comparators.IntegerComparator;
import testing.comparators.MarkedValueComparator;
import testing.generation.*;
import testing.generation.conversion.LinkedListGenerator;
import testing.generation.conversion.MarkingGenerator;
import testing.results.Result;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


import java.util.Map;
import java.util.HashMap;


public class Main {

	public static void main(String[] args) {
		Comparator<MarkedValue<Integer>> markedComparator = new MarkedValueComparator<>(new IntegerComparator());

		// Create base generators
		List<Generator<Integer>> baseGenerators = createBaseGenerators();

		// Create marked generators
		List<Generator<MarkedValue<Integer>>> arrayGenerators = createArrayGenerators(baseGenerators);
		List<Generator<MarkedValue<Integer>>> linkedListGenerators = createLinkedListGenerators(baseGenerators);

		// Define algorithm-to-generators mapping
		Map<Class<? extends AbstractSortingAlgorithm>, List<Generator<MarkedValue<Integer>>>> algorithmGenerators = new HashMap<>();
		List<Generator<MarkedValue<Integer>>> linkedListQuickSortGenerators = new ArrayList<>();
		linkedListQuickSortGenerators.addAll(arrayGenerators);
		linkedListQuickSortGenerators.addAll(linkedListGenerators);
		algorithmGenerators.put(LinkedListQuickSort.class, linkedListQuickSortGenerators);
		algorithmGenerators.put(ThreeWayMergeSort.class, arrayGenerators);

		// Define algorithms and test parameters
		List<AbstractSortingAlgorithm<MarkedValue<Integer>>> algorithms = createNonSwappingAlgorithms(markedComparator);
		int[] sizes = {1, 5, 10, 25, 50, 80, 100, 150, 200, 250, 500, 1000, 2000, 5000, 10000, 20000, 100000};
		int repetitions = 20;

		// Run tests
		List<TestResult> results = runTests(algorithms, sizes, repetitions, algorithmGenerators);

		// Print results
		printResults(results);
	}

	private static List<Generator<Integer>> createBaseGenerators() {
		List<Generator<Integer>> generators = new ArrayList<>();
		generators.add(new RandomIntegerArrayGenerator(10));
//		generators.add(new OrderedIntegerArrayGenerator());
//		generators.add(new ReversedIntegerArrayGenerator());
		generators.add(new ShuffledIntegerArrayGenerator());
		return generators;
	}

	private static List<Generator<MarkedValue<Integer>>> createArrayGenerators(List<Generator<Integer>> baseGenerators) {
		List<Generator<MarkedValue<Integer>>> generators = new ArrayList<>();
		for (Generator<Integer> baseGenerator : baseGenerators) {
			generators.add(new MarkingGenerator<>(baseGenerator));
		}
		return generators;
	}

	private static List<Generator<MarkedValue<Integer>>> createLinkedListGenerators(List<Generator<Integer>> baseGenerators) {
		List<Generator<MarkedValue<Integer>>> generators = new ArrayList<>();
		for (Generator<Integer> baseGenerator : baseGenerators) {
			generators.add(new LinkedListGenerator<>(new MarkingGenerator<>(baseGenerator)));
		}
		return generators;
	}

	private static List<AbstractSortingAlgorithm<MarkedValue<Integer>>> createNonSwappingAlgorithms(
			Comparator<MarkedValue<Integer>> markedComparator) {
		List<AbstractSortingAlgorithm<MarkedValue<Integer>>> algorithms = new ArrayList<>();
		algorithms.add(new LinkedListQuickSort<>(markedComparator, new FirstElementPivotStrategy<>()));
//		algorithms.add(new ThreeWayMergeSort<>(markedComparator));
		return algorithms;
	}

	private static List<TestResult> runTests(
			List<? extends AbstractSortingAlgorithm<MarkedValue<Integer>>> algorithms,
			int[] sizes,
			int repetitions,
			Map<Class<? extends AbstractSortingAlgorithm>, List<Generator<MarkedValue<Integer>>>> algorithmGenerators) {

		List<TestResult> results = new ArrayList<>();
		for (AbstractSortingAlgorithm<MarkedValue<Integer>> algorithm : algorithms) {
			List<Generator<MarkedValue<Integer>>> generatorsToTest = algorithmGenerators.get(algorithm.getClass());


			for (Generator<MarkedValue<Integer>> generator : generatorsToTest) {
				String generatorType = (generator instanceof LinkedListGenerator) ? "LinkedList" : "Array";
				for (int size : sizes) {
					System.out.println("Testing " + algorithm.getClass().getSimpleName()
							+ " with " + generator.getName() + " (" + generatorType + ")"
							+ " (Size: " + size + ")");

					Result result = Tester.runNTimes(algorithm, generator, size, repetitions);
					results.add(new TestResult(
							algorithm.getClass().getSimpleName(),
							generator.getName(),
							size,
							result,
							generatorType)); // Store generator type
				}
			}
		}
		return results;
	}

	private static void printResults(List<TestResult> results) {
		System.out.println("\nWyniki testów:");
		System.out.println("Algorytm\tGenerator\tTyp\tRozmiar\tPorównania\tOdch. porównań\tCzas (ms)\tOdch. czasu");
		for (TestResult tr : results) {
			Result res = tr.result;
			System.out.printf("%s\t%s\t%s\t%d\t%s\t%s\t%s\t%s\t%n",
					tr.algorithm,
					tr.generator,
					tr.generatorType, // Print generator type
					tr.size,
					double2String(res.averageComparisons()),
					double2String(res.comparisonsStandardDeviation()),
					double2String(res.averageTimeInMilliseconds()),
					double2String(res.timeStandardDeviation()));
		}
	}

	private static String double2String(double value) {
		return String.format("%.12f", value);
	}
}

class TestResult {
	String algorithm;
	String generator;
	int size;
	Result result;
	String generatorType; // Added field for generator type

	public TestResult(String algorithm, String generator, int size, Result result, String generatorType) {
		this.algorithm = algorithm;
		this.generator = generator;
		this.size = size;
		this.result = result;
		this.generatorType = generatorType;
	}
}