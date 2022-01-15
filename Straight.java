/**
 * This class is used to model a hand of Straight
 * 
 * @author Chan Sze Wing
 *
 */
public class Straight extends Hand {
	/**
     * Building a hand of Straight with a player and their chosen list of cards
     * 
     * @param player the index of player 
     * @param cards	the list of selected cards
     */
    public Straight(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    /**
     * Check if the hand is a valid Straight hand
     * 
     * @return true if the hand is Straight, otherwise false
     */
    public boolean isValid() {
        if (this.size() == 5) {
            this.sort();
            int temp = this.getCard(0).getRank();
            for (int i = 1; i < this.size(); i++) {
                if (this.getCard(i).getRank() != (++temp % 13)) {
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
     * @return the string "Straight"
     */
    public String getType() {
        return "Straight";
    }
}
