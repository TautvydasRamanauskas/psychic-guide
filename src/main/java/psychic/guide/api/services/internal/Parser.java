package psychic.guide.api.services.internal;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import psychic.guide.api.model.ResultEntry;
import psychic.guide.api.neuralnetwork.NeuralNetwork;
import psychic.guide.api.neuralnetwork.NeurophNetwork;
import psychic.guide.api.services.internal.textrule.TextRule;
import psychic.guide.api.services.internal.textrule.TextRuleSet;

import java.util.*;
import java.util.stream.Collectors;

public class Parser {
	private static final int DEFAULT_TAG_LEVEL = 3;
	private static final String TAG_A = "a";
	private static final String TAG_B = "b";
	private static final String TAG_H3 = "h3";
	private static final String TAG_H4 = "h4";
	private static final String TAG_H5 = "h5";
	private static final Set<String> TAGS = Set.of(TAG_A, TAG_B, TAG_H3, TAG_H4, TAG_H5);

	private final Set<String> brandList;
	private final List<ResultEntry> results;
	private final TextRule ruleSet;
	private final NeuralNetwork network;

	public Parser(Set<String> brandList) {
		this.brandList = brandList;
		this.results = new ArrayList<>();
		this.ruleSet = new TextRuleSet();
		this.network = new NeurophNetwork();

	}

	public Collection<ResultEntry> getResults() {
		return results;
	}

	public void parse(String url) {
		results.clear();
		Document page = PageFetcher.fetch(url);
		parse(page, url);
	}

	private void parse(Document page, String url) {
		Collection<Element> brandedElements = brandList.stream()
				.flatMap(b -> page.select(String.format(":containsOwn(%s)", b)).stream())
				.collect(Collectors.toSet());
		Collection<Element> filteredElements = brandedElements.stream()
				.filter(this::isResult)
				.collect(Collectors.toSet());
		page.children().forEach(e -> train(e, brandedElements, filteredElements));

		filteredElements.stream()
				.map(Element::text)
				.map(ruleSet::modify)
				.map(t -> createResultEntry(url, t))
				.forEach(results::add);
	}

	private boolean isResult(Element element) {
		return isResult(element, DEFAULT_TAG_LEVEL);
	}

	private boolean isResult(Element element, int level) {
		return level > 0 && element.parent() != null &&
				(TAGS.contains(element.tagName()) || isResult(element.parent(), --level));
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

	private static ResultEntry createResultEntry(String url, String text) {
		ResultEntry resultEntry = new ResultEntry();
		resultEntry.setResult(text);
		resultEntry.setReferences(Collections.singletonList(url));
		return resultEntry;
	}
}
