/**
 * This class is used to model a card used in  a Big Two card ga,e
 * 
 * @author Chan Sze Wing
 */
public class BigTwoCard extends Card {
	
	/**
	 * Creates and returns an instance of the BigTwoCard
	 * 
	 * @param suit an int value between 0 and 3 representing the suit of a card:
	 *             <p>
	 *             0 = Diamond, 1 = Club, 2 = Heart, 3 = Spade
	 * @param rank an int value between 0 and 12 representing the rank of a card:
	 *             <p>
	 *             0 = 'A', 1 = '2', 2 = '3', ..., 8 = '9', 9 = '0', 10 = 'J', 11 =
	 *             'Q', 12 = 'K'
	 */
    public BigTwoCard(int suit, int rank) {
        super(suit, rank);
    }
    
    /**
	 * Compares this card with the specified card in the Big Two order.
	 * (i.e. rank 'A' larger than 'K', and '2' is the largest)
	 * 
	 * @param card the card to be compared
	 * @return a negative integer, zero, or a positive integer as this card is less
	 *         than, equal to, or greater than the specified card
	 */
    public int compareTo(Card card) {
        int card_rank = card.rank;
        int this_rank = this.rank;
        if (card_rank < 2) {
            card_rank += 13;
        }
        if (this_rank < 2) {
            this_rank += 13;
        }

        if (this_rank > card_rank) {
            return 1;
        } else if (this_rank < card_rank) {
            return -1;
        } else if (this.suit > card.suit) {
            return 1;
        } else if (this.suit < card.suit) {
            return -1;
        } else {
            return 0;
        }
    }

}
