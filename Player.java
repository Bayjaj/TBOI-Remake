package main;

//Player.java  
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Player {
	private int x, y;
	private int velocityX, velocityY;
	private BufferedImage image;
	private boolean isMovingUp, isMovingDown, isMovingLeft, isMovingRight;
	private int playerSpeed = 7; // Default player speed
	private int health = 4; // Default player health
	private BufferedImage heartImage;
	private long lastDamageTime = 0; // Time of last damage
	private long invincibilityDuration = 1200; // 1.2 seconds
	public ArrayList<Tear> tears;
	private long lastTearShotTime = 0;
	private final long tearCooldown = 400; // 0.4 seconds
	private boolean isShooting = false;
	private boolean isShootingUp = false;
	private boolean isShootingDown = false;
	private boolean isShootingLeft = false;
	private boolean isShootingRight = false;
	private boolean isDead = false;
	private GameLogic gameLogic;

	public Player(int x, int y, GameLogic gamelogic) {
		this.x = x;
		this.y = y;
		velocityX = 0;
		velocityY = 0;
		isMovingUp = false;
		isMovingDown = false;
		isMovingLeft = false;
		isMovingRight = false;
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/images/isaac.png"));
			heartImage = ImageIO.read(getClass().getResourceAsStream("/images/heart.png"));
		} catch (IOException e) {
			System.out.println("Error loading player image: " + e.getMessage());
		}
		tears = new ArrayList<>();
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			isMovingUp = true;
			break;
		case KeyEvent.VK_S:
			isMovingDown = true;
			break;
		case KeyEvent.VK_A:
			isMovingLeft = true;
			break;
		case KeyEvent.VK_D:
			isMovingRight = true;
			break;
		case KeyEvent.VK_UP:
			isShootingUp = true;
			isShooting = true;
			break;
		case KeyEvent.VK_DOWN:
			isShootingDown = true;
			isShooting = true;
			break;
		case KeyEvent.VK_LEFT:
			isShootingLeft = true;
			isShooting = true;
			break;
		case KeyEvent.VK_RIGHT:
			isShootingRight = true;
			isShooting = true;
			break;
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameLogic.pauseGame();
		}
		updateVelocity();
	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			isMovingUp = false;
			break;
		case KeyEvent.VK_S:
			isMovingDown = false;
			break;
		case KeyEvent.VK_A:
			isMovingLeft = false;
			break;
		case KeyEvent.VK_D:
			isMovingRight = false;
			break;
		case KeyEvent.VK_UP:
			isShootingUp = false;
			isShooting = false;
			break;
		case KeyEvent.VK_DOWN:
			isShootingDown = false;
			isShooting = false;
			break;
		case KeyEvent.VK_LEFT:
			isShootingLeft = false;
			isShooting = false;
			break;
		case KeyEvent.VK_RIGHT:
			isShootingRight = false;
			isShooting = false;
			break;
		}
		updateVelocity();
	}

	private void updateVelocity() {
		if (isMovingUp) {
			velocityY = -playerSpeed;
		} else if (isMovingDown) {
			velocityY = playerSpeed;
		} else {
			velocityY = 0;
		}

		if (isMovingLeft && isMovingRight) {
			velocityX = 0;
		} else if (isMovingLeft) {
			velocityX = -playerSpeed;
		} else if (isMovingRight) {
			velocityX = playerSpeed;
		} else {
			velocityX = 0;
		}
	}

	public void update() {
		x += velocityX;
		y += velocityY;

		// Update tears
		for (int i = tears.size() - 1; i >= 0; i--) {
			Tear tear = tears.get(i);
			tear.update();
			if (tear.isDead()) {
				tears.remove(i);
			}
		}

		// Shoot tears
		if (isShooting && System.currentTimeMillis() - lastTearShotTime >= tearCooldown) {
			shootTear();
			lastTearShotTime = System.currentTimeMillis();
		}

		// Boundary checking to prevent the player from moving off the screen
		if (x < 0) {
			x = 0;
		} else if (x > 1920 - 150) {
			x = 1920 - 150;
		}
		if (y < 0) {
			y = 0;
		} else if (y > 1080 - 200) {
			y = 1080 - 200;
		}
	}

	public void render(Graphics g) {
		g.drawImage(image, x, y, null);
		for (Tear tear : tears) {
			tear.render(g);
		}
		for (int i = 0; i < health; i++) {
			g.drawImage(heartImage, 10 + i * 60, 10, null);
		}
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, image.getWidth(), image.getHeight());
	}

	public void takeDamage() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastDamageTime >= invincibilityDuration) {
			health--;
			if (health < 0) {
				health = 0;
			}
			lastDamageTime = currentTime;

			// Play hurt sound
			try {
				AudioInputStream audioInputStream = AudioSystem
						.getAudioInputStream(getClass().getResource("/sfx/hurt.wav"));
				Clip hurtSound = AudioSystem.getClip();
				hurtSound.open(audioInputStream);
				hurtSound.start();
			} catch (Exception e) {
				System.out.println("Error playing hurt sound: " + e.getMessage());
			}
		}
	}

	public int getHealth() {
		return health;
	}

	private void shootTear() {
		// Create a new tear
		int tearVelocityX = 0;
		int tearVelocityY = 0;

		if (isShootingUp) {
			tearVelocityY = -10;
		} else if (isShootingDown) {
			tearVelocityY = 10;
		} else if (isShootingLeft) {
			tearVelocityX = -10;
		} else if (isShootingRight) {
			tearVelocityX = 10;
		}

		Tear tear = new Tear(x, y, tearVelocityX, tearVelocityY);
		tears.add(tear);

		// Play tear shoot sound effect
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(getClass().getResource("/sfx/tearshoot.wav"));
			Clip tearShootSound = AudioSystem.getClip();
			tearShootSound.open(audioInputStream);
			tearShootSound.start();
		} catch (Exception e) {
			System.out.println("Error playing tear shoot sound: " + e.getMessage());
		}
	}

	public void removeTear(Tear tear) {
		tears.remove(tear);
	}

	public void checkTearCollisions(Enemy enemy) {
		for (int i = tears.size() - 1; i >= 0; i--) {
			Tear tear = tears.get(i);
			if (tear.getBounds().intersects(enemy.getBounds())) {
				enemy.takeDamage();
				tears.remove(i);
			}
		}
	}

	public void die() {
		// Play player death sound effect
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(getClass().getResource("/sfx/playerdeath.wav"));
			Clip playerDeathSound = AudioSystem.getClip();
			playerDeathSound.open(audioInputStream);
			playerDeathSound.start();
		} catch (Exception e) {
			System.out.println("Error playing player death sound: " + e.getMessage());
		}
		isDead = true;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void reset(Room currentRoom) {
		x = currentRoom.getPlayerStartX();
		y = currentRoom.getPlayerStartY();
	}

	public void setGameLogic(GameLogic gameLogic) {
		this.gameLogic = gameLogic;
	}
}
