package psychic.guide.api.services.internal.textrule;

class TextRuleReview implements TextRule {
	private static final String REVIEW = "review";
	private static final String REVIEW_REGEX = "(?i)review";

	@Override
	public String modify(String text) {
		int review = text.toLowerCase().indexOf(REVIEW);
		if (review != -1) {
			return text.replaceAll(REVIEW_REGEX, "");
		}
		return text;
	}
}
