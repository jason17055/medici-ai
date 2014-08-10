package medici;

import java.awt.*;
import javax.swing.*;

class SuitProgressDisplay extends JPanel
{
	MediciGame G;
	final Suit suit;

	JPanel playersGrid;

	SuitProgressDisplay(Suit suit)
	{
		super(new BorderLayout());

		this.suit = suit;

		JLabel suitLbl = new JLabel(suit.toString());
		add(suitLbl, BorderLayout.NORTH);

	}

	void reload()
	{
		if (playersGrid != null) {

			remove(playersGrid);
			playersGrid = null;
		}

		if (G == null) { return; }

		playersGrid = new JPanel(new GridBagLayout());
		GridBagConstraints gb1 = new GridBagConstraints();
		gb1.gridx = 0;
		gb1.anchor = GridBagConstraints.SOUTHWEST;
		GridBagConstraints gb2 = new GridBagConstraints();
		gb2.gridx = 1;
		gb2.anchor = GridBagConstraints.SOUTHEAST;
		gb2.weightx = 1.0;

		for (int i = 0; i < G.C.playerCount; i++) {
			gb1.gridy = i;
			gb2.gridy = i;

			JLabel nameLbl = new JLabel(G.C.playerNames[i]);
			playersGrid.add(nameLbl, gb1);

			JLabel levelLbl = new JLabel(
				String.format("%d (%d)",
				G.seats[i].countInBoat(suit),
				G.seats[i].getLevel(suit))
				);
			playersGrid.add(levelLbl, gb2);
		}
		add(playersGrid, BorderLayout.CENTER);
	}
}
