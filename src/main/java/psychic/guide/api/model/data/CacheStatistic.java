package psychic.guide.api.model.data;

import psychic.guide.api.model.Search;

public class CacheStatistic implements Comparable<CacheStatistic> {
	private long id;
	private String keyword;
	private long searchCount;
	private long resultsCount;

	public CacheStatistic(Search search, long resultsCount) {
		this.id = search.getId();
		this.keyword = search.getKeyword();
		this.searchCount = search.getSearchCount();
		this.resultsCount = resultsCount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public CacheStatistic setKeyword(String keyword) {
		this.keyword = keyword;
		return this;
	}

	public long getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(long searchCount) {
		this.searchCount = searchCount;
	}

	public long getResultsCount() {
		return resultsCount;
	}

	public void setResultsCount(long resultsCount) {
		this.resultsCount = resultsCount;
	}


	@Override
	public int compareTo(CacheStatistic cacheStatistic) {
		int compareResultCount = Long.compare(cacheStatistic.resultsCount, resultsCount);
		if (compareResultCount == 0) {
			return Long.compare(cacheStatistic.searchCount, searchCount);
		}
		return compareResultCount;
	}
}
