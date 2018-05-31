package psychic.guide.api.services;

import psychic.guide.api.model.data.ResultEntry;

import java.util.List;

public interface ReportService {
	String generateDoc(List<ResultEntry> entries);
}
