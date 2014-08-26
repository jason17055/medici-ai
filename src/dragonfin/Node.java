package dragonfin;

class Node
{
	double outputValue;
	double inputValue;
	double errorSignal;
	Node [] baseNodes;
	double [] baseWeights;

	void calculateOutput()
	{
		double sum = 0.0;
		for (int i = 0; i < baseNodes.length; i++) {
			sum += baseWeights[i] * baseNodes[i].outputValue;
		}
		this.inputValue = sum;
		this.outputValue = activationFunction(this.inputValue);
	}

	double activationFunction(double x)
	{
		return 1.0/(1 + Math.exp(-x));
	}

	double activationDerivative(double x)
	{
		double tmp = activationFunction(x);
		return tmp * (1-tmp);
	}

	void createRandomEdgesTo(Node [] nodeList)
	{
		baseNodes = new Node[nodeList.length];
		baseWeights = new double[nodeList.length];
		for (int i = 0; i < nodeList.length; i++) {
			baseNodes[i] = nodeList[i];
			baseWeights[i] = 2*(Math.random()-0.5);
		}
	}
}

class OutputNode extends Node
{
	double activationFunction(double x) { return x; }
	double activationDerivative(double x) { return 1.0; }
}
