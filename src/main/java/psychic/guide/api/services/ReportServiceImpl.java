package psychic.guide.api.services;

import org.springframework.stereotype.Service;
import psychic.guide.api.model.data.ResultEntry;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
	@Override
	public String generateDoc(List<ResultEntry> entries) {
		StringBuilder doc = new StringBuilder();
		doc.append("<html>\n<body>\n<table style=\"border: 1px solid black;\">\n");
		doc.append("<tr><th>Title</th><th>Rating</th></tr>");
		entries.stream().map(this::createRow).forEach(doc::append);
		doc.append("</table>\n</body>\n</html>");
		return doc.toString();
	}

	private String createRow(ResultEntry entry) {
		return String.format("<tr><td>%s</td><td>%s</td></tr>\n",
				entry.getResult(), entry.getCount() + entry.getVoteValue());
	}
}
