package psychic.guide.api.services.internal.textrule;

class TextRuleTrim implements TextRule {
	@Override
	public String modify(String data) {
		return data.trim();
	}
}
