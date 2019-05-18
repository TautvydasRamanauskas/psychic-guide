package psychic.guide.api.services.internal.textrule;

import java.util.ArrayList;
import java.util.Collection;

public class TextRuleSet implements TextRule {
	private final Collection<TextRule> ruleSet;

	public TextRuleSet() { // TODO: create rule for blacklisted words removal
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

//Ticwatch Pro on Amazon – Black/Silver
//Where to buy the Samsung Galaxy Watch:
//Today's best Google Pixel XL deals
//Casio Pro Trek Smart WSD-F20 at Amazon,
//Best Budget: Ticwatch E
//Runner-Up, Best for Fitness Tracking: Fitbit Versa
//Best Shows on Amazon Prime
