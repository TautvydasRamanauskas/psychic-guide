package psychic.guide.api.services.internal.textrule;

import psychic.guide.api.model.Options;

import java.util.ArrayList;
import java.util.List;

public class TextRuleSet implements TextRule {
	private final List<TextRule> ruleSet;

	public TextRuleSet(Options options) {
		ruleSet = new ArrayList<>();
		if (options.isUseTextRuleParenthesis()) {
			ruleSet.add(new TextRuleParenthesis());
		}
		if (options.isUseTextRuleReview()) {
			ruleSet.add(new TextRuleReview());
		}
		if (options.isUseTextRuleNumber()) {
			ruleSet.add(new TextRuleNumber());
		}
		ruleSet.add(new TextRuleTrim());
	}

	@Override
	public String modify(String text) {
		String newText = text;
		for (TextRule rule : ruleSet) {
			newText = rule.modify(newText);
		}
		return newText;
	}
}
