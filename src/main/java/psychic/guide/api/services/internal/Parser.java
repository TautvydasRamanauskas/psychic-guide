package psychic.guide.api.services.internal;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import psychic.guide.api.model.Options;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.services.internal.neuralnetwork.NeuralNetworkTrainer;
import psychic.guide.api.services.internal.neuralnetwork.NeurophNetwork;
import psychic.guide.api.services.internal.textrule.TextRule;
import psychic.guide.api.services.internal.textrule.TextRuleSet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static psychic.guide.api.services.internal.Tags.*;

public class Parser {
	private static final String ELEMENTS_BRANDS_SELECTOR = ":containsOwn(%s)";
	private static final int DEFAULT_TAG_LEVEL = 3;
	private static final Set<String> TAGS = Set.of(TAG_A, TAG_B, TAG_H1, TAG_H2, TAG_H3, TAG_H4, TAG_H5);

	private final Set<String> brandList;
	private final TextRule ruleSet;
	private final NeuralNetworkTrainer networkTrainer;

	public Parser(Set<String> brandList, Options options) {
		this.brandList = brandList;
		this.ruleSet = new TextRuleSet(options);
		this.networkTrainer = new NeuralNetworkTrainer(new NeurophNetwork());
	}

	public Set<ResultEntry> parse(Document page, String url) {
		System.out.printf("Parsing: %s\n", url);
		Collection<Element> brandedElements = brandList.stream()
				.flatMap(b -> page.select(String.format(ELEMENTS_BRANDS_SELECTOR, b)).stream())
				.collect(Collectors.toSet());
		Collection<Element> filteredElements = brandedElements.stream()
				.filter(this::isResult)
				.collect(Collectors.toSet());
		networkTrainer.trainOnThread(page.children(), brandedElements, filteredElements);

		return filteredElements.stream()
				.map(e -> createResultEntry(e, url))
				.collect(Collectors.toSet());
	}

	public void persist() {
		networkTrainer.persist();
	}

	private boolean isResult(Element element) {
		return isResult(element, DEFAULT_TAG_LEVEL);
	}

	private boolean isResult(Element element, int level) {
		return level > 0 && element.parent() != null &&
				(TAGS.contains(element.tagName()) || isResult(element.parent(), --level));
	}

	private ResultEntry createResultEntry(Element element, String url) {
		ResultEntry resultEntry = new ResultEntry();
		resultEntry.setResult(ruleSet.modify(element.text()));
		resultEntry.setReferences(new HashSet<>(Collections.singleton(url)));
		return resultEntry;
	}
}
