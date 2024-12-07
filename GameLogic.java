package main;

//GameLogic.java   
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

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
	private BufferedImage doorImage;
	private boolean gameStarted = false;
	private boolean gamePaused = false;
	private Room room1, room2, room3, room4, room5, room6, room7;
	private Door door1, door2, door3, door4, door5, door6, door7, door8, door9, door10, door11, door12; // this is
																										// horrendous
																										// coding
																										// structure
	private PauseMenu pauseMenu;
	private boolean canUseDoor = true; // Cooldown flag
	private Timer doorCooldownTimer; // Cooldown timer

	public GameLogic() {

		// Initialize pause menu
		pauseMenu = new PauseMenu(this);

		// Initialize rooms and doors
		room1 = new Room(1, getBackgroundImage(), 1920, 1080);
		room2 = new Room(2, getBackgroundImage(), 1920, 1080);
		room3 = new Room(3, getBackgroundImage(), 1920, 1080);
		room4 = new Room(4, getBackgroundImage(), 1920, 1080);
		room5 = new Room(5, getBackgroundImage(), 1920, 1080);
		room6 = new Room(6, getBackgroundImage(), 1920, 1080);
		room7 = new Room(7, getBackgroundImage(), 1920, 1080);

		// Initialize doors
		door1 = new Door(1, room1, room5, 860, -40, 50, 50); // Room 1 to Room 5 (down)
		door2 = new Door(2, room1, room3, 860, 980, 50, 50); // Room 1 to Room 3 (up)
		door3 = new Door(3, room1, room2, 1820, 470, 50, 50); // Room 1 to Room 2 (right)
		door4 = new Door(4, room1, room4, -40, 470, 50, 50); // Room 1 to Room 4 (left)

		door5 = new Door(5, room2, room1, -40, 470, 50, 50); // Room 2 to Room 1 (left)
		door6 = new Door(6, room3, room1, 860, -40, 50, 50); // Room 3 to Room 1 (down)
		door7 = new Door(7, room4, room1, 1820, 470, 50, 50); // Room 4 to Room 1 (right)

		door8 = new Door(8, room5, room1, 860, 980, 50, 50); // Room 5 to Room 1 (up)
		door9 = new Door(9, room5, room6, 860, -40, 50, 50); // Room 5 to Room 6 (down)

		door10 = new Door(10, room6, room5, 860, 980, 50, 50); // Room 6 to Room 5 (up)
		door11 = new Door(11, room6, room7, 860, -40, 50, 50); // Room 6 to Room 7 (down)

		door12 = new Door(12, room7, room6, 860, 980, 50, 50); // Room 7 to Room 6 (up)

		// Add doors to rooms
		room1.addDoor(door1);
		room1.addDoor(door2); // no
		room1.addDoor(door3);
		room1.addDoor(door4);

		room2.addDoor(door5);
		room3.addDoor(door6);
		room4.addDoor(door7);

		room5.addDoor(door8);
		room5.addDoor(door9);

		room6.addDoor(door10); // no
		room6.addDoor(door11); // no

		// BOSS ROOM (we don't want them to be able to leave, because we are just going to end game here
		//room7.addDoor(door12);

		// Initialize player
		player = new Player(960, 540, this);
		player.setGameLogic(this);

		// Set current room
		currentRoom = room1;

		// Start game loop
		gameLoop = new Timer(16, this);
		gameLoop.start();
		
		List<Enemy> enemiesInRoom5 = room5.getEnemies();
		List<Enemy> enemiesInRoom6 = room6.getEnemies();

		// Add key listener for player movement
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				player.keyPressed(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				player.keyReleased(e);
			}
		});

		// Initialize the door cooldown timer
		doorCooldownTimer = new Timer(1500, new ActionListener() { // 1.5 seconds
			@Override
			public void actionPerformed(ActionEvent e) {
				canUseDoor = true; // Re-enable door usage
				doorCooldownTimer.stop(); // Stop the timer
			}
		});

		setFocusable(true); // Ensure the panel can receive key events
	}

	private BufferedImage getBackgroundImage() {
		try {
			backgroundImage = ImageIO.read(getClass().getResourceAsStream("/images/background.png"));
		} catch (IOException e) {
			System.out.println("Error loading background image: " + e.getMessage());
		}
		return backgroundImage;
	}

	private BufferedImage getDoorImage() {
		try {
			doorImage = ImageIO.read(getClass().getResourceAsStream("/images/doorclosed.png"));
		} catch (IOException e) {
			System.out.println("Error loading door image: " + e.getMessage());
		}
		return doorImage;
	}

    public void updateGame() {
        // Update player
        player.update();
        // Check for player-enemy collisions
        for (Enemy enemy : currentRoom.getEnemies()) {
            // Check if player collides with the enemy
            if (player.getBounds().intersects(enemy.getBounds())) {
                // Handle collision (e.g., player takes damage)
                player.takeDamage(); // Implement this method in the Player class
            }
            // Update enemy
            enemy.update();
        }
        
        
        // Check for door collisions only if cooldown allows
        if (canUseDoor) {
            for (Door door : currentRoom.getDoors()) {
                if (player.getBounds().intersects(door.getBounds())) {
                    // Transition to the next room
                    transitionToNextRoom(door);

                    // Play door open sound
                    try {
                        AudioInputStream audioInputStream = AudioSystem
                                .getAudioInputStream(getClass().getResource("/sfx/dooropen.wav"));
                        Clip doorOpenSound = AudioSystem.getClip();
                        doorOpenSound.open(audioInputStream);
                        doorOpenSound.start();
                    } catch (Exception e) {
                        System.out.println("Error playing door open sound: " + e.getMessage());
                    }

                    // Disable door usage and start cooldown
                    canUseDoor = false;
                    doorCooldownTimer.start();
                    break; // Prevent multiple door activations
                }
            }
        }
	}

	public void renderGame(Graphics g) {
		// Render current room
		g.drawImage(backgroundImage, 0, 0, null);
		// Render any objects or enemies in the room
		// Render items in the room
		// for (Item item : currentRoom.getItems()) {
		// item.render(g);
		// }
		// Render doors
		for (Door door : currentRoom.getDoors()) {
			BufferedImage doorImage = getDoorImage(); // Call getDoorImage() to get the door image
			g.drawImage(doorImage, door.getX(), door.getY(), doorImage.getWidth(), doorImage.getHeight(), null);
		}
		// Render enemies in the room
		for (Enemy enemy : currentRoom.getEnemies()) {
			enemy.render(g);
		}
		// Render player
		player.render(g);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!gamePaused) {
			renderGame(g);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!gamePaused) {
			updateGame();
		}
		updateGame();
		repaint();
	}

	private void transitionToNextRoom(Door door) {
	    // Update the current room
	    currentRoom = (door.getRoom1() == currentRoom) ? door.getRoom2() : door.getRoom1();

	    // Determine the new position based on the door the player exited (talk about torture...)
	    if (door == door2 || door == door8 || door == door10 || door == door12) { // and door12
	        // Player exited through a south door -> Enter the new room from the north
	        player.setPosition(860, 50); // Fixed coordinates near the top of the new room
	    } else if (door == door1 || door == door6 || door == door9 || door == door11) { // or door11
	        // Player exited through a north door -> Enter the new room from the south
	        player.setPosition(860, 950); // Fixed coordinates near the bottom of the new room
	    } else if (door == door3 || door == door7) {
	        // Player exited through an east door -> Enter the new room from the west
	        player.setPosition(50, 470); // Fixed coordinates near the left of the new room
	    } else if (door == door5 ||door == door4) {
	        // Player exited through a west door -> Enter the new room from the east
	        player.setPosition(1750, 470); // Fixed coordinates near the right of the new room
	    }
	}



	public void startNewGame() {
		gameStarted = true;
		gamePaused = false;
		// Initialize game state, such as player position and room
		player = new Player(900, 500, this);
		System.out.println("DEBUG: Player position at start of game: " + player.getX() + ", " + player.getY());
		currentRoom = room1;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void pauseGame() {
		gamePaused = true;
		// Add the pause menu to the game panel
		add(pauseMenu);
		revalidate();
		repaint();
	}

	public void resumeGame() {
		gamePaused = false;
		// Remove the pause menu from the game panel
		remove(pauseMenu);
		revalidate();
		repaint();
	}

	public void getCurrentRoom(Room x) {
		currentRoom = x;
	}

}
