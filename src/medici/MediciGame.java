package medici;

import java.util.*;

public class MediciGame
{
	GameConfig C;
	List<Card> cardSupply;
	Seat[] seats;
	Card current;
	int activePlayer;
	int activeBidder;

	static final int PASSING_BID = Integer.MIN_VALUE;
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
		C.playerCount = 1;
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
			seats[i].florins = 40;
		}

		nextAuction();
	}

	void nextAuction()
	{
		this.current = cardSupply.remove(cardSupply.size()-1);
	}

	void makeBid(int bidAmount)
	{
		seats[activeBidder].bid = bidAmount;

		if (activeBidder == activePlayer) {
			doEndOfAuction();
		}
		else {
			activeBidder = playerAfter(activeBidder);
		}
	}

	int playerAfter(int pid, int count)
	{
		assert count > 0;
		return (pid+count) % C.playerCount;
	}

	int playerAfter(int pid)
	{
		return playerAfter(pid, 1);
	}

	void doEndOfAuction()
	{
		int winner = playerAfter(activePlayer);
		for (int i = 1; i < C.playerCount; i++) {
			int pid = playerAfter(activePlayer, 1+i);
			if (seats[pid].bid > seats[winner].bid) {
				winner = pid;
			}
		}
		System.out.println("in end of auction");
		System.out.printf("winning bid was %d (by %s)\n",
			seats[winner].bid,
			C.playerNames[winner]);

		if (seats[winner].bid != PASSING_BID) {
			seats[winner].addToBoat(current);
			seats[winner].florins -= seats[winner].bid;
		}

		activePlayer = playerAfter(activePlayer);
		nextAuction();
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
