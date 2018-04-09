package psychic.guide.api.services.internal.textrule;

class TextRuleParenthesis implements TextRule {
	private static final String PARENTHESIS_OPEN = "(";

	@Override
	public String modify(String text) {
		int start = text.indexOf(PARENTHESIS_OPEN);
		if (start != -1) {
			return text.substring(0, start);
		}
		return text;
	}
}
