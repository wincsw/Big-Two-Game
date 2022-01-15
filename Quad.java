/**
 * This class is used to model a hand of Quad
 * 
 * @author Chan Sze Wing
 *
 */
public class Quad extends Hand{

	/**
     * Building a hand of Quad with a player and their chosen list of cards
     * 
     * @param player the index of player 
     * @param cards	the list of selected cards
     */
	public Quad(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }
	
	/**
     * Check if the hand is a valid Quad hand
     * 
     * @return true if the hand is Quad, otherwise false
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
            return (count1 == 4 && count2 == 1) || (count1 == 1 && count2 == 4);
        } else {
            return false;
        }
    }

	/**
     * Return the hand's type
     * 
     * @return the string "Quad"
     */
    public String getType() {
        return "Quad";
    }
    
    /**
     * Return the top card 
     * (i.e. the card with the highest suit in the quadruplet)
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
	        		rank2 = this.getCard(i).rank;
	        	}
	        	else if (this.getCard(i).getRank() == rank2) {
	        		count2++;
	        	}
	        }
	        int quad_rank = 0;
	        if (count1 == 4) {
	        	quad_rank = rank1;
	        }
	        else {
	        	quad_rank = rank2;
	        }
	        
	        Card top_card = this.getCard(0);
	        for (int i = 1; i < this.size(); i++) {
	        	if ((this.getCard(i).getRank() == quad_rank) && (this.getCard(i).getSuit() > top_card.getSuit())) {
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
