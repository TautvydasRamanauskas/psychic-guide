package psychic.guide.api.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "links")
public class Link {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String link;

	@ManyToMany
	@JoinColumn(name = "id")
	private List<Result> results;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}
}
