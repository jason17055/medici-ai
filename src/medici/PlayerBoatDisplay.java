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

		for (int i = 0; i < seat.boat.length; i++) {
			Card c = seat.boat[i];

			JLabel goodsLbl = new JLabel();
			goodsLbl.setHorizontalAlignment(SwingConstants.CENTER);
			goodsLbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			if (c != null) {
				goodsLbl.setText(String.format("<html>%s<br>%d",
					c.suit.name(),
					c.value));
			}

			c1.gridx = i+1;
			c1.gridy = 0;
			c1.gridheight = 2;
			add(goodsLbl, c1);
		}
	}
}
