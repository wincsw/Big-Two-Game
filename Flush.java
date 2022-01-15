/**
 * This class is used to model a hand of Flush
 * 
 * @author Chan Sze Wing
 *
 */
public class Flush extends Hand {
    /**
     * Building a hand of Flush with a player and their chosen list of cards
     * 
     * @param player the index of player 
     * @param cards	the list of selected cards
     */
    public Flush(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }
    
    /**
     * Check if the hand is a valid Flush hand
     * 
     * @return true if the hand is Flush, otherwise false
     */
    public boolean isValid() {
        if (this.size() == 5) {
            int temp = this.getCard(0).getSuit();
            for (int i = 1; i < this.size(); i++) {
                if (this.getCard(i).getSuit() != temp) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Return the hand's type
     * 
     * @return the string "Flush"
     */
    public String getType() {
        return "Flush";
    }
    
    /**
     * Check if the hand of Flush beats a specific hand
     * (if the specific hand is a Flush hand with the different suit, compare the suit)
     * 
     * @param hand the specific hand require to check if it's beaten by the hand if Flush
     * @return true if the hand of Flush beats the specific hand, otherwise false
     */
    public boolean beats(Hand hand) {
        if (hand.getType() == "Flush") {
        	if (this.getTopCard().getSuit() > hand.getTopCard().getSuit()) {
        		return super.beats(hand);
        	}
            return (this.getTopCard().getSuit() > hand.getTopCard().getSuit());

        } else {
            return super.beats(hand);
        }
    }

}
