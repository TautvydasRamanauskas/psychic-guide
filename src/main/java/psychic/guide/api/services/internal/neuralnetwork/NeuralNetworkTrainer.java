package psychic.guide.api.services.internal.neuralnetwork;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static psychic.guide.api.services.internal.Tags.*;

public class NeuralNetworkTrainer {
	private final NeurophNetwork network;
	private final ExecutorService trainExecutor;

	public NeuralNetworkTrainer(NeurophNetwork network) {
		this.network = network;
		this.trainExecutor = Executors.newSingleThreadExecutor();
	}

	public void trainOnThread(Elements elements, Collection<Element> brandedElements, Collection<Element> filteredElements) {
		trainExecutor.execute(() -> train(elements, brandedElements, filteredElements));
	}

	private void train(Elements elements, Collection<Element> brandedElements, Collection<Element> filteredElements) {
		List<double[]> inputs = new ArrayList<>();
		List<double[]> outputs = new ArrayList<>();
		elements.forEach(e -> addRow(e, brandedElements, filteredElements, inputs, outputs));
		network.train(inputs, outputs);
	}

	private void addRow(Element element, Collection<Element> brandedElements, Collection<Element> filteredElements,
						List<double[]> inputs, List<double[]> outputs) {
		boolean isBrand = brandedElements.contains(element);
		boolean isLink = Objects.equals(element.tagName(), TAG_A);
		boolean isBold = Objects.equals(element.tagName(), TAG_B);
		boolean isH3 = Objects.equals(element.tagName(), TAG_H3);
		boolean isH4 = Objects.equals(element.tagName(), TAG_H4);
		boolean isH5 = Objects.equals(element.tagName(), TAG_H5);
		boolean result = filteredElements.contains(element);

		inputs.add(toDoubleArray(new boolean[]{isBrand, isLink, isBold, isH3, isH4, isH5}));
		outputs.add(toDoubleArray(new boolean[]{result}));
	}

	private static double[] toDoubleArray(boolean[] booleanArray) {
		double[] doubleArray = new double[booleanArray.length];
		for (int i = 0; i < booleanArray.length; i++) {
			doubleArray[i] = booleanArray[i] ? 1 : 0;
		}
		return doubleArray;
	}
}
