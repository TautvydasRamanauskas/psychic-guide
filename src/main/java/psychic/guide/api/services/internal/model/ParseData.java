package psychic.guide.api.services.internal.model;

import org.jsoup.nodes.Element;

public class ParseData {
	private final Element element;
	private String text;

	public ParseData(Element element) {
		this.element = element;
		this.text = element.text();
	}

	public Element getElement() {
		return element;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ParseData parseData = (ParseData) o;
		return text.equals(parseData.text);
	}

	@Override
	public int hashCode() {
		int result = element.hashCode();
		result = 31 * result + text.hashCode();
		return result;
	}
}
