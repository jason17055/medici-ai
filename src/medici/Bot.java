package medici;

public class Bot
{
	MediciGame G;
	int mySeatNumber;

	Bot(MediciGame game, int seatNumber)
	{
		this.G = game;
		this.mySeatNumber = seatNumber;
	}

	int requestBid()
	{
		return 1;
	}
}
