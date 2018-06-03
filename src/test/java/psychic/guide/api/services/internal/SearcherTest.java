package psychic.guide.api.services.internal;

import org.junit.Ignore;
import org.junit.Test;
import psychic.guide.api.model.Options;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.services.internal.searchengine.LoadBalancer;

import java.util.List;

import static org.junit.Assert.assertFalse;

public class SearcherTest {
	@Test
	@Ignore
	public void testSearch() {
		Searcher searcher = new Searcher(new LoadBalancer(new Options()), new Options());
		List<ResultEntry> results = searcher.search("test");
		assertFalse(results.isEmpty());
	}
}