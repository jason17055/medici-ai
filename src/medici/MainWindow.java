package medici;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainWindow extends JFrame
{
	MediciGame G;

	JLabel currentLotLbl = new JLabel();
	JLabel scoreLbl = new JLabel();
	JTextField bidEntry = new JTextField();

	public MainWindow()
	{
		super("Medici AI");

		Box box1 = new Box(BoxLayout.Y_AXIS);
		box1.add(currentLotLbl);
		box1.add(scoreLbl);
		add(box1, BorderLayout.CENTER);

		JPanel actionPane = new JPanel();
		add(actionPane, BorderLayout.SOUTH);

		actionPane.add(new JLabel("Enter Bid:"));
		bidEntry.setPreferredSize(new Dimension(100,40));
		bidEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onBidClicked();
			}});
		actionPane.add(bidEntry);

		JButton b1 = new JButton("Bid");
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onBidClicked();
			}});
		actionPane.add(b1);

		setPreferredSize(new Dimension(640,480));
		pack();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private void onBidClicked()
	{
		String s = bidEntry.getText();
		try {
			int bid = Integer.parseInt(s);
			G.makeBid(bid);
		}
		catch (NumberFormatException e) {
			e.printStackTrace(System.err);
		}

		bidEntry.setText("");
		bidEntry.requestFocusInWindow();
		reloadGame();
	}

	public void setGame(MediciGame game)
	{
		this.G = game;
		reloadGame();
	}

	void reloadGame()
	{
		currentLotLbl.setText(
			String.format("Current: %s", G.getCurrentLot().toString())
			);
		scoreLbl.setText(
			String.format("Your score: %d", G.seats[0].florins)
			);
	}
}
