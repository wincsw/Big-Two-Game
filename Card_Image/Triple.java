/**
 * This class is used to model a hand of Triple
 * 
 * @author Chan Sze Wing
 *
 */
public class Triple extends Hand {

	/**
     * Building a hand of Triple with a player and their chosen list of cards
     * 
     * @param player the index of player 
     * @param cards	the list of selected cards
     */
    public Triple(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    /**
     * Check if the hand is a valid Triple hand
     * 
     * @return true if the hand is Triple, otherwise false
     */
    public boolean isValid() {
        if (this.size() == 3) {
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
     * @return the string "Triple"
     */
    public String getType() {
        return "Triple";
    }
}
