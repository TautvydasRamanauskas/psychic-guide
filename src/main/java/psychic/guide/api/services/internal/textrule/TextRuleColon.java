package psychic.guide.api.services.internal.textrule;

class TextRuleColon implements TextRule {
	private static final String COLON = ":";

	@Override
	public String modify(String text) {
		int indexOfColon = text.indexOf(COLON);
		if (indexOfColon != -1) {
			return text.substring(indexOfColon);
		}
		return text;
	}
}
