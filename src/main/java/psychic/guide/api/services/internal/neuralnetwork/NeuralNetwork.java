package psychic.guide.api.services.internal.neuralnetwork;

public interface NeuralNetwork {
	void train(double[] inputs, double[] outputs);

	double[] calculate(double[] inputs);
}
