package psychic.guide.api.services.internal.neuralnetwork;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class NeurophNetwork implements psychic.guide.api.services.internal.neuralnetwork.NeuralNetwork {
	private static final int INPUT_NEURONS_COUNT = 6;
	private static final int OUTPUT_NEURONS_COUNT = 1;
	private static final String FILE_NAME = "data/network.nn";
	private final NeuralNetwork network;

	public NeurophNetwork() {
		this.network = new Perceptron(INPUT_NEURONS_COUNT, OUTPUT_NEURONS_COUNT);
	}

	@Override
	public void train(List<double[]> inputs, List<double[]> outputs) {
		DataSet trainingSet = new DataSet(INPUT_NEURONS_COUNT, OUTPUT_NEURONS_COUNT);
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
}
