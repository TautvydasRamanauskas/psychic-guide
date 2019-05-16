package psychic.guide.api.services.internal.searchengine;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoadBalancerImplTest {
	private LoadBalancer loadBalancer;

	@Before
	public void setUp() {
		loadBalancer = new LoadBalancerImpl();
	}

	@Test
	public void getLimits() {
		Map<String, Integer> limits = loadBalancer.getLimits();

		assertEquals(SearchApi.values().length, limits.size());

		assertTrue(limits.containsKey("Google Scrape"));
		assertTrue(limits.containsKey("Google"));
		assertTrue(limits.containsKey("Empty"));
		assertTrue(limits.containsKey("Yandex"));
		assertTrue(limits.containsKey("Local"));
		assertTrue(limits.containsKey("Azure Demo"));

		assertEquals(1000, limits.get("Google Scrape").intValue());
		assertEquals(100, limits.get("Google").intValue());
		assertEquals(1, limits.get("Empty").intValue());
		assertEquals(10, limits.get("Yandex").intValue());
		assertEquals(0, limits.get("Local").intValue());
		assertEquals(0, limits.get("Azure Demo").intValue());
	}
}