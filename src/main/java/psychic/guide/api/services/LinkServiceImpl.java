package psychic.guide.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psychic.guide.api.model.*;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.repository.LinksRepository;
import psychic.guide.api.repository.ReferenceRepository;
import psychic.guide.api.repository.ResultsRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LinkServiceImpl implements LinkService {
	private final VoteService voteService;
	private final BookmarkService bookmarkService;
	private final ResultsRepository resultsRepository;
	private final LinksRepository linksRepository;
	private final ReferenceRepository referenceRepository;

	@Autowired
	public LinkServiceImpl(VoteService voteService, BookmarkService bookmarkService,
						   ResultsRepository resultsRepository, LinksRepository linksRepository,
						   ReferenceRepository referenceRepository) {
		this.voteService = voteService;
		this.bookmarkService = bookmarkService;
		this.resultsRepository = resultsRepository;
		this.linksRepository = linksRepository;
		this.referenceRepository = referenceRepository;
	}

	@Override
	public List<ResultEntry> get(String link, User user) {
		List<Result> results = linksRepository.getLinkByLink(link).getResults();
		return resultsToEntries(results, user);
	}

	@Override
	public Link generate(List<ResultEntry> results) {
		Link link = new Link();
		link.setLink(UUID.randomUUID().toString());
		link.setResults(entriesToResults(results));
		linksRepository.save(link);
		return link;
	}

	private List<Result> entriesToResults(List<ResultEntry> results) {
		return results.stream()
				.map(ResultEntry::getId)
				.map(resultsRepository::findOne)
				.collect(Collectors.toList());
	}

	private List<ResultEntry> resultsToEntries(List<Result> results, User user) {
		return results.stream()
				.map(r -> {
					ResultEntry entry = new ResultEntry();
					entry.setId(r.getId());
					entry.setResult(r.getResult());
					entry.setCount(r.getRating());
					entry.setPersonalVote(getVote(user, r));
					entry.setVoteValue(voteService.calculateVoteValue(r));
					entry.setBookmark(bookmarkService.containsBookmark(entry, user));
					entry.setReferences(referenceRepository.findReferencesByResult(r).stream().map(Reference::getUrl).collect(Collectors.toSet()));
					return entry;
				})
				.collect(Collectors.toList());
	}

	private int getVote(User user, Result result) {
		Vote vote = voteService.getVote(result, user);
		return vote == null ? 0 : vote.getValue();
	}
}
