package psychic.guide.api.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long facebookId;
	private int level;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "optionId", referencedColumnName = "id")
	private Options options = new Options();

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

	public Options getOptions() {
		return options;
	}

	public User setOptions(Options options) {
		this.options = options;
		return this;
	}
}
