package psychic.guide.api.services.internal.neuralnetwork;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

public class NeuralNetworkManagerTest {
	@Test
	public void testTrainer() {
		List<Element> brandedElements = List.of(new Element("tag"), new Element("tag"), new Element("tag"));
		List<Element> filteredElements = List.of(new Element("tag"), new Element("tag"), new Element("tag"));
		Elements elements = createElements(brandedElements, filteredElements);

		NeurophNetwork neuralNetwork = Mockito.mock(NeurophNetwork.class);
		NeuralNetworkManager trainer = new NeuralNetworkManager(neuralNetwork);
		trainer.train(elements, brandedElements, filteredElements);

		Mockito.verify(neuralNetwork).train(Mockito.any(), Mockito.any());
	}

	private Elements createElements(List<Element> brandedElements, List<Element> filteredElements) {
		Elements elements = new Elements();
		elements.addAll(brandedElements);
		elements.addAll(filteredElements);
		return elements;
	}
}