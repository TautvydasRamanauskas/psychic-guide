package psychic.guide.api.services.internal.textrule;

class TextRuleParenthesis implements TextRule {
	private static final String PARENTHESIS_MATCHING_PATTERN = "\\((.+?)\\)";

	@Override
	public String modify(String text) {
		return text.replaceAll(PARENTHESIS_MATCHING_PATTERN, "");
	}
}
