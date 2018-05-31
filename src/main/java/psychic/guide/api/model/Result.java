package psychic.guide.api.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "results")
public class Result {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToMany
	@JoinColumn(name = "id")
	private Set<Reference> references;

	private String result;

	public Result() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getResult() {
		return result;
	}

	public Result setResult(String result) {
		this.result = result;
		return this;
	}

	public Set<Reference> getReferences() {
		return references;
	}

	public void setReferences(Set<Reference> references) {
		this.references = references;
	}
}
