package medici;

public class Seat
{
	int florins;
	int bid;

	Card [] boat = new Card[5];

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
}
