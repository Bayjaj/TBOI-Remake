package main;

//Main.java  
import javax.swing.*;

public class Main extends JFrame {
	private GameLogic gameLogic;
	private MainMenu mainMenu;

	public Main() {
		gameLogic = new GameLogic();
		mainMenu = new MainMenu(this);

		// Set full screen mode
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);

		add(mainMenu);
		setSize(1920, 1080);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void showGamePanel() {
		remove(mainMenu);
		add(gameLogic);
		revalidate();
		repaint();
		gameLogic.requestFocusInWindow(); // Request focus for the GameLogic panel
	}

	public GameLogic getGameLogic() {
		return gameLogic;
	}

	public static void main(String[] args) {
		new Main();
	}
}
