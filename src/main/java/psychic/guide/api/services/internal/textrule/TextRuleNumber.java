package psychic.guide.api.services.internal.textrule;

class TextRuleNumber implements TextRule {
	private static final String NUMBER_REGEX = "\\d+.";

	@Override
	public String modify(String text) {
		return text.replaceFirst(NUMBER_REGEX, "");
	}
}
