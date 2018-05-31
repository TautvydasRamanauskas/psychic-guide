package psychic.guide.api.model;

import javax.persistence.*;

@Entity
@Table(name = "\"references\"")
public class Reference {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "url", nullable = false) // to use unique constraint length has to be < 1000/4
	private String url;

	@ManyToOne
	@JoinColumn(name = "resultId", referencedColumnName = "id")
	private Result result;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public Reference setUrl(String url) {
		this.url = url;
		return this;
	}

	public Result getResult() {
		return result;
	}

	public Reference setResult(Result result) {
		this.result = result;
		return this;
	}
}
