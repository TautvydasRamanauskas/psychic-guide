package psychic.guide.api;

public class ResultEntry {
	private final String result;
	private int count;

	public ResultEntry(String result) {
		this.result = result;
	}

	public ResultEntry(String result, int count) {
		this.result = result;
		this.count = count;
	}

	public String getResult() {
		return result;
	}

	public int getCount() {
		return count;
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
}
