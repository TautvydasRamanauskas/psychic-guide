package psychic.guide.api.services.internal.textrule;

import java.util.HashSet;
import java.util.Set;

public class TextRuleSet implements TextRule {
	private final Set<TextRule> ruleSet;

	public TextRuleSet() {
		ruleSet = new HashSet<>();
		ruleSet.add(new TextRuleParenthesis());
		ruleSet.add(new TextRuleReview());
		ruleSet.add(new TextRuleNumber());
		ruleSet.add(new TextRuleTrim());
	}

	@Override
	public String modify(String text) {
		String newText = "";
		for (TextRule rule : ruleSet) {
			newText = rule.modify(newText);
		}
		return newText;
	}
}
