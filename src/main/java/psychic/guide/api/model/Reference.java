package psychic.guide.api.model;

import javax.persistence.*;

@Entity
@Table(name = "\"references\"")
public class Reference {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id;

	private String url;

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
}
