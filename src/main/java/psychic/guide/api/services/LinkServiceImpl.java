package psychic.guide.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psychic.guide.api.model.ResultEntry;
import psychic.guide.api.model.Vote;

import java.io.*;
import java.util.*;

@Service
public class LinkServiceImpl implements LinkService {
	private static final String FILE_NAME = "data/searches.ser";
	private final VoteService voteService;
	private final Map<UUID, List<ResultEntry>> links;

	@Autowired
	public LinkServiceImpl(VoteService voteService) {
		this.voteService = voteService;
		this.links = read();
	}

	@Override
	public List<ResultEntry> get(UUID link, String ip) {
		List<ResultEntry> linkEntries = links.getOrDefault(link, new ArrayList<>());
		for (ResultEntry linkEntry : linkEntries) {
			linkEntry.setVoteValue(voteService.calculateVoteValue(linkEntry.getResult()));
			Vote vote = voteService.getVote(linkEntry.getResult(), ip);
			linkEntry.setPersonalVote(vote == null ? 0 : vote.getValue());
		}
		return linkEntries;
	}

	@Override
	public UUID generate(List<ResultEntry> results) {
		UUID link = UUID.randomUUID();
		links.put(link, results);
		save();
		return link;
	}

	public void clear() {
		links.clear();
		save();
	}

	private synchronized void save() {
		try (FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME);
			 ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
			outputStream.writeObject(links);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Map<UUID, List<ResultEntry>> read() {
		try (FileInputStream fileInputStream = new FileInputStream(FILE_NAME);
			 ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {
			//noinspection unchecked
			return (Map<UUID, List<ResultEntry>>) inputStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}
}
