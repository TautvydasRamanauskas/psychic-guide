package psychic.guide.api.model;

import psychic.guide.api.services.internal.matchers.MatcherType;

import javax.persistence.*;

@Entity
@Table(name = "options")
public class Options {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private boolean useNeuralNetwork = false;
	private boolean useCache = true;
	private String matcher = MatcherType.CONTAINS.name();
	private int searchApi = Integer.MAX_VALUE;
	private long minRating = 3;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isUseNeuralNetwork() {
		return useNeuralNetwork;
	}

	public void setUseNeuralNetwork(boolean useNeuralNetwork) {
		this.useNeuralNetwork = useNeuralNetwork;
	}

	public boolean isUseCache() {
		return useCache;
	}

	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	public String getMatcher() {
		return matcher;
	}

	public void setMatcher(String matcher) {
		this.matcher = matcher;
	}

	public int getSearchApi() {
		return searchApi;
	} // TODO: delete

	public void setSearchApi(int searchApi) {
		this.searchApi = searchApi;
	}

	public long getMinRating() {
		return minRating;
	}

	public void setMinRating(long minRating) {
		this.minRating = minRating;
	}
}

//https://medium.com/@appaloosastore/string-similarity-algorithms-compared-3f7b4d12f0ff // TODO: compare
