package psychic.guide.api.services.internal.matchers;

import java.util.Arrays;
import java.util.Objects;

class ShingleWordsMatcher implements StringMatcher {
	private static final int MATCHES_OFFSET_ONE = 2;
	private static final int MATCHES_OFFSET_TWO = 4;

	@Override
	public boolean match(String stringOne, String stringTwo) {
		if (Objects.equals(stringOne, stringTwo)) {
			return true;
		}

		if (stringOne.isEmpty() || stringTwo.isEmpty()) {
			return false;
		}

		boolean stringOneMatched = matchString(stringOne, stringTwo);
		boolean stringTwoMatched = matchString(stringTwo, stringOne);
		return stringOneMatched && stringTwoMatched;
	}

	private boolean matchString(String stringOne, String stringTwo) {
		String[] words = stringOne.split(" ");
		int wordsCount = stringTwo.split(" ").length;
		long wordsMatched = Arrays.stream(words)
				.filter(w -> containsIgnoreCase(stringTwo, w))
				.count();

		if (wordsCount > MATCHES_OFFSET_TWO) { // TODO: offset 3?
			return wordsCount <= wordsMatched + 2;
		}
		if (wordsCount > MATCHES_OFFSET_ONE) {
			return wordsCount <= wordsMatched + 1;
		}
		return wordsMatched == wordsCount;
	}

	private static boolean containsIgnoreCase(String containingString, String stringToCheck) {
		return containingString.toLowerCase().contains(stringToCheck.toLowerCase());
	}
}
