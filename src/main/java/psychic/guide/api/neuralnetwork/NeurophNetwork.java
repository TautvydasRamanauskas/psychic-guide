package psychic.guide.api.neuralnetwork;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;
import org.springframework.stereotype.Service;

@Service
public class NeurophNetwork implements psychic.guide.api.neuralnetwork.NeuralNetwork {
	private static final int INPUT_NEURONS_COUNT = 2;
	private static final int OUTPUT_NEURONS_COUNT = 1;
	private static final String FILE_NAME = "data/network.nn";
	private final NeuralNetwork network;

	public NeurophNetwork() {
		this.network = new Perceptron(INPUT_NEURONS_COUNT, OUTPUT_NEURONS_COUNT);
	}

	@Override
	public void train(boolean isBrand, boolean isLink, boolean isBold, boolean isH3,
					  boolean isH4, boolean isH5, boolean isWord) {
		train(new double[]{boolToDouble(isBrand), boolToDouble(isLink), boolToDouble(isBold), boolToDouble(isH3),
				boolToDouble(isH4), boolToDouble(isH5), boolToDouble(isWord)});
	}

	@Override
	public void train(double[] inputs) {
		DataSet trainingSet = new DataSet(INPUT_NEURONS_COUNT, OUTPUT_NEURONS_COUNT);
		trainingSet.addRow(new DataSetRow(inputs));
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

	private static double boolToDouble(boolean bool) {
		return bool ? 1 : 0;
	}
}
