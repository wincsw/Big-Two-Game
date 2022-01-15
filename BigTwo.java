import java.util.ArrayList;

/**
 * This class is use to mode and launch the Big Two card game.
 * 
 * @author Chan Sze Wing
 */
public class BigTwo implements CardGame{
    private int numOfPlayers = 4;
    private Deck deck = new Deck();
    private ArrayList<CardGamePlayer> playerList = new ArrayList<CardGamePlayer>(this.numOfPlayers);
    private ArrayList<Hand> handOnTable = new ArrayList<Hand>();
    private int currentPlayerIdx;
    private BigTwoGUI ui;
    private BigTwoClient client;

    /**
     * Create a Big Two card game and create 4 players and a UI object
     */
    public BigTwo() {
        for (int i = 0; i < this.numOfPlayers; i++) {
            CardGamePlayer temp = new CardGamePlayer();
            this.playerList.add(temp);
        }
        this.ui = new BigTwoGUI(this);
        this.client = new BigTwoClient(this, this.ui);
    }

    /** 
     * Returns the number of players
     *
     * @return an integer of the number of players
     */
    public int getNumOfPlayers() {
        return this.numOfPlayers;
    }

    /**
     * Return the deck of cards
     * 
     * @return a deck object representing the deck of cards
     */
    public Deck getDeck() {
        return this.deck;
    }

    /**
     * Return a list of player
     * 
     * @return an arrayList of player 
     */
    public ArrayList<CardGamePlayer> getPlayerList() {
        return this.playerList;
    }

    /**
     * Return a list of hands played on the table 
     * 
     * @return a arrayList hands representing the hands played on the table,
     * with the last hand the most recent one on the table 
     */
    public ArrayList<Hand> getHandsOnTable() {
        return this.handOnTable;
    }

    /**
     * Return the index of the current player
     * 
     * @return an integer of the index of the current player
     */
    public int getCurrentPlayerIdx() {
        return currentPlayerIdx;
    }
    
    public BigTwoClient getClient() {
    	return client;
    }
    
    public BigTwoGUI getGUI() {
    	return ui;
    }
    
    /**
     * Return a valid hand from a list of cards
     * 
     * @param player the player who holds the cards
     * @param cards the list of card indices selected by the player
     * @return	the object of the specific hand type compose by the cards, or 
     * null if no valid hand can be composed
     */
    public static Hand composeHand(CardGamePlayer player, CardList cards) {
    	if (cards.size() == 1) {
    		return (new Single(player, cards));
    	}
    	else if ((cards.size() == 2) && ((new Pair(player, cards)).isValid())) {
    		return (new Pair(player, cards));
    	}
    	else if ((cards.size() == 3) && ((new Triple(player, cards)).isValid())) {
    		return (new Triple(player, cards));
    	}
    	else if ((cards.size() > 5)) {
    		return null;
    	}
    	else {
    		if ((new StraightFlush(player, cards)).isValid()) {
    			return (new StraightFlush(player, cards));
    		}
    		else if ((new Quad(player, cards)).isValid()) {
    			return (new Quad(player, cards));
    		}
    		else if ((new FullHouse(player, cards)).isValid()) {
    			return (new FullHouse(player, cards));
    		}
    		else if ((new Flush(player, cards)).isValid()) {
    			return (new Flush(player, cards));
    		}
    		else if ((new Straight(player, cards)).isValid()) {
    			return (new Straight(player, cards));
    		}
    		else {
    			return null;
    		}
    		
    	}
    }

    /**
     * Start / Restart the game
     * 
     * @param deck the shuffled deck of card
     */
    public void start(Deck deck) {
        // remove all cards from table
        this.handOnTable.removeAll(this.handOnTable);
        
        // remove all cards from players
        for (int i = 0; i < this.numOfPlayers; i++) {
        	this.playerList.get(i).removeAllCards();
        }
        
        // distribute cards to player & find player who holds the Three of Diamonds
        for (int i = 0; i < deck.size(); i += 4) {
        	for (int j = 0; j < this.numOfPlayers; j++) {
        		if ((deck.getCard(i+j).getRank() == 2) && (deck.getCard(i+j).getSuit() == 0)) {
        			this.currentPlayerIdx = j;
        			this.ui.setActivePlayer(j);
        		}
        		this.playerList.get(j).addCard(deck.getCard(i+j));
        	}
        }
        
        for (int i = 0; i < this.numOfPlayers; i++) {
        	this.playerList.get(i).getCardsInHand().sort();
        }
        
        this.ui.repaint();
        this.ui.promptActivePlayer();
    }
    
    /**
     * Make a move by a player with a list of card indices
     * 
     * @param playerIdx the index of the player
     * @param cardIdx the list of card indices selected by the player for the move
     */
    public void makeMove(int playerIdx, int[] cardIdx) {
    	CardGameMessage message = new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx);
		this.client.sendMessage(message);
    	return;
    }
    
    /**
     * Check is the move made by a player valid
     * 
     * @param playerIdx the index of the player
     * @param cardIdx the list of card indices selected by the player for the move
     */
    public void checkMove(int playerIdx, int[] cardIdx) {
    	// invalid move message
    	String warning = "Not a legal move!!!\n";
    	
    	// get last hand on table
    	Hand table_hand;
    	if (this.handOnTable.size() > 0 ) {
    		table_hand = this.handOnTable.get(this.handOnTable.size()-1);
    	}
    	else {
    		table_hand = null;
    	}
	    	
    	// player index to player
    	CardGamePlayer player = this.playerList.get(playerIdx);
    	
    	
		 
    	// pass
    	if (cardIdx == null) {
    		// first player or player of last hand --> canNOT pass
    		if ((this.getHandsOnTable().size() == 0) || (table_hand.getPlayer() == player)) {
    			this.ui.invalidMove();
    			this.ui.repaint();
    			return;
    		}
    		else {
	    		this.ui.printMsg("{Pass}\n");
				this.currentPlayerIdx = (this.currentPlayerIdx + 1) % this.numOfPlayers;
				this.ui.disable();
				this.ui.setActivePlayer(this.currentPlayerIdx);
				this.ui.repaint();
				this.ui.promptActivePlayer();
				return;
    		}
    	}
    	else {
    		// list of card indices to cardlist
			CardList player_card = new CardList();
			for (int i = 0; i < cardIdx.length; i++) {
				player_card.addCard(this.playerList.get(playerIdx).getCardsInHand().getCard(cardIdx[i]));
	    	}
			
			// transform cardlist to a specific type of hand
			Hand player_hand = composeHand(player, player_card);
			
			// invalid card combination
			if (player_hand == null) {
				this.ui.invalidMove();
				this.ui.repaint();
				return;
			}
			// valid card combination
			else {
				// fist hand
				if ((this.getHandsOnTable().size() == 0)) {
					// check if first hand includes the Three of Diamonds
					if (player_hand.contains((new BigTwoCard(0, 2)))) {
						player.removeCards(player_hand);
						this.ui.printMsg(String.format("{%1$s} %2$s\n", player_hand.getType(), player_hand.toString()));
						this.handOnTable.add(player_hand);
						this.currentPlayerIdx = (this.currentPlayerIdx + 1) % this.numOfPlayers;
						this.ui.disable();
						this.ui.setActivePlayer(this.currentPlayerIdx);
						this.ui.repaint();
						this.ui.promptActivePlayer();
						return;
					}
					else {
						this.ui.invalidMove();
						this.ui.repaint();
		    			return;
					}
					
				}
				// other rounds
				else {
					// all other player pass and loop back 
					if ((table_hand.getPlayer() == player)) {
						player.removeCards(player_hand);
						this.handOnTable.add(player_hand);
						this.ui.printMsg(String.format("{%1$s} %2$s\n", player_hand.getType(), player_hand.toString()));
						// end of game
						if (this.endOfGame()) {
				        	this.ui.repaint();
				        	this.ui.endGame();
				    		return;
				    	}
						// game continues
						this.currentPlayerIdx = (this.currentPlayerIdx + 1) % this.numOfPlayers;
						this.ui.disable();
						this.ui.setActivePlayer(this.currentPlayerIdx);
						this.ui.repaint();
						this.ui.promptActivePlayer();
						return;
					}
					// invalid due to different num of cards from the hand on table
					else if (table_hand.size() != player_hand.size()) {
						this.ui.invalidMove();
						this.ui.repaint();
						return;
					}
					// same num of cards as the hand on table
					else {
						// player's hand beats the hand on table
						if (player_hand.beats(table_hand)) {
							player.removeCards(player_hand);
							this.handOnTable.add(player_hand);
							this.ui.printMsg(String.format("{%1$s} %2$s\n", player_hand.getType(), player_hand.toString()));
							// end of game
							if (this.endOfGame()) {
					        	this.ui.repaint();
					        	this.ui.endGame();
					    		return;
					    	}
							// game continues
							
							this.currentPlayerIdx = (this.currentPlayerIdx + 1) % this.numOfPlayers;
							this.ui.disable();
							this.ui.setActivePlayer(this.currentPlayerIdx);
							this.ui.repaint();
							this.ui.promptActivePlayer();
							return;
						}
						// player's hand does NOT beats the hand on table
						else {
							this.ui.invalidMove();
							this.ui.repaint();
							return;
						}
						
					}
				}
			}

    	}
   	
    }
    
    /**
     * Check if the game ends (i.e. any one of the players have no more cards)
     * 
     * @return true if game ends, otherwise false
     */
    public boolean endOfGame() {
    	for (int i = 0; i < this.numOfPlayers; i++) {
    		if (this.playerList.get(i).getNumOfCards() == 0) {
    			return true;
    		}
    	}
    	return false;
    }
    
    
    /**
     * Start the BigTwo game 
     * 
     * @param args not used
     */
    public static void main(String[] args) {
    	BigTwo game = new BigTwo();
    	
    	// input user name
    	game.getClient().setPlayerName(game.getGUI().inputName());
    	
    	// make a connection to server
    	game.getClient().connect();
    	
    	// set the client in the gui as this client
    	game.getGUI().setClient(game.getClient());
    }

}