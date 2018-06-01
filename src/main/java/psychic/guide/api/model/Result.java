package psychic.guide.api.model;

import javax.persistence.*;

@Entity
@Table(name = "results")
public class Result {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String keyword;

	private String result;

	private int rating;

	public Result() {

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

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getResult() {
		return result;
	}

	public Result setResult(String result) {
		this.result = result;
		return this;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
}
