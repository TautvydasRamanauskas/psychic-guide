package psychic.guide.api.services.internal.searchengine;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import psychic.guide.api.SearchProperties;
import psychic.guide.api.services.internal.model.SearchResult;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static psychic.guide.api.services.internal.PercentEncoder.encode;

public class YandexSearchApi implements SearchApiService {
	private static final String DEMO_FILE = "data/yandex-demo.xml";
	private static final String API_URL_TEMPLATE = "https://yandex.com/search/xml?" +
			"user=" + SearchProperties.getInstance().get("yandex.user.id") +
			"&key=" + SearchProperties.getInstance().get("yandex.api.key") +
			"&query=%s" +
			"&l10n=en" +
			"&sortby=rlv" +
			"&filter=none" +
			"&groupby=attr%%3D%%22%%22.mode%%3Dflat.groups-on-page%%3D10.docs-in-group%%3D1";
	private static final String KEY_YANDEX_SEARCH = "yandexsearch";
	private static final String KEY_RESPONSE = "response";
	private static final String KEY_RESULTS = "results";
	private static final String KEY_GROUPING = "grouping";
	private static final String KEY_GROUP = "group";
	private static final String KEY_DOC = "doc";
	private static final String KEY_URL = "url";

	@Override
	public List<SearchResult> search(String keyword) {
		Document document = fetchResults(keyword);
		if (document != null) {
			NodeList groups = getGroups(document);
			return IntStream.range(0, groups.getLength())
					.mapToObj(groups::item)
					.map(this::getUrl)
					.map(SearchResult::new)
					.collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	private Document readDemoFile() {
		File file = new File(DEMO_FILE);
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			document.getDocumentElement().normalize();
			return document;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Document fetchResults(String keyword) {
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		try {
			URL url = new URL(String.format(API_URL_TEMPLATE, encode(keyword)));
			URLConnection connection = url.openConnection();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			try (InputStream inputStream = connection.getInputStream()) {
				return documentBuilder.parse(inputStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private NodeList getGroups(Document document) {
		Element yandexSearch = (Element) document.getElementsByTagName(KEY_YANDEX_SEARCH).item(0);
		Element response = (Element) yandexSearch.getElementsByTagName(KEY_RESPONSE).item(0);
		Element results = (Element) response.getElementsByTagName(KEY_RESULTS).item(0);
		Element grouping = (Element) results.getElementsByTagName(KEY_GROUPING).item(0);
		return grouping.getElementsByTagName(KEY_GROUP);
	}

	private String getUrl(Node groupNode) {
		Element group = (Element) groupNode;
		Element doc = (Element) group.getElementsByTagName(KEY_DOC).item(0);
		Node url = doc.getElementsByTagName(KEY_URL).item(0);
		return url.getTextContent().trim();
	}
}
