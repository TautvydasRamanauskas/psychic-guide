package psychic.guide.api.services.internal.neuralnetwork;

import org.jsoup.nodes.Element;

import java.util.Collection;
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

	public void trainOnThread(Element element, Collection<Element> brandedElements, Collection<Element> filteredElements) {
		trainExecutor.execute(() -> train(element, brandedElements, filteredElements));
	}

	private void train(Element element, Collection<Element> brandedElements, Collection<Element> filteredElements) {
		boolean isBrand = brandedElements.contains(element);
		boolean isLink = Objects.equals(element.tagName(), TAG_A);
		boolean isBold = Objects.equals(element.tagName(), TAG_B);
		boolean isH3 = Objects.equals(element.tagName(), TAG_H3);
		boolean isH4 = Objects.equals(element.tagName(), TAG_H4);
		boolean isH5 = Objects.equals(element.tagName(), TAG_H5);
		boolean result = filteredElements.contains(element);

		double[] inputs = toDoubleArray(new boolean[]{isBrand, isLink, isBold, isH3, isH4, isH5});
		double[] outputs = toDoubleArray(new boolean[]{result});
		network.train(inputs, outputs);
	}

	private static double[] toDoubleArray(boolean[] booleanArray) {
		double[] doubleArray = new double[booleanArray.length];
		for (int i = 0; i < booleanArray.length; i++) {
			doubleArray[i] = booleanArray[i] ? 1 : 0;
		}
		return doubleArray;
	}
}
