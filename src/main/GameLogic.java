package main;

//GameLogic.java   
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameLogic extends JPanel implements ActionListener {
	private Room currentRoom;
	private Player player;
	private Timer gameLoop;
	private BufferedImage backgroundImage;
	private boolean gameStarted = false;
	private boolean gamePaused = false;
	private Room room1, room2, room3, room4, room5, room6, room7;

	public GameLogic() {

		// Initialize rooms and doors
		room1 = new Room(1, getBackgroundImage(), 1920, 1080);
		room2 = new Room(2, getBackgroundImage(), 1920, 1080);
		room3 = new Room(3, getBackgroundImage(), 1920, 1080);
		room4 = new Room(4, getBackgroundImage(), 1920, 1080);
		room5 = new Room(5, getBackgroundImage(), 1920, 1080);
		room6 = new Room(6, getBackgroundImage(), 1920, 1080);
		room7 = new Room(7, getBackgroundImage(), 1920, 1080);

		// Initialize doors
		Door door1 = new Door(1, room1, room5, 960, 0, 50, 50);
		Door door2 = new Door(2, room1, room3, 960, 1080, 50, 50);
		Door door3 = new Door(3, room1, room2, 1920, 540, 50, 50);
		Door door4 = new Door(4, room1, room4, 0, 540, 50, 50);
		Door door5 = new Door(5, room5, room6, 960, 1080, 50, 50);
		Door door6 = new Door(6, room6, room7, 960, 1080, 50, 50);
		Door door7 = new Door(7, room2, room1, 0, 540, 50, 50);
		Door door8 = new Door(8, room3, room1, 960, 0, 50, 50);
		Door door9 = new Door(9, room4, room1, 1920, 540, 50, 50);

		// Add doors to rooms
		room1.addDoor(door1);
		room1.addDoor(door2);
		room1.addDoor(door3);
		room1.addDoor(door4);
		room2.addDoor(door7);
		room3.addDoor(door8);
		room4.addDoor(door9);
		room5.addDoor(door5);
		room6.addDoor(door6);

		// Initialize player
		player = new Player(960, 540);

		// Set current room
		currentRoom = room1;

		// Start game loop
		gameLoop = new Timer(16, this);
		gameLoop.start();
	}

	private BufferedImage getBackgroundImage() {
		try {
			backgroundImage = ImageIO.read(getClass().getResourceAsStream("/images/background.png"));
		} catch (IOException e) {
			System.out.println("Error loading background image: " + e.getMessage());
		}
		return backgroundImage;
	}

	public void updateGame() {
		// Update player
		player.update();

		for (Door door : currentRoom.getDoors()) {
			if (player.getBounds().intersects(door.getBounds())) {
				// Transition to the next room
				transitionToNextRoom(door);
				// Play door open audio
				try {
					AudioInputStream audioInputStream = AudioSystem
							.getAudioInputStream(getClass().getResource("/sfx/dooropen.wav"));
					Clip doorOpenSound = AudioSystem.getClip();
					doorOpenSound.open(audioInputStream);
					doorOpenSound.start();
				} catch (Exception e) {
					System.out.println("Error playing door open sound: " + e.getMessage());
				}
			}

			// Update player position
			if (door.getX() == 0) {
				player.setX(1920 - player.getBounds().width);
			} else if (door.getX() == 1920) {
				player.setX(0);
			} else if (door.getY() == 0) {
				player.setY(1080 - player.getBounds().height);
			} else if (door.getY() == 1080) {
				player.setY(0);
			}
		}
	}

	public void renderGame(Graphics g) {
		// Render current room
		g.drawImage(backgroundImage, 0, 0, null);
		// Render player
		player.render(g);
		// Render any objects or enemies in the room
		// Render items in the room
		// for (Item item : currentRoom.getItems()) {
		// item.render(g);
		// }

		// Render enemies in the room
		for (Enemy enemy : currentRoom.getEnemies()) {
			enemy.render(g);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		renderGame(g);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		updateGame();
		repaint();
	}

	private void transitionToNextRoom(Door door) {
		// Update the current room
		currentRoom = door.getRoom2();

		// Update the player's position
		player.setPosition(door.getX(), door.getY());
	}

	public void startNewGame() {
		gameStarted = true;
		gamePaused = false;
		// Initialize game state, such as player position and room
		player = new Player(960, 540);
		   System.out.println("Player position: " + player.getX() + ", " + player.getY());
		currentRoom = room1;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void resumeGame() {
		gamePaused = false;
	}

	public void pauseGame() {
		gamePaused = true;
	}
}
