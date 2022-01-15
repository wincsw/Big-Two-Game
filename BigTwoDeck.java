/**
 * This class is used to model a deck of cards for Big Two card games.
 * 
 * @author Chan Sze Wing
 */
public class BigTwoDeck extends Deck {
    /**
     * Initializes the deck of Big Two cards.
	 * (i.e. rank 'A' larger than 'K', and '2' is the largest)
     */
    public void initialize() {
        removeAllCards();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                BigTwoCard card = new BigTwoCard(i, j);
                addCard(card);
            }
        }
    }
}
