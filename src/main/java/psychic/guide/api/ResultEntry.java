package psychic.guide.api;

import java.io.Serializable;
import java.util.UUID;

public class ResultEntry implements Comparable<ResultEntry>, Serializable {
	private final UUID id = UUID.randomUUID();
	private String result;
	private int count;
	private boolean bookmark;

	public ResultEntry() {

	}

	public UUID getId() {
		return id;
	}

	public String getResult() {
		return result;
	}

	public int getCount() {
		return count;
	}

	public boolean isBookmark() {
		return bookmark;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setBookmark(boolean bookmark) {
		this.bookmark = bookmark;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ResultEntry that = (ResultEntry) o;
		return that.result.contains(result) || result.contains(that.result);
	}

	@Override
	public int hashCode() {
		return result != null ? result.hashCode() : 0;
	}

	@Override
	public int compareTo(ResultEntry resultEntry) {
		return Integer.compare(resultEntry.getCount(), this.count);
	}
}
