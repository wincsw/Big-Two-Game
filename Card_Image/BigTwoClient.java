import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 * This class is used for modeling model a Big Two game client that is 
 * responsible for establishing a connection and communicating with
 * the Big Two game server.
 * 
 * @author Chan Sze Wing
 *
 */
public class BigTwoClient implements NetworkGame {
	private BigTwo game;
	private BigTwoGUI gui;
	private Socket sock; // socket connection
	private ObjectOutputStream oos; // for sending message to server
	private int playerID; // player index
	private String playerName;
	// hardrcode IP address and TCP port
	private String serverIP = "127.0.0.1"; 
	private int serverPort = 2396;
	
	/**
	 * Constructor for creating a Big Two client.
	 * 
	 * @param game
	 * @param gui
	 */
	public BigTwoClient(BigTwo game, BigTwoGUI gui) {
		this.game = game;
		this.gui = gui;
	}
	
	

    /**
	 * Returns the playerID (index) of the local player.
	 * 
	 * @return the playerID (index) of the local player
	 */
	public int getPlayerID() {
		return this.playerID;
	}

	/**
	 * Sets the playerID (index) of the local player.
	 * 
	 * @param playerID
	 *            the playerID (index) of the local player.
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/**
	 * Returns the name of the local player.
	 * 
	 * @return the name of the local player
	 */
	public String getPlayerName() {
		return this.playerName;
	}

	/**
	 * Sets the name of the local player.
	 * 
	 * @param playerName
	 *            the name of the local player
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * Returns the IP address of the server.
	 * 
	 * @return the IP address of the server
	 */
	public String getServerIP() {
		return this.serverIP;
	}

	/**
	 * Sets the IP address of the server.
	 * 
	 * @param serverIP
	 *            the IP address of the server
	 */
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	/**
	 * Returns the TCP port of the server.
	 * 
	 * @return the TCP port of the server
	 */
	public int getServerPort() {
		return this.serverPort;
	}

	/**
	 * Sets the TCP port of the server
	 * 
	 * @param serverPort
	 *            the TCP port of the server
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * Makes a network connection to the server.
	 */
	public void connect() {
		
		try {
			sock = new Socket(serverIP, serverPort);
			oos = new ObjectOutputStream(sock.getOutputStream());
			Thread recieveThread = new Thread(new ServerHandler());
			recieveThread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

	/**
	 * Parses the specified message received from the server.
	 * 
	 * @param message
	 *            the specified message received from the server
	 */
	public void parseMessage(GameMessage message) {
		try {
			// PLAYER_LIST
			if (message.getType() == CardGameMessage.PLAYER_LIST) {
				this.playerID = message.getPlayerID();
				for (int i = 0; i < this.game.getNumOfPlayers(); i++) {
					this.game.getPlayerList().get(i).setName(((String[]) message.getData())[i]);
				}

				sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, this.playerName));
			}
			// JOIN
			else if (message.getType() == CardGameMessage.JOIN) {
				this.game.getPlayerList().get(message.getPlayerID()).setName((String) message.getData());
				if (message.getPlayerID() == this.playerID) {
					sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
				}			
			}
			// FULL
			else if (message.getType() == CardGameMessage.FULL) {
				this.gui.printMsg("Server is full, cannot join game.\n");
			}
			// QUIT
			else if (message.getType() == CardGameMessage.QUIT) {
				this.gui.disable();
				this.gui.printMsg(String.format("Game stop (%s leave game)\n", (String) message.getData()));
				this.game.getPlayerList().get(message.getPlayerID()).setName("");
				this.gui.constructPlayerPanels();
				this.gui.repaint();
				sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			}
			// READY
			else if (message.getType() == CardGameMessage.READY) {
				this.gui.printMsg(String.format("%s is ready.\n", this.game.getPlayerList().get(message.getPlayerID()).getName()));
			}
			// START
			else if (message.getType() == CardGameMessage.START) {
				this.gui.reset();
				this.game.start((Deck) message.getData());
				if (this.game.getCurrentPlayerIdx() == this.playerID) {
					this.gui.enable();
				}
				else {
					this.gui.disable();
				}
				this.gui.repaint();
			}
			// MOVE
			else if (message.getType() == CardGameMessage.MOVE) {
				this.game.checkMove(message.getPlayerID(), (int[]) message.getData());
				if (this.game.getCurrentPlayerIdx() == this.playerID) {
					this.gui.enable();
				}
				else {
					this.gui.disable();
				}
				this.gui.repaint();
			}
			// MSG
			else if (message.getType() == CardGameMessage.MSG) {
				this.gui.printChat((String) message.getData());
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

	/**
	 * Sends the specified message to the server.
	 * 
	 * @param message
	 *            the specified message to be sent the server
	 */
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Receive and handle messages from the game server
	 * 
	 * @author Chan Sze Wing
	 *
	 */
	class ServerHandler implements Runnable {
		public void run() {
			GameMessage message;
			try {
				// input stream for the receive GameMessage object from server
				ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
				while ((message = (GameMessage) ois.readObject()) != null) {
					parseMessage(message);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	

	

}
