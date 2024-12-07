package main;

//Main.java  
import javax.swing.*;

public class Main extends JFrame {
	private GameLogic gameLogic;
	private MainMenu mainMenu;

	public Main() {
		// Create the main menu and game logic (so create everything...)
		gameLogic = new GameLogic();
		mainMenu = new MainMenu(this);

		// Set full screen mode
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);

		// Add the main menu and set it visible
		add(mainMenu);
		setSize(1920, 1080);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	// Function for showing game panel which essentially starts the game and ends the main menu
	public void showGamePanel() {
		remove(mainMenu);
		add(gameLogic);
		revalidate(); // tells the panel that it needs to be updated because we just changed the Jframe stuff
		repaint();
		gameLogic.requestFocusInWindow(); // Request focus for the GameLogic panel 
	}

	// this gets the gameLogic so if other classes need it they have access
	public GameLogic getGameLogic() {
		return gameLogic;
	}

	// This gets called from the splash screen and just starts the main class code
	public static void main(String[] args) {
		new Main();
	}
}
