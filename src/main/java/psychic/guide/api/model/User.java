package psychic.guide.api.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id;

	private long facebookId;

	private int level;

	public long getId() {
		return id;
	}

	public User setId(long id) {
		this.id = id;
		return this;
	}

	public long getFacebookId() {
		return facebookId;
	}

	public User setFacebookId(long facebookId) {
		this.facebookId = facebookId;
		return this;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
