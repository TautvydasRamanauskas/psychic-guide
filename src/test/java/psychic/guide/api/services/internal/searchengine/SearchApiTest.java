package psychic.guide.api.services.internal.searchengine;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SearchApiTest {
	@Test
	public void getDisplayName() {
		assertEquals("Yandex", SearchApi.YANDEX.getDisplayName());
		assertEquals("Google", SearchApi.GOOGLE.getDisplayName());
		assertEquals("Empty", SearchApi.EMPTY.getDisplayName());
		assertEquals("Azure Demo", SearchApi.AZURE_DEMO.getDisplayName());
		assertEquals("Local", SearchApi.LOCAL.getDisplayName());
		assertEquals("Google Scrape", SearchApi.GOOGLE_SCRAPE.getDisplayName());
	}
}