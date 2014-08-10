package medici;

import java.awt.*;
import javax.swing.*;

class PlayerBoatDisplay extends JPanel
{
	final MediciGame G;
	final int seatNumber;

	PlayerBoatDisplay(MediciGame game, int seatNumber)
	{
		super(new GridBagLayout());

		this.G = game;
		this.seatNumber = seatNumber;
		Seat seat = G.seats[seatNumber];

		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx=0;
		c1.gridy=0;
		add(new JLabel(G.C.playerNames[seatNumber]), c1);

		c1.gridy = 1;
		add(new JLabel(String.format("$%d", seat.florins)), c1);

		JPanel pnl = new JPanel(new GridBagLayout());
		c1.gridx = 1;
		c1.gridy = 0;
		c1.gridheight = 2;
		add(pnl, c1);

		for (int i = 0; i < seat.boat.length; i++) {

			Card c = seat.boat[i];
			CardLabel lbl = new CardLabel();
			lbl.setCard(c);

			c1.gridx = i < 2 ? (i*2+1) : (i*2-4);
			c1.gridwidth = 2;
			c1.gridy = i < 2 ? 0 : 1;
			c1.gridheight = 1;
			pnl.add(lbl, c1);
		}
	}
}
