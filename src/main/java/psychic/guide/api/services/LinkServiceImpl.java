package psychic.guide.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psychic.guide.api.model.Link;
import psychic.guide.api.model.Result;
import psychic.guide.api.model.User;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.repository.LinksRepository;
import psychic.guide.api.repository.ReferenceRepository;
import psychic.guide.api.repository.ResultsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static psychic.guide.api.ResultsConverter.entriesToResults;
import static psychic.guide.api.ResultsConverter.resultsToEntries;

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
		Link linkObject = linksRepository.getLinkByLink(link);
		if (linkObject == null) {
			return new ArrayList<>();
		}
		List<Result> results = linkObject.getResults();
		return resultsToEntries(results, user, voteService, bookmarkService, referenceRepository);
	}

	@Override
	public Link generate(List<ResultEntry> results) {
		Link link = new Link();
		link.setLink(UUID.randomUUID().toString());
		link.setResults(entriesToResults(results, resultsRepository));
		linksRepository.save(link);
		return link;
	}

}
