package medici;

import java.util.*;

public class MediciGame
{
	GameConfig C;
	ArrayList<Card> cardSupply;
	Seat[] seats;
	Card current;
	int activePlayer;
	int activeBidder;
	int roundNumber;

	static final int PASSING_BID = Integer.MIN_VALUE;
	public MediciGame()
	{
	}

	void message(String s)
	{
		//TODO
	}

	public MediciGame cloneForMonteCarlo()
	{
		MediciGame g = new MediciGame();
		g.C = this.C;
		g.cardSupply = new ArrayList<Card>();
		g.cardSupply.addAll(this.cardSupply);
		shuffle(g.cardSupply);
		g.seats = new Seat[C.playerCount];
		for (int i = 0; i < g.seats.length; i++) {
			g.seats[i] = this.seats[i].clone();
		}
		g.current = this.current;
		g.activePlayer = this.activePlayer;
		g.activeBidder = this.activeBidder;
		g.roundNumber = this.roundNumber;
		return g;
	}

	Card getCurrentLot()
	{
		return current;
	}

	boolean canAutoPlay()
	{
		return C.bots[activeBidder] != null;
	}

	boolean autoPlay()
	{
System.out.printf("Auto Play: auctioneer %s bidder %s\n",
	C.playerNames[activePlayer],
	C.playerNames[activeBidder]);

		Bot bot = C.bots[activeBidder];
		if (bot != null) {
			int bid = bot.requestBid();
			makeBid(bid);
			return true;
		}
		else {
			return false;
		}
	}

	public boolean endOfGame()
	{
		return this.roundNumber == 3;
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
		C.bots = new Bot[6];
		C.bots[1] = new Bot1(this, 1);
		C.bots[2] = new Bot(this, 2);
		C.bots[3] = new Bot(this, 3);
		C.bots[4] = new Bot(this, 4);
		C.bots[5] = new Bot(this, 5);

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
		this.activeBidder = playerAfter(activePlayer);
	}

	int getCurrentBid()
	{
		int maxBid = 0;
		for (Seat s : seats) {
			if (s.bid != PASSING_BID && s.bid > maxBid) {
				maxBid = s.bid;
			}
		}
		return maxBid;
	}

	void defaultPlay()
	{
		makeBid(1);
	}

	void makeBid(int bidAmount)
	{
		if (seats[activeBidder].canBid() && bidAmount > getCurrentBid()) {
			seats[activeBidder].bid = bidAmount;
		}
		else {
			seats[activeBidder].bid = PASSING_BID;
		}

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

		message(String.format(
			"winning bid was %d (by %s)\n",
			seats[winner].bid,
			C.playerNames[winner])
			);

		if (seats[winner].bid != PASSING_BID) {
			seats[winner].addToBoat(current);
			seats[winner].florins -= seats[winner].bid;
		}

		current = null;
		for (Seat s : seats) {
			s.bid = 0;
		}

		if (countPlayersLeft() == 1 || cardSupply.isEmpty()) {
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

	int countPlayersLeft()
	{
		int count = 0;
		for (Seat s : seats) {
			if (s.hasRoom()) {
				count++;
			}
		}
		return count;
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
				message(String.format("%s: %d points for %s\n",
					rewardType,
					sum / (j-i),
					C.playerNames[order[k]]
					));
				seats[order[k]].florins += sum / (j-i);
			}
			i = j;
		}
	}

	void doEndOfRound()
	{
		// distribute remaining cards (if any)
		for (int i = cardSupply.size()-1; i >= 0; i--) {
			Card c = cardSupply.remove(i);

			for (int j = 0; j < C.playerCount; j++) {
				int pid = (activePlayer+j) % C.playerCount;
				if (seats[pid].hasRoom()) {

					message(String.format("%s goes to %s for free",
						c.toString(),
						C.playerNames[pid]));
					seats[pid].addToBoat(c);
					break;
				}
			}
		}

		int [] biggestBoat_6p = new int[] { 30, 20, 15, 10, 5, 0 };
		rewardPlayersFor("Boat Total", biggestBoat_6p, new ScoreMaker() {
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

		this.roundNumber++;
		if (!endOfGame()) {
			makeDeck();
			nextAuction();
		}
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
