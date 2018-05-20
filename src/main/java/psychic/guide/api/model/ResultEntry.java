package psychic.guide.api.model;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ResultEntry implements Comparable<ResultEntry>, Serializable {
	private final UUID id = UUID.randomUUID();
	private String result;
	private Set<String> references;
	private int count;
	private boolean bookmark;
	private int voteValue;
	private int personalVote;

	public ResultEntry() {

	}

	public UUID getId() {
		return id;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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
				o != null && getClass() == o.getClass() && id.equals(((ResultEntry) o).id);

	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return String.format("%s$%d$%s", result, count, references.stream().collect(Collectors.joining("|")));
	}

	@Override
	public int compareTo(ResultEntry resultEntry) {
		int voteCompareResult = Integer.compare(resultEntry.getCount() + resultEntry.getVoteValue(), count + voteValue);
		if (voteCompareResult == 0) {
			return resultEntry.getResult().compareTo(result);
		}
		return voteCompareResult;
	}
}
