package psychic.guide.api.services.internal.matchers;

class ContainsMatcher implements StringMatcher {
	@Override
	public boolean match(String stringOne, String stringTwo) {
		return stringOne.contains(stringTwo) || stringTwo.contains(stringOne);
	}
}
