package psychic.guide.api.neuralnetwork;

public interface NeuralNetwork {
	void train(double[] inputs, double[] outputs);

	double[] calculate(double[] inputs);
}
