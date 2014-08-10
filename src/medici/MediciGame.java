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

		makeDeck();

		seats = new Seat[C.playerCount];
		for (int i = 0; i < C.playerCount; i++) {
			seats[i] = new Seat();
			seats[i].florins = 40;
		}

		nextAuction();
	}

	void makeDeck()
	{
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

		if (allBoatsFull()) {
			doEndOfRound();
		}
		else {
			activePlayer = playerAfter(activePlayer);
			nextAuction();
		}
	}

	boolean allBoatsFull()
	{
		for (Seat s : seats) {
			for (int i = 0; i < s.boat.length; i++) {
				if (s.boat[i] == null) {
					return false;
				}
			}
		}
		return true;
	}

	interface ScoreMaker
	{
		int get(int seatNumber);
	}

	void rewardPlayersFor(String rewardType, int [] rewards, ScoreMaker scorer)
	{
		final int [] scores = new int[C.playerCount];
		for (int i = 0; i < scores.length; i++) {
			scores[i] = scorer.get(i);
		}

		Integer [] order = new Integer[C.playerCount];
		for (int i = 0; i < order.length; i++) {
			order[i] = i;
		}
		Arrays.sort(order, new Comparator<Integer>() {
			public int compare(Integer A, Integer B) {
				int a = A.intValue(), b = B.intValue();
				int a_t = scores[a];
				int b_t = scores[b];
				return -(a_t>b_t ? 1 : a_t<b_t ? -1 : 0);
			}
			});

		for (int i = 0; i < order.length; ) {
			int sum = 0;
			int j = i;
			while (j < order.length && scores[order[j]] == scores[order[i]]) {
				sum += (j < rewards.length ? rewards[j] : 0);
				j++;
			}

			assert j > i;

			for (int k = i; k < j; k++) {
				System.out.printf("%s: %d points for %s\n",
					rewardType,
					sum / (j-i),
					C.playerNames[order[k]]
					);
				seats[order[k]].florins += sum / (j-i);
			}
			i = j;
		}
	}

	void doEndOfRound()
	{
		int [] biggestBoat = new int[] { 30 };
		rewardPlayersFor("Boat Total", biggestBoat, new ScoreMaker() {
			public int get(int seatNumber) {
				return seats[seatNumber].getBoatTotal();
			}});

		for (Seat s : seats) {
			s.adjustLevels();
			s.clearBoat();
		}

		for (final Suit suit : Suit.values()) {
			if (suit == Suit.NO_SUIT) { continue; }

			int [] bestInSuit = new int[] { 10, 5 };
			rewardPlayersFor(suit.name(), bestInSuit, new ScoreMaker() {
			public int get(int seatNumber) {
				return seats[seatNumber].getLevel(suit);
			}});

			for (Seat s : seats) {
				//TODO: give 10/20 bonuses
			}
		}

		makeDeck();
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
