package psychic.guide.api;

import java.util.UUID;

public class ResultEntry implements Comparable<ResultEntry> {
	private final UUID id = UUID.randomUUID();
	private final String result;
	private int count;
	private boolean bookmark;

	public ResultEntry(String result) {
		this.result = result;
	}

	public ResultEntry(String result, int count, boolean bookmark) {
		this.result = result;
		this.count = count;
		this.bookmark = bookmark;
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

	public void setCount(int count) {
		this.count = count;
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
