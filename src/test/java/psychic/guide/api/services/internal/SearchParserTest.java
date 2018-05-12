package psychic.guide.api.services.internal;

import org.junit.Ignore;
import org.junit.Test;
import psychic.guide.api.model.ResultEntry;
import psychic.guide.api.services.internal.searchengine.LoadBalancer;

import java.util.List;

import static org.junit.Assert.assertFalse;

public class SearchParserTest {
	@Test
	@Ignore
	public void testSearch() {
		SearchParser searchParser = new SearchParser(new LoadBalancer());
		List<ResultEntry> results = searchParser.search("test");
		assertFalse(results.isEmpty());
	}
}