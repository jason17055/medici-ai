package medici;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class MainWindow extends JFrame
{
	MediciGame G;

	JButton bidBtn;
	Box playerBoatsContainer;
	JLabel currentLotLbl = new JLabel();
	JLabel yourBoatLbl = new JLabel();
	JLabel scoreLbl = new JLabel();
	JTextField bidEntry = new JTextField();
	Map<Suit,SuitProgressDisplay> suitDisplays;

	static final int GAP1 = 12;
	static final int AUTO_PLAY_DELAY = 300;

	public MainWindow()
	{
		super("Medici AI");

		playerBoatsContainer = new Box(BoxLayout.X_AXIS);
		add(playerBoatsContainer, BorderLayout.NORTH);

		Box box0 = new Box(BoxLayout.X_AXIS);
		add(box0, BorderLayout.CENTER);

		suitDisplays = new EnumMap<Suit,SuitProgressDisplay>(Suit.class);
		suitDisplays.put(Suit.DYES, new SuitProgressDisplay(Suit.DYES));
		suitDisplays.put(Suit.CLOTH, new SuitProgressDisplay(Suit.CLOTH));
		suitDisplays.put(Suit.GRAIN, new SuitProgressDisplay(Suit.GRAIN));
		suitDisplays.put(Suit.SPICES, new SuitProgressDisplay(Suit.SPICES));
		suitDisplays.put(Suit.FURS, new SuitProgressDisplay(Suit.FURS));

		box0.add(suitDisplays.get(Suit.DYES));
		box0.add(Box.createHorizontalStrut(GAP1));
		box0.add(suitDisplays.get(Suit.CLOTH));
		box0.add(Box.createHorizontalStrut(GAP1));
		box0.add(suitDisplays.get(Suit.GRAIN));
		box0.add(Box.createHorizontalStrut(GAP1));
		box0.add(suitDisplays.get(Suit.SPICES));
		box0.add(Box.createHorizontalStrut(GAP1));
		box0.add(suitDisplays.get(Suit.FURS));

		JPanel p1 = new JPanel(new BorderLayout());
		add(p1, BorderLayout.SOUTH);

		Box box1 = new Box(BoxLayout.Y_AXIS);
		box1.add(scoreLbl);
		box1.add(yourBoatLbl);
		p1.add(box1, BorderLayout.NORTH);

		JPanel actionPane = new JPanel();
		p1.add(actionPane, BorderLayout.SOUTH);

		actionPane.add(currentLotLbl);
		bidEntry.setPreferredSize(new Dimension(100,40));
		bidEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onBidClicked();
			}});
		actionPane.add(bidEntry);

		bidBtn = new JButton("Bid");
		bidBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onBidClicked();
			}});
		actionPane.add(bidBtn);

		setPreferredSize(new Dimension(800,480));
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

		checkAutoPlay();
	}

	Timer autoTimer;
	void checkAutoPlay()
	{
		if (autoTimer == null && G.canAutoPlay()) {

			autoTimer = new Timer(AUTO_PLAY_DELAY,
				new ActionListener() {
				public void actionPerformed(ActionEvent evt) {

					autoTimer = null;
					G.autoPlay();
					reloadGame();
					checkAutoPlay();
				}});
			autoTimer.setRepeats(false);
			autoTimer.start();
		}
	}

	public void setGame(MediciGame game)
	{
		this.G = game;
		for (SuitProgressDisplay spd : suitDisplays.values()) {
			spd.G = game;
		}
		reloadGame();
	}

	void reloadGame()
	{
		playerBoatsContainer.removeAll();
		for (int pid = 1; pid < G.C.playerCount; pid++) {
			PlayerBoatDisplay pbd = new PlayerBoatDisplay(G, pid);
			pbd.setBorder(BorderFactory.createTitledBorder(G.C.playerNames[pid]));
			playerBoatsContainer.add(pbd);
		}

		Seat mySeat = G.seats[0];

		currentLotLbl.setText(
			String.format("Bid for %s:", G.getCurrentLot().toString())
			);
		scoreLbl.setText(
			String.format("Your score: %d", mySeat.florins)
			);

		String inBoat = "";
		for (int i = 0; i < mySeat.boat.length; i++) {
			if (mySeat.boat[i] == null) { continue; }
			if (i != 0) { inBoat += ", "; }
			inBoat += mySeat.boat[i].toString();
		}
		yourBoatLbl.setText(
			String.format("Your boat: %s (TOTAL=%d)",
				inBoat,
				mySeat.getBoatTotal()
			));

		for (SuitProgressDisplay spd : suitDisplays.values()) {
			spd.reload();
		}

		bidEntry.setEnabled(G.activeBidder == 0);
		bidBtn.setEnabled(G.activeBidder == 0);
		if (G.activeBidder == 0) {
			bidEntry.requestFocusInWindow();
		}

		repaint();
		validate();
	}
}
