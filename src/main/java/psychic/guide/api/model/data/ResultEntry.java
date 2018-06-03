package psychic.guide.api.model.data;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

public class ResultEntry implements Comparable<ResultEntry>, Serializable {
	private long id = System.currentTimeMillis();
	private String result;
	private Set<String> references;
	private boolean bookmark;
	private int voteValue;
	private int personalVote;

	public ResultEntry() {

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

	public void setResult(String result) {
		this.result = result;
	}

	public Set<String> getReferences() {
		return references;
	}

	public void setReferences(Set<String> references) {
		this.references = references;
	}

	public boolean isBookmark() {
		return bookmark;
	}

	public void setBookmark(boolean bookmark) {
		this.bookmark = bookmark;
	}

	public int getVoteValue() {
		return voteValue;
	}

	public void setVoteValue(int voteValue) {
		this.voteValue = voteValue;
	}

	public int getPersonalVote() {
		return personalVote;
	}

	public void setPersonalVote(int personalVote) {
		this.personalVote = personalVote;
	}

	@Override
	public boolean equals(Object o) {
		return this == o ||
				o != null && getClass() == o.getClass() && id == ((ResultEntry) o).id;

	}

	@Override
	public int hashCode() {
		return (int) id;
	}

	@Override
	public String toString() {
		return String.format("%s$%d$%s", result, references.size(), references.stream().collect(Collectors.joining("|")));
	}

	@Override
	public int compareTo(ResultEntry resultEntry) {
		int voteCompareResult = Integer.compare(
				resultEntry.references.size() + resultEntry.voteValue,
				references.size() + voteValue
		);
		if (voteCompareResult == 0) {
			return resultEntry.getResult().compareTo(result);
		}
		return voteCompareResult;
	}
}
