/**
 * This class is used to model a hand of Pair
 * 
 * @author Chan Sze Wing
 *
 */
public class Pair extends Hand {
	/**
     * Building a hand of Pair with a player and their chosen list of cards
     * 
     * @param player the index of player 
     * @param cards	the list of selected cards
     */
    public Pair(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }
    
    /**
     * Check if the hand is a valid Pair hand
     * 
     * @return true if the hand is Pair, otherwise false
     */
    public boolean isValid() {
        if (this.size() == 2) {
            int temp = this.getCard(0).getRank();
            for (int i = 1; i < this.size(); i++) {
                if (this.getCard(i).getRank() != temp) {
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
     * @return the string "Pair"
     */
    public String getType() {
        return "Pair";
    }
}
