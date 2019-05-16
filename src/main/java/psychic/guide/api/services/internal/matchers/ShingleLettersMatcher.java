package psychic.guide.api.services.internal.matchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShingleLettersMatcher implements StringMatcher {
	private static final int SHINGLE_SIZE = 3;
	private static final double MATCH_PERCENTAGE = .7;

	@Override
	public boolean match(String stringOne, String stringTwo) {
		List<String> shinglesOne = createShingles(stringOne);
		List<String> shinglesTwo = createShingles(stringTwo);
		double matched = shinglesOne.stream()
				.mapToLong(shingleOne -> shinglesTwo.stream()
						.filter(shingleTwo -> Objects.equals(shingleOne, shingleTwo))
						.count())
				.sum();
		double matchPercentage = matched / shinglesOne.size() * shinglesTwo.size();
		return matchPercentage > MATCH_PERCENTAGE;
	}

	List<String> createShingles(String string) {
		List<String> shingles = new ArrayList<>();
		int position = 0;
		while (position < string.length()) {
			int end = Math.min(position + SHINGLE_SIZE, string.length());
			shingles.add(string.substring(position, end));
			position = end;
		}
		return shingles;
	}
}
