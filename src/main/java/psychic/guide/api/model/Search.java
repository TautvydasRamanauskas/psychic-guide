package psychic.guide.api.model;

import javax.persistence.*;

@Entity
@Table(name = "searches")
public class Search {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id;

	private String keyword;

	private long searchCount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public Search setKeyword(String keyword) {
		this.keyword = keyword;
		return this;
	}

	public long getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(long searchCount) {
		this.searchCount = searchCount;
	}
}
