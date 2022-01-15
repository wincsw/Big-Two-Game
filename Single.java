
/**
 * This class is used to model a hand of Single
 * 
 * @author Chan Sze Wing
 *
 */
public class Single extends Hand {
	/**
     * Building a hand of Single with a player and their chosen list of cards
     * 
     * @param player the index of player 
     * @param cards	the list of selected cards
     */
    public Single(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }
    /**
     * Check if the hand is a valid Single hand
     * 
     * @return true if the hand is Single, otherwise false
     */
    public boolean isValid() {
        if (this.size() == 1) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Return the hand's type
     * 
     * @return the string "Single"
     */
    public String getType() {
        return "Single";
    }
}
