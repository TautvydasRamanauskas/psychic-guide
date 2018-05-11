package psychic.guide.api.services.internal.textrule;

import java.util.ArrayList;
import java.util.List;

public class TextRuleSet implements TextRule {
	private final List<TextRule> ruleSet;

	public TextRuleSet() {
		ruleSet = new ArrayList<>();
		ruleSet.add(new TextRuleParenthesis());
		ruleSet.add(new TextRuleReview());
		ruleSet.add(new TextRuleNumber());
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
