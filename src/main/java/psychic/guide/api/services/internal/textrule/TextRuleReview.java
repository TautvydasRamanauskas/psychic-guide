package psychic.guide.api.services.internal.textrule;

class TextRuleReview implements TextRule {
	private static final String REVIEW = "Review";

	@Override
	public String modify(String text) {
		int review = text.indexOf(REVIEW);
		if (review != -1) {
			return text.replaceAll(REVIEW, "");
		}
		return text;
	}
}
