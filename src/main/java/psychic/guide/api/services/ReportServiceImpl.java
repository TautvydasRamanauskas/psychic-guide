package psychic.guide.api.services;

import org.springframework.stereotype.Service;
import psychic.guide.api.model.ResultEntry;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
	@Override
	public String generateDoc(List<ResultEntry> entries) {
		StringBuilder doc = new StringBuilder();
		doc.append("<html>\n<body>\n<table>\n");
		doc.append("<th><td>Title</td><td>Rating</td></th>");
		entries.stream().map(this::createRow).forEach(doc::append);
		doc.append("</table>\n</body>\n</html>");
		return doc.toString();
	}

	private String createRow(ResultEntry entry) {
		return String.format("<tr><td>%s</td><td>%s</td></tr>\n",
				entry.getResult(), entry.getCount() + entry.getVoteValue());
	}
}
