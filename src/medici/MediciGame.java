package medici;

import java.util.*;

public class MediciGame
{
	GameConfig C;
	List<Card> cardSupply;
	Seat[] seats;
	Card current;

	public MediciGame()
	{
	}

	Card getCurrentLot()
	{
		return current;
	}

	void initialize(long seed)
	{
		C = new GameConfig();
		C.R = new Random(seed);
		C.playerCount = 6;
		C.playerNames = new String[] {
			"player1", "player2", "player3",
			"player4", "player5", "player6"
			};

		cardSupply = new ArrayList<Card>();
		for (Suit suit : Suit.values())
		{
			if (suit == Suit.NO_SUIT) {
				cardSupply.add(new Card(suit, 10));
			}
			else {
				cardSupply.add(new Card(suit, 0));
				cardSupply.add(new Card(suit, 1));
				cardSupply.add(new Card(suit, 2));
				cardSupply.add(new Card(suit, 3));
				cardSupply.add(new Card(suit, 4));
				cardSupply.add(new Card(suit, 5));
				cardSupply.add(new Card(suit, 5));
			}
		}

		assert cardSupply.size() == 36;

		shuffle(cardSupply);

		seats = new Seat[C.playerCount];
		for (int i = 0; i < C.playerCount; i++) {
			seats[i] = new Seat();
		}

		this.current = cardSupply.remove(0);
	}

	void shuffle(List<Card> deck)
	{
		for (int i = 0; i < deck.size(); i++) {
			int j = i+C.R.nextInt(deck.size()-i);
			Card t = deck.get(i);
			deck.set(i, deck.get(j));
			deck.set(j, t);
		}
	}
}
