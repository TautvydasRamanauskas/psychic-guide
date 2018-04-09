package psychic.guide.api.services.internal;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import psychic.guide.api.model.ResultEntry;
import psychic.guide.api.neuralnetwork.NeuralNetwork;
import psychic.guide.api.neuralnetwork.NeurophNetwork;
import psychic.guide.api.services.internal.model.ParseData;
import psychic.guide.api.services.internal.textrule.TextRule;
import psychic.guide.api.services.internal.textrule.TextRuleSet;

import java.util.*;
import java.util.stream.Collectors;

public class Parser {
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

	public void parse(Document page) {
		results.clear();

		Collection<Element> brandedElements = brandList.stream()
				.flatMap(b -> page.select(String.format(":containsOwn(%s)", b)).stream())
				.collect(Collectors.toSet());

		Collection<Element> filteredElements = brandedElements.stream()
				.filter(this::isResult)
				.collect(Collectors.toSet());

		List<ParseData> results = filteredElements.stream()
				.map(ParseData::new)
				.collect(Collectors.toList());

		results.forEach(d -> d.setText(ruleSet.modify(d.getText())));
		Set<ResultEntry> resultSet = results.stream().map(d -> new ResultEntry()).collect(Collectors.toSet());

		page.children().forEach(e -> train(e, brandedElements, filteredElements));

		this.results.addAll(resultSet);
	}

	private boolean isResult(Element element) {
		return element.parent() != null && (isTag(element.tagName()) || isResult(element.parent()));
	}

	private boolean isTag(String tagName) {
		return Objects.equals(tagName, "a") ||
				Objects.equals(tagName, "b") ||
				Objects.equals(tagName, "h3") ||
				Objects.equals(tagName, "h4") ||
				Objects.equals(tagName, "h5");
	}

	private void train(Element element, Collection<Element> brandedElements, Collection<Element> filteredElements) {
		boolean isBrand = brandedElements.contains(element);
		boolean isLink = Objects.equals(element.tagName(), "a");
		boolean isBold = Objects.equals(element.tagName(), "b");
		boolean isH3 = Objects.equals(element.tagName(), "h3");
		boolean isH4 = Objects.equals(element.tagName(), "h4");
		boolean isH5 = Objects.equals(element.tagName(), "h5");
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
