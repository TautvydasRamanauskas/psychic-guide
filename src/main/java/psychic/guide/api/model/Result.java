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
}
