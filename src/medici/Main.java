package medici;

public class Main
{
	public static void main(String [] args)
	{
		MediciGame g = new MediciGame();
		g.initialize(0);

		MainWindow w = new MainWindow();
		w.setGame(g);
		w.setVisible(true);
		w.checkAutoPlay();
	}
}
