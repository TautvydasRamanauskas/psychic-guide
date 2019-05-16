package psychic.guide.api.services.internal;

public interface Globals {
	// Load Balancer
	boolean USE_LOCAL_SEARCH_API = false;
	boolean USE_AZURE_DEMO = false;
	boolean USE_GOOGLE_SCRAPE = true;

	// Final Search Results
	boolean MOCK_SEARCH_FINAL_RESULTS_FILE = false;
	boolean MOCK_SEARCH_FINAL_RESULTS_GENERATE = false;

	// Misc
	boolean USE_SEARCHES_CACHE = false;
	boolean USE_NEURAL_NETWORK = false;
}
