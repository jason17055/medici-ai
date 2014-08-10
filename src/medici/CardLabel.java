package medici;

import java.awt.*;
import javax.swing.*;

class CardLabel extends JComponent
{
	Card c;

	public CardLabel()
	{
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	public void setCard(Card c)
	{
		this.c = c;
	}

	@Override
	public Dimension getMinimumSize()
	{
		return new Dimension(32,32);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(32,32);
	}

	@Override
	public void paintComponent(Graphics gr)
	{
		if (c == null) return;

		gr.setFont(new Font("Arial", Font.PLAIN, 8));
		String s = c.suit.name();
		FontMetrics fm = gr.getFontMetrics();

		int cx = fm.stringWidth(s);
		int cy = fm.getAscent();

		gr.drawString(s, getWidth()/2-cx/2, getHeight()-3);

		gr.setFont(new Font("Arial", Font.BOLD, 24));
		s = String.format("%d", c.value);
		fm = gr.getFontMetrics();
		cx = fm.stringWidth(s);
		cy = fm.getAscent();

		gr.drawString(s, getWidth()/2-cx/2, cy-4);
	}
}
