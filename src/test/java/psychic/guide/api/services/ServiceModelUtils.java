package psychic.guide.api.services;

import psychic.guide.api.model.*;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.model.data.UserIdLevel;

import java.util.HashSet;
import java.util.List;

public class ServiceModelUtils {
	private final static int DEFAULT_USER_LEVEL = 1;
	private final static String DEFAULT_RESULT_TEXT = "";
	private final static String DEFAULT_RESULT_ENTRY_RESULT = "";
	private final static int DEFAULT_RESULT_ENTRY_VOTE_VALUE = 7;

	public static Vote createVote(int value, String result, long resultId, long userId) {
		Vote vote = new Vote();
		vote.setUser(createUser(userId));
		vote.setResult(createResult(resultId, result));
		vote.setValue(value);
		return vote;
	}

	public static User createUser(long userId) {
		return createUser(userId, DEFAULT_USER_LEVEL);
	}

	public static User createUser(long userId, int level) {
		User user = new User();
		user.setId(userId);
		user.setLevel(level);
		return user;
	}

	public static Result createResult(long resultId) {
		return createResult(resultId, DEFAULT_RESULT_TEXT);
	}

	public static Result createResult(long resultId, String resultText) {
		Result result = new Result();
		result.setId(resultId);
		result.setResult(resultText);
		return result;
	}

	public static Search createSearch(long id) {
		Search search = new Search();
		search.setId(id);
		search.setKeyword("");
		return search;
	}

	public static UserIdLevel createUserIdLevel(long userId, int level) {
		UserIdLevel userIdLevel = new UserIdLevel();
		userIdLevel.setUserId(userId);
		userIdLevel.setLevel(level);
		return userIdLevel;
	}

	public static ResultEntry createResultEntry(long id) {
		return createResultEntry(id, DEFAULT_RESULT_ENTRY_RESULT, DEFAULT_RESULT_ENTRY_VOTE_VALUE);
	}

	public static ResultEntry createResultEntry(long id, String result, int voteValue) {
		ResultEntry resultEntry = new ResultEntry();
		resultEntry.setId(id);
		resultEntry.setResult(result);
		resultEntry.setVoteValue(voteValue);
		resultEntry.setReferences(new HashSet<>());
		return resultEntry;
	}

	public static Bookmark createBookmark(long id, Result result) {
		Bookmark bookmark = new Bookmark();
		bookmark.setId(id);
		bookmark.setResult(result);
		return bookmark;
	}

	public static Link createLink(long linkId, List<Result> results) {
		Link link = new Link();
		link.setId(linkId);
		link.setResults(results);
		return link;
	}

	public static Options createOptions(boolean useCache) {
		Options options = new Options();
		options.setUseCache(useCache);
		return options;
	}
}
