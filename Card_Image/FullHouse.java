/**
 * This class is used to model a hand of Full House
 * 
 * @author Chan Sze Wing
 *
 */
public class FullHouse extends Hand{
	/**
     * Building a hand of Full House with a player and their chosen list of cards
     * 
     * @param player the index of player 
     * @param cards	the list of selected cards
     */
	public FullHouse(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }
	/**
     * Check if the hand is a valid Full House hand
     * 
     * @return true if the hand is Full House, otherwise false
     */
	public boolean isValid() {
        if (this.size() == 5) {
            int count1 = 1, count2 = 0;
            int rank1 = this.getCard(0).getRank(), rank2 = -1;
            for (int i = 1; i < this.size(); i++) {
            	if (this.getCard(i).getRank() == rank1) {
            		count1++;
            	}
            	else if (rank2 == -1) {
            		rank2 = this.getCard(i).getRank();
            		count2++;
            	}
            	else if (this.getCard(i).getRank() == rank2) {
            		count2++;
            	}
            	else {
            		return false;
            	}
            }
            return (count1 == 2 && count2 == 3) || (count1 == 3 && count2 == 2);
        } else {
            return false;
        }
    }
	/**
     * Return the hand's type
     * 
     * @return the string "FullHouse"
     */
    public String getType() {
        return "FullHouse";
    }
    /**
     * Return the top card 
     * (i.e. the card with the highest suit in the triplet)
     * 
     * @return the card instance of the top card
     */
    public Card getTopCard() {
    	if (this.isValid()) {
	    	int count1 = 0, count2 = 0;
	        int rank1 = this.getCard(0).getRank(), rank2 = 0;
	        for (int i = 0; i < this.size(); i++) {
	        	if (this.getCard(i).getRank() == rank1) {
	        		count1++;
	        	}
	        	else if (rank2 == 0) {
	        		rank2 = this.getCard(i).getRank();
	        	}
	        	else if (this.getCard(i).getRank() == rank2) {
	        		count2++;
	        	}
	        }
	        int triple_rank = 0;
	        if (count1 == 3) {
	        	triple_rank = rank1;
	        }
	        else {
	        	triple_rank = rank2;
	        }
	        
	        Card top_card = this.getCard(0);
	        for (int i = 1; i < this.size(); i++) {
	        	if ((this.getCard(i).getRank() == triple_rank) && (this.getCard(i).getSuit() > top_card.getSuit())) {
	        		top_card = this.getCard(i);
	        	}
	        }
	        return top_card;
	        
    	}
    	else {
    		return super.getTopCard();
    	}
    }
}
