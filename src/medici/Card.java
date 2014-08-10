package medici;

public class Card
{
	final Suit suit;
	final int value;

	public Card(Suit suit, int value)
	{
		this.suit = suit;
		this.value = value;
	}

	@Override
	public String toString()
	{
		return String.format("%d %s",
			value,
			suit.toString()
			);
	}
}
