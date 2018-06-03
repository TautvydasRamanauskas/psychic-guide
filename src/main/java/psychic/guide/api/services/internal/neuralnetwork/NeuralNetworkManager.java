package psychic.guide.api.services.internal.neuralnetwork;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static psychic.guide.api.services.internal.Tags.*;

public class NeuralNetworkManager {
	private final NeurophNetwork network;
	private final ExecutorService trainExecutor;

	public NeuralNetworkManager(NeurophNetwork network) {
		this.network = network;
		this.trainExecutor = Executors.newSingleThreadExecutor();
	}

	public void trainOnThread(Elements elements, Collection<Element> brandedElements, Collection<Element> filteredElements) {
		trainExecutor.execute(() -> train(elements, brandedElements, filteredElements));
	}

	public void train(Elements elements, Collection<Element> brandedElements, Collection<Element> filteredElements) {
		List<double[]> inputs = new ArrayList<>();
		List<double[]> outputs = new ArrayList<>();
		elements.forEach(e -> addRow(e, brandedElements, filteredElements, inputs, outputs));
		network.train(inputs, outputs);
	}

	public Set<Element> calculate(Elements elements, Collection<Element> brandedElements) {
		return elements.stream()
				.filter(e -> calculateElement(brandedElements, e))
				.collect(Collectors.toSet());
	}

	public void persist() {
		trainExecutor.execute(network::save);
	}

	private void addRow(Element element, Collection<Element> brandedElements, Collection<Element> filteredElements,
						List<double[]> inputs, List<double[]> outputs) {
		boolean[] inputArray = createInputsArray(element, brandedElements);
		boolean[] outputArray = {filteredElements.contains(element)};

		inputs.add(toDoubleArray(inputArray));
		outputs.add(toDoubleArray(outputArray));
	}

	private boolean[] createInputsArray(Element element, Collection<Element> brandedElements) {
		boolean isBrand = brandedElements.contains(element);
		boolean isLink = Objects.equals(element.tagName(), TAG_A);
		boolean isBold = Objects.equals(element.tagName(), TAG_B);
		boolean isH3 = Objects.equals(element.tagName(), TAG_H3);
		boolean isH4 = Objects.equals(element.tagName(), TAG_H4);
		boolean isH5 = Objects.equals(element.tagName(), TAG_H5);
		return new boolean[]{isBrand, isLink, isBold, isH3, isH4, isH5};
	}

	private static double[] toDoubleArray(boolean[] booleanArray) {
		double[] doubleArray = new double[booleanArray.length];
		for (int i = 0; i < booleanArray.length; i++) {
			doubleArray[i] = booleanArray[i] ? 1 : 0;
		}
		return doubleArray;
	}

	private boolean calculateElement(Collection<Element> brandedElements, Element element) {
		boolean[] inputsArray = createInputsArray(element, brandedElements);
		double[] results = network.calculate(toDoubleArray(inputsArray));
		double result = results[0];
		return result > 0.5;
	}
}
