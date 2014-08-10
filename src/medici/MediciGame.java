package medici;

import java.util.*;

public class MediciGame
{
	Random R;

	public MediciGame()
	{
	}

	void initialize(long seed)
	{
		R = new Random(seed);

		ArrayList<Card> deck = new ArrayList<Card>();
		for (Suit suit : Suit.values())
		{
			if (suit == Suit.NO_SUIT) {
				deck.add(new Card(suit, 10));
			}
			else {
				deck.add(new Card(suit, 0));
				deck.add(new Card(suit, 1));
				deck.add(new Card(suit, 2));
				deck.add(new Card(suit, 3));
				deck.add(new Card(suit, 4));
				deck.add(new Card(suit, 5));
				deck.add(new Card(suit, 5));
			}
		}

		assert deck.size() == 36;

		shuffle(deck);

		for (Card c : deck) {
			System.out.println(c);
		}
	}

	void shuffle(List<Card> deck)
	{
		for (int i = 0; i < deck.size(); i++) {
			int j = i+R.nextInt(deck.size()-i);
			Card t = deck.get(i);
			deck.set(i, deck.get(j));
			deck.set(j, t);
		}
	}
}
