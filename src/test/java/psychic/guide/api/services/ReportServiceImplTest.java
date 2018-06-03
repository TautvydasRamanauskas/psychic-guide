package psychic.guide.api.services;

import org.junit.Test;
import psychic.guide.api.model.data.ResultEntry;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReportServiceImplTest {
	private static final String EXPECTED_DOC =
			"<html>\n" +
			"<body>\n" +
			"<table style=\"border: 1px solid black;\">\n" +
			"<tr><th>Title</th><th>Rating</th></tr>" +
			"<tr><td>ResultOne</td><td>7</td></tr>\n" +
			"<tr><td>ResultTwo</td><td>2</td></tr>\n" +
			"</table>\n" +
			"</body>\n" +
			"</html>";

	@Test
	public void generateDoc() throws Exception {
		List<ResultEntry> resultEntries = List.of(
				createResultEntry("ResultOne", 5, 2),
				createResultEntry("ResultTwo", 3, -1)
		);
		ReportService reportService = new ReportServiceImpl();
		String doc = reportService.generateDoc(resultEntries);
		assertEquals(EXPECTED_DOC, doc);
	}

	private static ResultEntry createResultEntry(String result, int count, int voteValue) {
		ResultEntry resultEntry = new ResultEntry();
		resultEntry.setResult(result);
		resultEntry.setVoteValue(voteValue);
		return resultEntry;
	}

}