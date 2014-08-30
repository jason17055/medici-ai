package dragonfin;

import java.util.Arrays;

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
		double e2x = Math.exp(-2*x);
		return (1-e2x)/(1+e2x);
	}

	double activationDerivative(double x)
	{
		return 1.0-Math.pow(activationFunction(x), 2.0);
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

	void addRandomEdgeTo(Node aNode)
	{
		int len = baseNodes.length;
		baseNodes = Arrays.copyOf(baseNodes, len+1);
		baseNodes[len] = aNode;
		baseWeights = Arrays.copyOf(baseWeights, len+1);
		baseWeights[len] = 2*(Math.random()-0.5);
	}
}

class OutputNode extends Node
{
	double activationFunction(double x) { return x; }
	double activationDerivative(double x) { return 1.0; }
}

class BiasNode extends Node
{
}
