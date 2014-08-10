package medici;

import java.awt.Color;

public enum Suit
{
	NO_SUIT,
	FURS,
	SPICES,
	GRAIN,
	DYES,
	CLOTH;

	Color color;
	static {

		NO_SUIT.color = Color.WHITE;
		FURS.color = new Color(0x88ff88);
		SPICES.color = new Color(0x7777ff);
		GRAIN.color = new Color(0xffff77);
		DYES.color = new Color(0xff5555);
		CLOTH.color = new Color(0xee11ee);
	}
}
