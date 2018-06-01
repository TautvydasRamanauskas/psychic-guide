package psychic.guide.api.model;

import javax.persistence.*;

@Entity
@Table(name = "bookmarks")
public class Bookmark {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	@JoinColumn(name = "userId", referencedColumnName = "id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "resultId", referencedColumnName = "id")
	private Result result;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public Bookmark setUser(User user) {
		this.user = user;
		return this;
	}

	public Result getResult() {
		return result;
	}

	public Bookmark setResult(Result result) {
		this.result = result;
		return this;
	}
}
