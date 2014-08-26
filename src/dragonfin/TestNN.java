package dragonfin;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class TestNN
{
	NeuralNetwork network;
	MainWindow mainWin;

	static class MyPoint
	{
		double [] inputs;

		MyPoint(double x, double y) {
			inputs = new double[] { x, y };
		}
	}

	static class Focus
	{
		MyPoint p;
		synchronized void set(double x, double y) {
			p = new MyPoint(x, y);
		}

		synchronized MyPoint get() {
			return p;
		}
	}
	Focus focus = new Focus();

	static class Example
	{
		double [] inputs;
		double [] outputs;
	}

	static Example makeExample(MyPoint focusPt)
	{
		Example x = new Example();
		x.inputs = new double[2];
		if (focusPt == null) {
			x.inputs[0] = 2*Math.random()-1.0;
			x.inputs[1] = 2*Math.random()-1.0;
		}
		else {
			x.inputs[0] = 0.1*(Math.random()-.5) + focusPt.inputs[0];
			x.inputs[1] = 0.1*(Math.random()-.5) + focusPt.inputs[1];
		}
		x.outputs = new double[1];
		x.outputs[0] = Math.sqrt(Math.pow(x.inputs[0],2.0)+Math.pow(x.inputs[1],2.0)) < 1.0 ? 1.0 : 0.0;
		return x;
	}

	void initialize()
	{
		mainWin = new MainWindow();
		mainWin.setVisible(true);
	}

	class MainWindow extends JFrame
	{
		NNDisplay display;

		MainWindow()
		{
			super("TestNN");

			this.display = new NNDisplay();
			add(display, BorderLayout.CENTER);

			JPanel buttonPane = new JPanel();
			add(buttonPane, BorderLayout.SOUTH);

			JButton btn1 = new JButton("Run");
			btn1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					onRunClicked();
				}});
			buttonPane.add(btn1);

			pack();
			setLocationRelativeTo(null);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		}
	}

	void safeUpdate()
	{
		try {

		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				mainWin.display.update();
			}});

		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}

	void runTest()
	{
		network = NeuralNetwork.create(2, 12, 1);

		for (int k = 0; k < 200; k++) {

		safeUpdate();

		double runningAvgErr = 0.0;
		MyPoint focusPt = focus.get();

		for (int i = 0; i < 10000; i++) {

			Example x = makeExample(focusPt);
			network.evaluate(x.inputs);
			double thisErr = Math.pow(x.outputs[0]-network.outputNodes[0].outputValue,2.0);
			runningAvgErr = (runningAvgErr * .99) + (thisErr * .01);
//System.out.println(nn);
//System.out.println("EXPECTED: "+x.outputs[0]);
			network.learn(x.outputs);
//			network.evaluate(x.inputs);
//System.out.println("AFTER LEARN: "+nn.outputNodes[0].outputValue);
//System.out.printf("%%                                                 %10.3f\n", runningAvgErr);
		}

		double sumErr = 0.0;
		for (int i = 0; i < 1000; i++) {

			Example x = makeExample(null);
			double [] outputs = network.evaluate(x.inputs);
			double err = outputs[0] - x.outputs[0];
			sumErr += Math.pow(err, 2.0);
		}

		System.out.println("iter " +k + ": err "+Math.sqrt(sumErr/50));
		} //next k
	}

	void onRunClicked()
	{
		Thread t = new Thread() {
			@Override
			public void run() {
				runTest();
			}
		};
		t.start();
	}

	void onMouseDragged(double x, double y)
	{
		focus.set(x, y);
	}

	class NNDisplay extends JComponent
	{
		BufferedImage img = new BufferedImage(512,512,BufferedImage.TYPE_INT_RGB);

		NNDisplay()
		{
			MouseAdapter mouse = new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent evt) {
				onMouseDragged(
					4.0*evt.getX()/img.getWidth() - 2.0,
					4.0*evt.getY()/img.getHeight() - 2.0
					);
			}
			};

			addMouseMotionListener(mouse);
		}

		@Override
		public Dimension getPreferredSize()
		{
			return new Dimension(img.getWidth(), img.getHeight());
		}

		void update()
		{
			int w = img.getWidth();
			int h = img.getHeight();

			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					double [] inputs = new double[] {
						(double)(j - w/2) / (w/4),
						(double)(i - h/2) / (h/4)
					};
					double [] outputs = network.evaluate(inputs);
					img.setRGB(j, i, colorOf(outputs[0]));
				}
			}
			repaint();
		}

		@Override
		public void paintComponent(Graphics g)
		{
			g.drawImage(img, 0, 0, null);
		}
	}

	static int colorOf(double z)
	{
		double zz = 1.0 / (1.0 + Math.exp(-z));

		if (zz < 0.5) {
			int c = (int)Math.round(255*zz/0.5);
			return rgb(c,0,0);
		}
		else {
			int c = (int)Math.round(255*(zz-0.5)/0.5);
			return rgb(255,c,c);
		}
	}

	static int rgb(int r, int g, int b)
	{
		return (r<<16) + (g<<8) + b;
	}

	public static void main(String [] args)
		throws Exception
	{
		TestNN me = new TestNN();
		me.initialize();
	}
}
