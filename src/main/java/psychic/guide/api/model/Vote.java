package psychic.guide.api.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "votes")
public class Vote implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	@JoinColumn(name = "userId", referencedColumnName = "id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "resultId", referencedColumnName = "id")
	private Result result;

	private int value;

	public Vote() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
