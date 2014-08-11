package medici;

import static medici.MediciGame.PASSING_BID;

class Bot1 extends Bot
{
	static final int ITERATIONS = 100;

	Bot1(MediciGame game, int seatNumber) { super(game, seatNumber); }

	@Override
	public int requestBid()
	{
		int currentBid = G.getCurrentBid();

		int withSum = 0;
		for (int i = 0; i < ITERATIONS; i++) {

			MediciGame g = G.cloneForMonteCarlo();
			g.makeBid(currentBid+1);
			while (!g.endOfGame()) {
				g.defaultPlay();
			}
			withSum += g.seats[mySeatNumber].florins;
		}

		int withoutSum = 0;
		for (int i = 0; i < ITERATIONS; i++) {

			MediciGame g = G.cloneForMonteCarlo();
			g.makeBid(PASSING_BID);
			while (!g.endOfGame()) {
				g.defaultPlay();
			}
			withoutSum += g.seats[mySeatNumber].florins;
		}

		System.out.printf("to take:%5.1f,  to NOT take:%5.1f\n",
			(double)withSum / ITERATIONS,
			(double)withoutSum / ITERATIONS);

		if (withSum > withoutSum) {
			return currentBid + 1;
		}
		else {
			return PASSING_BID;
		}
	}
}
