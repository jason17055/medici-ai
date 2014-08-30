package dragonfin;

public class NeuralNetwork
{
	InputNode [] inputNodes;
	Node [] middleNodes;
	OutputNode [] outputNodes;

	double learningRate = 0.0005;

	private NeuralNetwork()
	{
	}

	public static NeuralNetwork create(int a, int b, int c)
	{
		NeuralNetwork nn = new NeuralNetwork();
		nn.inputNodes = new InputNode[a];
		for (int i = 0; i < a; i++) {
			nn.inputNodes[i] = new InputNode();
		}

		BiasNode bnode = new BiasNode();
		bnode.outputValue = 1.0;

		nn.middleNodes = new Node[b];
		for (int i = 0; i < b; i++) {
			nn.middleNodes[i] = new Node();
			nn.middleNodes[i].createRandomEdgesTo(nn.inputNodes);
			nn.middleNodes[i].addRandomEdgeTo(bnode);
		}

		nn.outputNodes = new OutputNode[c];
		for (int i = 0; i < c; i++) {
			nn.outputNodes[i] = new OutputNode();
			nn.outputNodes[i].createRandomEdgesTo(nn.middleNodes);
			nn.outputNodes[i].addRandomEdgeTo(bnode);
		}
		return nn;
	}

	public double[] evaluate(double [] inputValues)
	{
		assert inputNodes.length == inputValues.length;

		for (int i = 0; i < inputValues.length; i++) {
			inputNodes[i].outputValue = inputValues[i];
		}
		for (int i = 0; i < middleNodes.length; i++) {
			middleNodes[i].calculateOutput();
		}
		double[] outputValues = new double[outputNodes.length];
		for (int i = 0; i < outputNodes.length; i++) {
			outputNodes[i].calculateOutput();
			outputValues[i] = outputNodes[i].outputValue;
		}
		return outputValues;
	}

	void learn(double [] outputValues)
	{
		assert outputNodes.length == outputValues.length;

		for (Node n : inputNodes) {
			n.errorSignal = 0;
		}
		for (Node n : middleNodes) {
			n.errorSignal = 0;
		}

		for (int i = 0; i < outputNodes.length; i++) {
			Node n = outputNodes[i];
			n.errorSignal = outputValues[i] - n.outputValue;
			double errDlta = n.errorSignal * n.activationDerivative(n.inputValue);
			for (int j = 0; j < n.baseNodes.length; j++) {
				n.baseNodes[j].errorSignal += n.baseWeights[j] * errDlta;
				n.baseWeights[j] += learningRate * n.baseNodes[j].outputValue * errDlta;
			}
		}

		for (int i = 0; i < middleNodes.length; i++) {
			Node n = middleNodes[i];
			double errDlta = n.errorSignal * n.activationDerivative(n.inputValue);
			for (int j = 0; j < n.baseNodes.length; j++) {
				n.baseWeights[j] += learningRate * n.baseNodes[j].outputValue * errDlta;
			}
		}
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("INPUTS: ");
		for (int i = 0; i < inputNodes.length; i++) {
			if (i != 0) { sb.append(", "); }
			sb.append(String.format("%10.3f",inputNodes[i].outputValue));
		}
		sb.append("\n");
		sb.append("HIDDEN: ");
		for (int i = 0; i < middleNodes.length; i++) {
			if (i != 0) { sb.append(", "); }
			sb.append(String.format("%10.3f",middleNodes[i].outputValue));
			sb.append("[");
			for (int j = 0; j < middleNodes[i].baseWeights.length; j++) {
				if (j!=0) { sb.append(","); }
				sb.append(String.format("%5.2f",
				middleNodes[i].baseWeights[j]));
			}
			sb.append("]");
		}
		sb.append("\n");
		sb.append("OUTPUT: ");
		for (int i = 0; i < outputNodes.length; i++) {
			if (i != 0) { sb.append(", "); }
			sb.append(String.format("%10.3f",outputNodes[i].outputValue));
		}
		return sb.toString();
	}
}
