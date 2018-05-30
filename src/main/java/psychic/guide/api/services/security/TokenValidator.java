package psychic.guide.api.services.security;

public interface TokenValidator {
	boolean validate(String accessToken, long userId);
}
