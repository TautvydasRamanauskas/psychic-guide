package psychic.guide.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psychic.guide.api.model.Reference;
import psychic.guide.api.model.Result;
import psychic.guide.api.model.Search;
import psychic.guide.api.model.Vote;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.repository.ReferenceRepository;
import psychic.guide.api.repository.ResultsRepository;
import psychic.guide.api.repository.SearchesRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
	private static final int SHOWN_MOST_POPULAR_SEARCHES = 10;

	private final BookmarkService bookmarkService;
	private final VoteService voteService;
	private final SearchesRepository searchesRepository;
	private final ResultsRepository resultsRepository;
	private final ReferenceRepository referenceRepository;

	@Autowired
	public SearchServiceImpl(BookmarkService bookmarkService, VoteService voteService,
							 SearchesRepository searchesRepository, ResultsRepository resultsRepository,
							 ReferenceRepository referenceRepository) {
		this.bookmarkService = bookmarkService;
		this.voteService = voteService;
		this.searchesRepository = searchesRepository;
		this.resultsRepository = resultsRepository;
		this.referenceRepository = referenceRepository;
	}

	@Override
	public List<ResultEntry> search(String keyword, String ip) {
		saveSearch(keyword);

		List<ResultEntry> results = readResults().stream()
				.map(line -> parseResultEntry(line, ip))
				.sorted()
				.collect(Collectors.toList());

		saveResults(results, keyword);
		return results;
	}

	@Override
	public List<String> mostPopular() {
		Map<String, Long> searches = new HashMap<>();
		for (Search search : searchesRepository.findAll()) {
			searches.put(search.getKeyword(), search.getSearchCount());
		}
		return searches.entrySet().stream()
				.sorted(this::compareSearches)
				.map(Map.Entry::getKey)
				.limit(SHOWN_MOST_POPULAR_SEARCHES)
				.collect(Collectors.toList());
	}

	private void saveSearch(String keyword) {
		Search search = getSearch(keyword);
		search.setSearchCount(search.getSearchCount() + 1);
		searchesRepository.save(search);
	}

	private Search getSearch(String keyword) {
		Iterable<Search> searches = searchesRepository.findAll();
		for (Search search : searches) {
			if (Objects.equals(search.getKeyword(), keyword)) {
				return search;
			}
		}
		return new Search().setKeyword(keyword);
	}

	private void saveResults(List<ResultEntry> results, String keyword) {
		results.forEach(r -> {
			Result existingResult = resultsRepository.findResultByResultAndKeyword(r.getResult(), keyword);
			if (existingResult == null) {
				Result result = new Result();
				result.setResult(r.getResult());
				result.setKeyword(keyword);

				Result newResult = resultsRepository.save(result);
				r.setId(newResult.getId());
				r.getReferences().stream()
						.filter(ref -> referenceRepository.findReferenceByUrlAndResult(ref, result) == null)
						.map(ref -> new Reference().setUrl(ref).setResult(result))
						.forEach(referenceRepository::save);
			} else {
				r.setId(existingResult.getId());
			}
		});
	}

	private static Set<String> readResults() {
		Path path = new File("data/results").toPath();
		try {
			return Files.lines(path).collect(Collectors.toSet());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptySet();
	}

	private ResultEntry parseResultEntry(String line, String ip) {
		String[] splits = line.split("\\$");
		ResultEntry resultEntry = new ResultEntry();
		resultEntry.setResult(splits[0]);
		resultEntry.setCount(Integer.valueOf(splits[1]));
		resultEntry.setReferences(Arrays.stream(splits[2].split("\\|")).collect(Collectors.toSet()));
		resultEntry.setBookmark(bookmarkService.containsBookmark(resultEntry, ip));
		resultEntry.setVoteValue(voteService.calculateVoteValue(resultEntry.getResult()));
		resultEntry.setPersonalVote(getVote(resultEntry.getResult(), ip));
		return resultEntry;
	}

	private int getVote(String title, String ip) {
		Vote vote = voteService.getVote(title, ip);
		return vote == null ? 0 : vote.getValue();
	}

	private int compareSearches(Map.Entry<String, Long> entryOne, Map.Entry<String, Long> entryTwo) {
		long searchCountOne = entryOne.getValue();
		long searchCountTwo = entryTwo.getValue();
		return Long.compare(searchCountTwo, searchCountOne);
	}
}
