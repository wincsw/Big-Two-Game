import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;



/**
 *  This class is used for modeling a GUI (graphic user interface) for the Big Two card game.
 * 
 * @author Chan Sze Wing
 *
 */
public class BigTwoGUI implements CardGameUI{
	private BigTwo game;
	private final static int MAX_CARD_NUM = 13; // max. no. of cards each player holds
	private boolean [] selected = new boolean[MAX_CARD_NUM]; // selected cards
	private int activePlayer = -1; // index of active player
	private JFrame frame; // main window
	private JPanel bigTwoPanel;	// main panel (different from requirement -- including chat and msg)
	private JButton playButton, passButton; // play and pass button
	private JTextArea msgArea, chatArea; // text area for current game status and chat message
	private JTextField chatInput; // text field for chat message input
	// self defined 
	private JMenuBar menuBar; // menu bar holding menu 
	private JMenu gameMenu; // menu holding menu items
	private JMenuItem quitItem, connectItem; // quit and connect menu item
	private ArrayList<Hand> handsOnTable; // list of hand played
	private ArrayList<CardGamePlayer> playerList; // list of player
	private ArrayList<PlayerPanel> playerPanels; // whether card selection are enabled 
	private JScrollPane msgPane, chatPane; // scroll panes for msgArea and chatArea
	private boolean active; // determine can the card be selected
	private BigTwoClient client; // client for this gui
	private JDialog endDialog; // dialog box for game end
	
	/**
	 * Creates and returns an instance of a BigTwoUI class.
	 * 
	 * @param game the BigTwo game object associated with this GUI
	 */
	public BigTwoGUI(BigTwo game) {
		this.game = game;
		handsOnTable = game.getHandsOnTable();
		playerList = game.getPlayerList();
		
		frame = new JFrame("Big Two");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		msgArea = new JTextArea();
		msgArea.setColumns(40);
		msgArea.setRows(25);
		msgArea.setEditable(false);
		msgArea.setLineWrap(true);
		msgArea.setWrapStyleWord(true);
		
		chatArea = new JTextArea();
		chatArea.setColumns(40);
		chatArea.setRows(25);
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		chatArea.setWrapStyleWord(true);
		
		client = this.game.getClient();
		
		chatInput = new JTextField();
		
		
		playButton = new JButton("Play");
		playButton.addActionListener(new PlayButtonItemListener());
		
		passButton = new JButton("Pass");
		passButton.addActionListener(new PassButtonItemListener());
		
		menuBar = new JMenuBar();
		gameMenu = new JMenu("Game");
		quitItem = new JMenuItem("Quit");
		connectItem = new JMenuItem("Connect");
		
		quitItem.addActionListener(new QuitMenuItemListener());
		connectItem.addActionListener(new ConnectMenuItemListener());
		
		gameMenu.add(quitItem);
		gameMenu.add(connectItem);
		
		menuBar.add(gameMenu);
		
		frame.setJMenuBar(menuBar);
		
		reset();
		
		
		// dummy bigTwoPanel to set minimum window size
		bigTwoPanel = new BigTwoPanel();
		frame.add(bigTwoPanel);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	// method from CardGameUI interface (javadocs is copied from CardGameUI.java)
	
	/**
	 * Sets the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer an int value representing the index of the active player
	 */
	public void setActivePlayer(int activePlayer) {
		if (activePlayer < 0 || activePlayer >= playerList.size()) {
			this.activePlayer = -1;
		} else {
			this.activePlayer = activePlayer;
		}
	}
	
	/**
	 * Construct all PlayerPanels
	 */
	public void constructPlayerPanels() {
		handsOnTable = game.getHandsOnTable();
		playerList = game.getPlayerList();
		playerPanels = new ArrayList<PlayerPanel>(this.game.getNumOfPlayers());
		for (int i = 0; i < this.game.getNumOfPlayers(); i++) {
			PlayerPanel temp = new PlayerPanel(i);
			playerPanels.add(temp);
		}
	}
	
	/**
	 * Get the array of PlayerPanels
	 * 
	 * @return the array of PlayerPanels
	 */
	public ArrayList<PlayerPanel> getPlayerPanels() {
		return this.playerPanels;
	}
	
	/**
	 * Repaints the user interface.
	 */
	public void repaint() {
		handsOnTable = game.getHandsOnTable();
		playerList = game.getPlayerList();
		frame.remove(bigTwoPanel);
		bigTwoPanel = new BigTwoPanel();
		frame.add(bigTwoPanel);
		frame.pack();
		frame.repaint();
	}
	
	/**
	 * Prints the specified string to the message area of the card game user
	 * interface.
	 * 
	 * @param msg the string to be printed to the message area of the card game user
	 *            interface
	 */
	public void printMsg(String msg) {
		msgArea.append(msg);
		frame.repaint();
		msgArea.setCaretPosition(msgArea.getText().length());
	}
	
	/**
	 * Prints the chat message string to the chat area of the card game user
	 * interface.
	 * 
	 * @param msg the chat message 
	 */
	public void printChat(String msg) {
		if (msg.length() != 0) 
			chatArea.append(msg + "\n");
		frame.repaint();
		chatArea.setCaretPosition(chatArea.getText().length());
	}
	
	/**
	 * Clears the message area of the card game user interface.
	 */
	public void clearMsgArea() {
		this.msgArea.setText("");
		frame.repaint();
	}
	
	/**
	 * Resets the card game user interface.
	 */
	public void reset() {
		clearMsgArea();
		selected = new boolean[MAX_CARD_NUM];
	}
	
	/**
	 * Enables user interactions.
	 */
	public void enable() {
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		active = true;
		constructPlayerPanels();
	}
	
	/**
	 * Disables user interactions.
	 */
	public void disable() {
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		active = false;
		constructPlayerPanels();
	}
	
	/**
	 * Returns an array of indices of the cards selected through the UI.
	 * 
	 * @return an array of indices of the cards selected, or null if no valid cards
	 *         have been selected
	 */
	public int[] getSelected() {
		int[] cardIdx = null;
		int count = 0;
		for (int j = 0; j < selected.length; j++) {
			if (selected[j]) {
				count++;
			}
		}

		if (count != 0) {
			cardIdx = new int[count];
			count = 0;
			for (int j = 0; j < selected.length; j++) {
				if (selected[j]) {
					cardIdx[count] = j;
					count++;
				}
			}
		}
		return cardIdx;
	}
	
	/**
	 * Reset card selection
	 */
	public void resetSelected() {
		selected = new boolean[MAX_CARD_NUM];
	}
	
	
	/**
	 * Prompts active player to select cards and make their move.
	 */
	public void promptActivePlayer() {
		printMsg(playerList.get(activePlayer).getName() + "'s turn: \n");
		
	}
	
	/**
	 * Show end of game message and pop-up window.
	 */
	public void endGame() {
		
		String endMsg = "";
		
		endMsg += "Game ends\n";
		
		for (int i = 0; i < game.getNumOfPlayers(); i++) {
    		if (game.getPlayerList().get(i).getNumOfCards() == 0) {
    			endMsg += String.format("%s wins the game. \n", game.getPlayerList().get(i).getName());
    		}
    		else {
    			endMsg += String.format("%1$s has %2$d cards in hand.\n", game.getPlayerList().get(i).getName(), game.getPlayerList().get(i).getNumOfCards());
    		}
    		
    	}
				
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new OKButtonItemListener());
		
		Object[] options = {okButton};
		
		JOptionPane endPane = new JOptionPane(endMsg, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_OPTION, null, options, options[0]);
		endDialog = endPane.createDialog(frame, "Game ends");
		endDialog.setVisible(true);
		
	}
	
	/**
	 * Show the pop-up window that allow player input their name
	 * 
	 * @return the inputed name (default as "Player")
	 */
	public String inputName() {
		String s = (String)JOptionPane.showInputDialog(
		                    frame,
		                    "Player name:",
		                    "Player Name",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null, null, null);

		if ((s != null) && (s.length() > 0)) {
		    return s;
		}
		return "Player";
	}
	
	/**
	 * Show ileagal move warning window
	 */
	public void invalidMove() {
		if (active) {
			JOptionPane.showMessageDialog(frame,
				    "No a legal move!!!",
				    "Ileagal move",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Set the client associated to this gui
	 * 
	 * @param client the client associated to this gui
	 */
	public void setClient(BigTwoClient client) {
		this.client = client;
	}
	
	

	/**
	 * This class of ActionListener is used to register with restartItem.
	 * 
	 * @author win
	 *
	 */
	class ConnectMenuItemListener implements ActionListener {

		// Restart the game.
		public void actionPerformed(ActionEvent e) {
			client.connect();
			
		}
		
	}
	
	/**
	 * This class of ActionListener is used to register with quitItem.
	 * 
	 * @author win
	 *
	 */
	class QuitMenuItemListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			
			System.exit(0);
		}
		
	}
	
	
	/**
	 * This class of ActionListener is used to register with playButton.
	 * 
	 * @author win
	 *
	 */
	class PlayButtonItemListener implements ActionListener {

		/**
		 * Allow player to play their cards (call makeMove())
		 */
		public void actionPerformed(ActionEvent e) {
			int[] cardIdx = getSelected();
			
			// reset the selected cards
			resetSelected();
			
			
			// no card is selected
			if (cardIdx == null) {
				invalidMove();
				return;
			}
			game.makeMove(activePlayer, cardIdx);
			
			
		}
		
	}
	
	/**
	 * This class of ActionListener is used to register with passButton.
	 * 
	 * @author Chan Sze Wing
	 *
	 */
	class PassButtonItemListener implements ActionListener {

		/**
		 * Pass this round.
		 */
		public void actionPerformed(ActionEvent e) {
			resetSelected();
			game.makeMove(activePlayer, null);
			
		}
		
	}
	
	
	/**
	 * This class of KeyListener is used to register with passButton.
	 * 
	 * @author Chan Sze Wing
	 *
	 */
	class ChatInputListener implements KeyListener {
		/**
		 * Not used.
		 */
		public void keyPressed (KeyEvent e) {
			
		}
		
		/**
		 * Not used.
		 */
		public void keyReleased (KeyEvent e) {}
		
		/**
		 * Display inputed text to chatArea when "ENTER" key is typed.
		 */
		public void keyTyped (KeyEvent e) {
			if (e.getKeyChar() == '\n') {
				String msg = chatInput.getText();
				if (msg.length() > 0) {
					CardGameMessage message = new CardGameMessage(CardGameMessage.MSG, -1, msg);
					client.sendMessage(message);
				}
				chatInput.setText("");
			}
		}
	}
	
	
	/**
	 * This class of ActionListener is used to register with okButton
	 * in the end game pop-up window.
	 * 
	 * @author Chan Sze Wing
	 *
	 */
	class OKButtonItemListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			CardGameMessage message = new CardGameMessage(CardGameMessage.READY, -1, null);
			client.sendMessage(message);
			endDialog.dispose();
		}
		
	}
	

	/**
	 * This class of JPanel is used for holding all the components (except frame and menu).
	 * 
	 * @author Chan Sze Wing
	 *
	 */
	class BigTwoPanel extends JPanel {
		
		// Creates and return an instance of the main panel for bigTwoPanel
		public BigTwoPanel() {
			this.setLayout(new GridBagLayout());
			this.construct();
		}
		
		// add all the component to the BigTwoPanel 
		public void construct() {
			this.removeAll();
			GridBagConstraints c = new GridBagConstraints();
			
			// add player's PlayerPanel
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1.0;
			c.weighty = 1.0;
			c.anchor = GridBagConstraints.WEST;
			c.insets = new Insets(5, 5, 0, 0);
			c.gridheight = 1;
			
			if(playerPanels != null) {
				this.add(playerPanels.get(0), c);
				c.gridy = 1;
				this.add(playerPanels.get(1), c);
				c.gridy = 2;
				this.add(playerPanels.get(2), c);
				c.gridy = 3;
				this.add(playerPanels.get(3), c);
				c.gridy = 4;
			}
			
			// add LastHandPanel
			c.insets = new Insets(5, 5, 5, 0);
			this.add(new LastHandPanel(), c);
			
			// add buttons
			c.gridy = 5;
			c.anchor = GridBagConstraints.CENTER;
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(playButton);
			buttonPanel.add(passButton);
			this.add(buttonPanel, c);
			
			// add TextPanel
			if (client != null)
				chatInput.addKeyListener(new ChatInputListener());
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 6;
			c.insets = new Insets(0, 5, 5, 5);
			this.add(new TextPanel(), c);
		}
	}

	
	/**
	 * This class of JPanel is used for holding all text-related components (i.e. msgArea, chatArea, chatInput).
	 * 
	 * @author Chan Sze Wing
	 *
	 */
	class TextPanel extends JPanel {
		
		
		// Creates and return an instance of TextPanel
		public TextPanel() {
			this.setLayout(new GridBagLayout());
			this.setSize(300, 500);
			
			
			chatPane = new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			chatPane.getVerticalScrollBar().setValue(chatPane.getVerticalScrollBar().getMaximum());
			chatArea.setCaretPosition(chatArea.getText().length());
			
			
			msgPane = new JScrollPane(msgArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			msgPane.getVerticalScrollBar().setValue(msgPane.getVerticalScrollBar().getMaximum());
			msgArea.setCaretPosition(msgArea.getText().length());
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridwidth = 3; 
			c.gridheight = 3; 
			c.weightx = 1; 
			c.weighty = 1; 
			c.fill = GridBagConstraints.BOTH;
			this.add(msgPane, c);
			
			c.insets = new Insets(10, 0, 0, 0);
			c.gridy = 4;
			this.add(chatPane, c);
	        
	        c.weightx = 0.0; 
			c.weighty = 0.0; 
	        c.gridy = 8;
	        c.gridheight = 1;
	        c.gridwidth = 1;
	        c.insets = new Insets(5, 5, 5, 0);
	        this.add(new JLabel("Message: "), c);
	        
	        c.gridx = 1;
	        c.gridwidth = 2; 
	        c.weightx = 0.8; 
	        c.insets = new Insets(5, 0, 5, 5);
	        this.add(chatInput, c);
		}
	}

	
	/**
	 * This class of JPanel is used for displaying each player's cards.
	 * 
	 * 
	 * @author Chan Sze Wing
	 *
	 */
	public class PlayerPanel extends JPanel{
		private int playerIdx; // index of player
		private CardGamePlayer player; // player
		
	
		/**
		 * Creates and return the player's PlayerPanel
		 * 
		 * @param playerIdx	the index of player
		 * @param you	whether the player are allow to select and show the face of their cards
		 */
		public PlayerPanel(int playerIdx) {
			this.playerIdx = playerIdx;
			this.player = playerList.get(playerIdx);
			contruct();
		}
		

		/**
		 * Construct the layout of a playerPanel
		 */
		private void contruct() {
			
			this.removeAll();
			this.setBackground(new Color(0, 0, 0));
			
			this.setLayout(new BorderLayout());
			JPanel temp = new JPanel();
			temp.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			
			if (activePlayer == playerIdx) {
				this.setBackground(new Color(66, 125, 96));
				temp.setBackground(new Color(66, 125, 96));
			}
			else {
				this.setBackground(new Color(42, 79, 61));
				temp.setBackground(new Color(42, 79, 61));
			}
			
			if ((game.getPlayerList().get(playerIdx).getName() != "") && (game.getPlayerList().get(playerIdx).getName() != null)) {
				JLabel label;
				
				
				if (playerIdx == client.getPlayerID()) {
					label = new JLabel("You: ");
					label.setForeground(Color.WHITE);
				}
				else {
					label = new JLabel(playerList.get(playerIdx).getName() + ": ");
					label.setForeground(Color.BLACK);
				}
				
				c.insets = new Insets(5, 5, 0, 0); 
				
				temp.add(label, c); 
				
				c.gridy = 1;
				c.gridwidth = 5; 
				c.gridheight = 2; 
				c.insets = new Insets(-40, 0, 50, 120); 
				AvatarPanel avatar = new AvatarPanel();
				temp.add(avatar, c);
			}
			
			
			c.gridx = 6;
			c.gridheight = 2; 
			c.insets = new Insets(0, 0, 0, 0); 
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.BOTH;
			
			CardPane cardPane = new CardPane(playerIdx == client.getPlayerID(), this.player.getCardsInHand(), active);
			cardPane.setPreferredSize(new Dimension(500, 130));
			temp.add(cardPane, c);
			
			
			this.add(temp, BorderLayout.WEST);
			
			
			
			
		}
		
		
		
		// This class of JPanel contain the avatar of the player
		class AvatarPanel extends JPanel {
			
			/**
			 * Draw the image of the player's avatar.
			 */
			public void paintComponent(Graphics g) {
				this.setSize(130, 130);
				Image image = new ImageIcon("Avatar/Player" + playerIdx + ".png").getImage();
				g.drawImage(image, 1, 1, this);
			}
		}
		
	}

	/**
	 * This class of JPanel shows the last hand on table
	 * 
	 * @author Chan Sze Wing
	 *
	 */
	class LastHandPanel extends JPanel{
		private Hand lastHand; // last hand on table
		
		/**
		 * Creates and returns the LastHandPanel.
		 */
		public LastHandPanel() {
			this.lastHand = (handsOnTable.isEmpty()) ? null : handsOnTable.get(handsOnTable.size() - 1);;
			this.setBackground(new Color(133, 94, 66));
			this.setPreferredSize(new Dimension(500, 150));
			
			if ((this.lastHand != null) && (this.lastHand.getPlayer() != playerList.get(activePlayer))) {
				this.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				
				c.fill = GridBagConstraints.BOTH;
				c.weightx = 1.0;
				c.weighty = 1.0;
				c.anchor = GridBagConstraints.WEST;
				c.insets = new Insets(3, 5, 0, 0);
				
				JLabel label = new JLabel("Played by " + lastHand.getPlayer().getName());
				label.setForeground(Color.BLACK);
				this.add(label, c);
				
				c.gridy = 1;
				c.insets = new Insets(0, 0, 0, 0);
				CardPane cardPane = new CardPane(true, this.lastHand, false);
				cardPane.setPreferredSize(new Dimension(350, 130));
				
				
				this.add(cardPane, c);
			}		
		}
	}
		

	/**
	 * This class of JLayeredPane place the card in an overlapping manner.
	 * 
	 * @author Chan Sze Wing
	 *
	 */
	class CardPane extends JLayeredPane {
		private CardList cards; // list of cards
		private int xLoc = 10, yLoc = 20, width = 76, height = 101; // parameter of the boundaries
		private boolean active;
		
		public CardPane(boolean face, CardList cards, boolean active) {
			this.cards = cards;
			this.active = active;
			
			// when it is the player's turn
			if (face) {
				placeCard();
			}// when it is NOT the player's turn
			else  {
				backCard();
			}
			
		}
		
		/**
		 * Place the cards face up.
		 * 
		 */
		public void placeCard() {
			for (int i = 0; i < cards.size(); i++) {
				CardPanel cardPanel = new CardPanel(cards.getCard(i));
				cardPanel.idx = i;
				cardPanel.c_xLoc = xLoc;
				cardPanel.c_yLoc = yLoc;
				if (active) 
					cardPanel.addMouseListener(cardPanel);
				cardPanel.setBounds(cardPanel.c_xLoc, cardPanel.c_yLoc, width, height);
				
				this.add(cardPanel, new Integer(i));
				xLoc += 30;
			}
		}
		
		/**
		 * Place the cards face down.
		 */
		public void backCard() {
			for (int i = 0; i < cards.size(); i++) {
				BackCardPanel cardPanel = new BackCardPanel();
				
				cardPanel.setBounds(xLoc, yLoc, width, height);
				
				this.add(cardPanel, new Integer(i));
				xLoc += 30;
			}
		}
		
		/**
		 * This class of JPanel contains the back of the card.
		 * 
		 * @author Chan Sze Wing
		 *
		 */
		class BackCardPanel extends JPanel {
			/**
			 * Draw the back of the card.
			 */
			public void paintComponent(Graphics g) {
				Image image = new ImageIcon("Card_Image/b.gif").getImage();
				g.drawImage(image, 3, 4, this);
			}
		}
		
		/**
		 * This class of JPanel contains the face of the card.
		 * 
		 * @author Chan Sze Wing
		 *
		 */
		class CardPanel extends JPanel implements MouseListener {
			private Card card; // the card
			private String cardName = ""; // the card's image name
			private int idx; // index of the card
			private int c_xLoc, c_yLoc; 
			
			
			/**
			 * Creates and return an instance of CardPanel.
			 * 
			 * @param card the card required to draw.
			 */
			public CardPanel(Card card) {
				this.card = card;
				
				if (card.getRank() == 0)
					cardName += "a";
				else if (card.getRank() == 9) 
					cardName += "t";
				else if (card.getRank() == 10)
					cardName += "j";
				else if (card.getRank() == 11)
					cardName += "q";
				else if (card.getRank() == 12)
					cardName += "k";
				else 
					cardName += card.getRank() + 1;
				
				
				if (card.getSuit() == 0)
					cardName += "d";
				else if (card.getSuit() == 1) 
					cardName += "c";
				else if (card.getSuit() == 2)
					cardName += "h";
				else 
					cardName += "s";
				
				for (int i = 0; i < cards.size(); i++) {
					if ((card.getSuit() == cards.getCard(i).getSuit()) && (card.getRank() == cards.getCard(i).getRank())) {
						this.idx = i;
					}
				}
			}
			
			/**
			 * Draw the card.
			 */
			public void paintComponent(Graphics g) {
				Image image = new ImageIcon("Card_Image/" + cardName + ".gif").getImage();
				g.drawImage(image, 3, 4, this);
			}
			

			// fix (https://moodle.hku.hk/mod/forum/discuss.php?d=672683)
			/**
			 * Rise the card when the mouse clicks it.
			 */
			public void mouseClicked(MouseEvent e) {
				if (selected[idx]) {
					selected[idx] = false;
					c_yLoc = yLoc;
				}
				else {
					selected[idx] = true;
					c_yLoc = yLoc - 20;
				}
				
				this.setBounds(c_xLoc, c_yLoc, width, height);
				frame.repaint();
			}
	
			/**
			 * Not used.
			 */
			public void mouseEntered(MouseEvent e) {}
			
			/**
			 * Not used.
			 */
			public void mouseExited(MouseEvent e) {}
	
			/**
			 * Not used.
			 */
			public void mousePressed(MouseEvent e) {}
	
			/**
			 * Not used.
			 */
			public void mouseReleased(MouseEvent e) {}
		}
	}
	
	
	
}