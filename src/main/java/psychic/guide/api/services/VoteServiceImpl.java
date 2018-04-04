package psychic.guide.api.services;

import org.springframework.stereotype.Service;
import psychic.guide.api.model.Vote;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class VoteServiceImpl implements VoteService {
	private static final String FILE_NAME = "data/votes.ser";
	private final Map<String, Map<String, Vote>> votes = read(); // title -> ip -> vote

	@Override
	public int calculateVoteValue(String title) {
		Map<String, Vote> votesByIp = votes.get(title);
		if (votesByIp != null) {
			return votesByIp.values().stream()
					.map(Vote::getValue)
					.reduce((vOne, vTwo) -> vOne + vTwo)
					.orElse(0);
		}
		return 0;
	}

	@Override
	public void addVote(Vote vote) {
		Map<String, Vote> votesByIp = votes.computeIfAbsent(vote.getTitle(), t -> new HashMap<>());
		votesByIp.put(vote.getIp(), vote);
		save();
	}

	@Override
	public void removeVote(Vote vote) {
		Map<String, Vote> votesByIp = votes.computeIfAbsent(vote.getTitle(), t -> new HashMap<>());
		votesByIp.remove(vote.getIp());
		save();
	}

	@Override
	public Vote getVote(String title, String ip) {
		Map<String, Vote> votesByIp = votes.get(title);
		if (votesByIp != null) {
			return votesByIp.get(ip);
		}
		return null;
	}

	public void clear() {
		votes.clear();
		save();
	}

	private synchronized void save() {
		try (FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME);
			 ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
			outputStream.writeObject(votes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Map<String, Map<String, Vote>> read() {
		try (FileInputStream fileInputStream = new FileInputStream(FILE_NAME);
			 ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {
			//noinspection unchecked
			return (Map<String, Map<String, Vote>>) inputStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}
}
