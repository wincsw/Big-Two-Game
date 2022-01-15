
/**
 * This class is used to model a hand of Straight Flush
 * 
 * @author Chan Sze Wing
 *
 */
public class StraightFlush extends Hand{
    /**
     * Building a hand of Straight Flush with a player and their chosen list of cards
     * 
     * @param player the index of player 
     * @param cards	the list of selected cards
     */
	public StraightFlush(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

	/**
     * Check if the hand is a valid Straight Flush hand
     * 
     * @return true if the hand is Straight Flush, otherwise false
     */
    public boolean isValid() {
        if (this.size() == 5) {
        	this.sort();
            int suit = this.getCard(0).getSuit(), rank = this.getCard(0).getRank();
            for (int i = 1; i < this.size(); i++) {
                if ((this.getCard(i).getSuit() != suit) || (this.getCard(i).getRank() != (++rank % 13))) {
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
     * @return the string "StraightFlush"
     */
    public String getType() {
        return "StraightFlush";
    }

}
