package psychic.guide.api.services.internal;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import psychic.guide.api.model.ResultEntry;
import psychic.guide.api.services.internal.neuralnetwork.NeuralNetworkTrainer;
import psychic.guide.api.services.internal.neuralnetwork.NeurophNetwork;
import psychic.guide.api.services.internal.textrule.TextRule;
import psychic.guide.api.services.internal.textrule.TextRuleSet;

import java.util.*;
import java.util.stream.Collectors;

import static psychic.guide.api.services.internal.Tags.*;

public class Parser {
	private static final String ELEMENTS_BRANDS_SELECTOR = ":containsOwn(%s)";
	private static final int DEFAULT_TAG_LEVEL = 3;
	private static final Set<String> TAGS = Set.of(TAG_A, TAG_B, TAG_H3, TAG_H4, TAG_H5);

	private final Set<String> brandList;
	private final List<ResultEntry> results;
	private final TextRule ruleSet;
	private final NeuralNetworkTrainer networkTrainer;

	public Parser(Set<String> brandList) {
		this.brandList = brandList;
		this.results = new ArrayList<>();
		this.ruleSet = new TextRuleSet();
		this.networkTrainer = new NeuralNetworkTrainer(new NeurophNetwork());
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
				.flatMap(b -> page.select(String.format(ELEMENTS_BRANDS_SELECTOR, b)).stream())
				.collect(Collectors.toSet());
		Collection<Element> filteredElements = brandedElements.stream()
				.filter(this::isResult)
				.collect(Collectors.toSet());
		page.children().forEach(e -> networkTrainer.train(e, brandedElements, filteredElements));

		filteredElements.stream()
				.map(e -> createResultEntry(e, url))
				.forEach(results::add);
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
		resultEntry.setResult(url);
		resultEntry.setReferences(Collections.singletonList(ruleSet.modify(element.text())));
		return resultEntry;
	}
}
