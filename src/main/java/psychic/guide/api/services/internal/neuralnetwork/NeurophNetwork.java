package psychic.guide.api.services.internal.neuralnetwork;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class NeurophNetwork implements psychic.guide.api.services.internal.neuralnetwork.NeuralNetwork {
	private static final int INPUT_NEURONS_COUNT = 6;
	private static final int HIDDEN_NEURONS_COUNT = 6;
	private static final int OUTPUT_NEURONS_COUNT = 1;
	private static final String FILE_NAME = "data/network.nn";
	private final NeuralNetwork network;

	public NeurophNetwork() {
		network = createNeuralNetwork();
	}

	@Override
	public void train(List<double[]> inputs, List<double[]> outputs) {
		DataSet trainingSet = new DataSet(HIDDEN_NEURONS_COUNT, OUTPUT_NEURONS_COUNT);
		IntStream.range(0, inputs.size())
				.mapToObj(i -> new DataSetRow(inputs.get(i), outputs.get(i)))
				.forEach(trainingSet::addRow);
		network.learn(trainingSet);
	}

	@Override
	public double[] calculate(double[] inputs) {
		network.setInput(inputs);
		network.calculate();
		return network.getOutput();
	}

	public void save() {
		network.save(FILE_NAME);
	}

	private NeuralNetwork createNeuralNetwork() {
		if (new File(FILE_NAME).exists()) {
			return NeuralNetwork.createFromFile(FILE_NAME);
		}
		return new MultiLayerPerceptron(INPUT_NEURONS_COUNT, HIDDEN_NEURONS_COUNT, OUTPUT_NEURONS_COUNT);
	}
}
