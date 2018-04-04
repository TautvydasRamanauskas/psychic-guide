package psychic.guide.api.model;

import java.io.Serializable;
import java.util.UUID;

public class Vote implements Serializable {
	private final UUID id = UUID.randomUUID();
	private String ip;
	private String title;
	private int value;

	public Vote() {

	}

	public UUID getId() {
		return id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
