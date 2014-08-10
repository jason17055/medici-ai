package medici;

import java.util.*;

public class Seat
{
	int florins;
	int bid;

	Card [] boat = new Card[5];
	Map<Suit,Integer> levels = new EnumMap<Suit,Integer>(Suit.class);

	void addToBoat(Card c)
	{
		for (int i = 0; i < boat.length; i++) {
			if (boat[i] == null) {
				boat[i] = c;
				return;
			}
		}

		System.err.println("Warning: attempt to add to a full boat");
	}

	int countInBoat(Suit s)
	{
		int count = 0;
		for (int i = 0; i < boat.length; i++) {
			if (boat[i] != null && boat[i].suit == s) {
				count++;
			}
		}
		return count;
	}

	int getLevel(Suit s)
	{
		Integer x = levels.get(s);
		return x != null ? x.intValue() : 0;
	}
}
