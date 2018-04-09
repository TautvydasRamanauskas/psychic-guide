package psychic.guide.api.services.internal;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import psychic.guide.api.model.ResultEntry;
import psychic.guide.api.services.internal.model.ParseData;
import psychic.guide.api.services.internal.textrule.TextRule;
import psychic.guide.api.services.internal.textrule.TextRuleSet;

import java.util.*;
import java.util.stream.Collectors;

public class Parser {
	private final Set<String> brandList;
	private final List<ResultEntry> results;
	private final TextRule ruleSet;

	public Parser(Set<String> brandList) {
		this.brandList = brandList;
		this.results = new ArrayList<>();
		this.ruleSet = new TextRuleSet();
	}

	public Collection<ResultEntry> getResults() {
		return results;
	}

	public void parse(Document page) {
		results.clear();

		List<ParseData> results = brandList.stream()
				.flatMap(b -> page.select(String.format(":containsOwn(%s)", b)).stream())
				.map(ParseData::new)
				.filter(d -> isResult(d.getElement()))
				.collect(Collectors.toList());

		results.forEach(d -> d.setText(ruleSet.modify(d.getText())));
		Set<ResultEntry> resultSet = results.stream().map(d -> new ResultEntry()).collect(Collectors.toSet());

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
}
