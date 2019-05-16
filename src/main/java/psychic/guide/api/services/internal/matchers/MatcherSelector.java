package psychic.guide.api.services.internal.matchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import psychic.guide.api.model.Options;

public class MatcherSelector {
	private final Logger logger;

	public MatcherSelector() {
		logger = LoggerFactory.getLogger(MatcherSelector.class);
	}

	public StringMatcher select(Options options) {
		MatcherType matcherType = getMatcherType(options);
		logger.info("Using {} as string matcher", matcherType.name());
		return matcherType.getMatcher();
	}

	private MatcherType getMatcherType(Options options) {
		try {
			return MatcherType.valueOf(options.getMatcher());
		} catch (IllegalArgumentException e) {
			return MatcherType.CONTAINS;
		}
	}
}
