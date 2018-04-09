package psychic.guide.api.neuralnetwork;

public interface NeuralNetwork {
	void train(boolean isBrand, boolean isLink, boolean isBold, boolean isH3,
			   boolean isH4, boolean isH5, boolean isWord);

	void train(double[] inputs);

	double[] calculate(double[] inputs);
}
