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
		add(new JLabel(String.format("\u0192%d", seat.florins)), c1);

		Box bx1 = new Box(BoxLayout.X_AXIS);
		c1.gridx = 1;
		c1.gridy = 0;
		c1.gridheight = 1;
		add(bx1, c1);

		Box bx2 = new Box(BoxLayout.X_AXIS);
		c1.gridy = 1;
		add(bx2, c1);

		bx1.add(Box.createHorizontalStrut(16));

		for (int i = 0; i < seat.boat.length; i++) {

			Card c = seat.boat[i];
			CardLabel lbl = new CardLabel();
			lbl.setCard(c);

			(i<2 ? bx1 : bx2).add(lbl);
		}

		bx1.add(Box.createHorizontalStrut(16));
	}
}
