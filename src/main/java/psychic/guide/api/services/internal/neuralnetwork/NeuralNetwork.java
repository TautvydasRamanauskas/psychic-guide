package psychic.guide.api.services.internal.neuralnetwork;

import java.util.List;

public interface NeuralNetwork {
	void train(double[] inputs, double[] outputs);

	void train(List<double[]> inputs, List<double[]> outputs);

	double[] calculate(double[] inputs);
}
