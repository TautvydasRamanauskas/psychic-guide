package psychic.guide.api.services.internal;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import psychic.guide.api.model.Options;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.services.internal.neuralnetwork.NeuralNetworkManager;
import psychic.guide.api.services.internal.neuralnetwork.NeurophNetwork;
import psychic.guide.api.services.internal.textrule.TextRule;
import psychic.guide.api.services.internal.textrule.TextRuleSet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static psychic.guide.api.services.internal.Tags.*;

public class Parser {
	private static final String BRANDS_LIST_PATH = "data/brands";
	private static final String BLACKLIST_PATH = "data/blacklist";
	private static final String ELEMENTS_BRANDS_SELECTOR = ":containsOwn(%s)";
	private static final int DEFAULT_TAG_LEVEL = 3;
	private static final Set<String> TAGS = Set.of(TAG_A, TAG_B, TAG_H1, TAG_H2, TAG_H3, TAG_H4, TAG_H5);

	private final Options options;
	private final Set<String> brandList;
	private final Set<String> blackList;
	private final TextRule ruleSet;
	private final NeuralNetworkManager networkManager;
	private final Logger logger;

	public Parser(Options options) {
		this.options = options;
		this.brandList = fetchBrandList();
		this.blackList = fetchBlacklist();
		this.ruleSet = new TextRuleSet(options);
		this.networkManager = new NeuralNetworkManager(new NeurophNetwork());
		this.logger = LoggerFactory.getLogger(Parser.class);
	}

	public Set<ResultEntry> parse(Document page, String url) {
		logger.info("Parsing url - {}", url);
		Collection<Element> brandedElements = brandList.stream()
				.flatMap(b -> page.select(String.format(ELEMENTS_BRANDS_SELECTOR, b)).stream())
				.collect(Collectors.toSet());
		Collection<Element> filteredElements = filter(page, brandedElements);
		trainNeuralNetwork(page, brandedElements, filteredElements);
		return filteredElements.stream()
				.map(e -> createResultEntry(e, url))
				.collect(Collectors.toSet());
	}

	public void persist() {
		networkManager.persist();
	}

	private Collection<Element> filter(Document page, Collection<Element> brandedElements) {
		if (options.isUseNeuralNetwork()) {
			return networkManager.calculate(page.children(), brandedElements);
		}
		return brandedElements.stream()
				.filter(this::isResult)
				.collect(Collectors.toSet());
	}

	private boolean isResult(Element element) {
		return !blackList.contains(element.text()) && isResult(element, DEFAULT_TAG_LEVEL);
	}

	private boolean isResult(Element element, int level) {
		return level > 0 && element.parent() != null &&
				(TAGS.contains(element.tagName()) || isResult(element.parent(), --level));
	}

	private void trainNeuralNetwork(Document page, Collection<Element> brandedElements, Collection<Element> filteredElements) {
		if (!options.isUseNeuralNetwork()) {
			networkManager.trainOnThread(page.children(), brandedElements, filteredElements);
		}
	}

	private ResultEntry createResultEntry(Element element, String url) {
		ResultEntry resultEntry = new ResultEntry();
		resultEntry.setResult(ruleSet.modify(element.text()));
		resultEntry.setReferences(new HashSet<>(Collections.singleton(url)));
		return resultEntry;
	}

	private static Set<String> fetchBrandList() {
		Path path = new File(BRANDS_LIST_PATH).toPath();
		try {
			return Files.lines(path).collect(Collectors.toSet());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptySet();
	}

	private static Set<String> fetchBlacklist() {
		Path path = new File(BLACKLIST_PATH).toPath();
		try {
			return Files.lines(path).collect(Collectors.toSet());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptySet();
	}
}
