package psychic.guide.api.services;

import org.junit.Before;
import org.junit.Test;
import psychic.guide.api.model.data.ResultEntry;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static psychic.guide.api.services.ServiceModelUtils.createResultEntry;

public class ReportServiceImplTest {
	private static final String EXPECTED_DOC =
			"<html>\n" +
					"<body>\n" +
					"<table style=\"border: 1px solid black;\">\n" +
					"<tr><th>Title</th><th>Rating</th></tr>" +
					"<tr><td>ResultOne</td><td>2</td></tr>\n" +
					"<tr><td>ResultTwo</td><td>-1</td></tr>\n" +
					"</table>\n" +
					"</body>\n" +
					"</html>";

	private ReportService reportService;

	@Before
	public void setUp() {
		reportService = new ReportServiceImpl();
	}

	@Test
	public void generateDoc() {
		final long resultIdOne = 1;
		final long resultIdTwo = 2;
		final String resultOne = "ResultOne";
		final String resultTwo = "ResultTwo";

		ResultEntry resultEntryOne = createResultEntry(resultIdOne, resultOne, 2);
		ResultEntry resultEntryTwo = createResultEntry(resultIdTwo, resultTwo, -1);
		List<ResultEntry> resultEntries = List.of(resultEntryOne, resultEntryTwo);
		String doc = reportService.generateDoc(resultEntries);

		assertEquals(EXPECTED_DOC, doc);
	}
}