/**
 * This abstract class is used to model a hand of cards
 * 
 * @author Chan Sze Wing
 */
public abstract class Hand extends CardList {
    private CardGamePlayer player;

    /**
     * Building a hand with a player and their chosen list of cards
     * 
     * @param player the index of player 
     * @param cards	the list of selected cards
     */
    public Hand(CardGamePlayer player, CardList cards) {
        this.player = player;
        for (int i = 0; i < cards.size(); i++) {
            this.addCard(cards.getCard(i));
        }
    }

    /**
     * Return the player whom the hand belongs to
     * 
     * @return the player instance of the player
     */
    public CardGamePlayer getPlayer() {
        return this.player;
    }

    /**
     * Return the top card (i.e. the biggest card)
     * 
     * @return the card instance of the top card
     */
    public Card getTopCard() {
        this.sort();
        return this.getCard(this.size() - 1);
    }

    /**
     * Check if the hand beats a specific hand
     * 
     * @param hand the specific hand require to check if it's beaten by the hand
     * @return true if the hand beats the specific hand, otherwise false
     */
    public boolean beats(Hand hand) {
        if (this.getType() == hand.getType()) {
            if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
                return true;
            } else {
                return false;
            }
        } else {
            if (this.getType() == "StrightFlush") {
                return true;
            } else if (hand.getType() == "StrightFlush") {
                return false;
            } else if (this.getType() == "Quad") {
                return true;
            } else if (hand.getType() == "Quad") {
                return false;
            } else if (this.getType() == "FullHouse") {
                return true;
            } else if (hand.getType() == "FullHouse") {
                return false;
            } else if (this.getType() == "Flush") {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Check if the hand is valid
     * (should be 'overridden' in all subclasses)
     * 
     * @return true if the hand is valid, otherwise false
     */
    public abstract boolean isValid();
    
    /**
     * Return the hand's type
     * (should be 'overridden' in all subclasses)
     * 
     * @return the string specifying the hand's type
     */
    public abstract String getType();

}