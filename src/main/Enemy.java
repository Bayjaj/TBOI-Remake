package main;

//Enemy.java  
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.awt.Rectangle;

public class Enemy {
	private int x, y;
	private int velocityX, velocityY; // Movement speeds
	private int speed = 12; // Increased movement speed
	private BufferedImage image;
	private int health;
	private boolean isDead = false; // Flag to track enemy death
	private final int BORDER_OFFSET = 100; // Offset from the screen border

	public Enemy(int x, int y) {
		this.x = x;
		this.y = y;
		this.velocityX = (int) (Math.random() * (speed * 2)) - speed; // Random initial X velocity
		this.velocityY = (int) (Math.random() * (speed * 2)) - speed; // Random initial Y velocity
		try {
			this.image = ImageIO.read(getClass().getResourceAsStream("/images/enemy.png"));
		} catch (IOException e) {
			System.out.println("Error loading enemy image: " + e.getMessage());
		}
		this.health = 3; // Default health value
	}

	public void update() {
		if (!isDead) { // Only update if the enemy is not dead
			x += velocityX;
			y += velocityY;

			// Boundary checking for screen edges with offset
			if (x <= BORDER_OFFSET || x + image.getWidth() >= 1920 - BORDER_OFFSET) {
				velocityX = -velocityX; // Reverse X direction
				if (x < BORDER_OFFSET)
					x = BORDER_OFFSET; // Ensure doesn't go off-screen
				if (x + image.getWidth() > 1920 - BORDER_OFFSET)
					x = 1920 - image.getWidth() - BORDER_OFFSET;
			}
			if (y <= BORDER_OFFSET || y + image.getHeight() >= 1080 - BORDER_OFFSET) {
				velocityY = -velocityY; // Reverse Y direction
				if (y < BORDER_OFFSET)
					y = BORDER_OFFSET;
				if (y + image.getHeight() > 1080 - BORDER_OFFSET)
					y = 1080 - image.getHeight() - BORDER_OFFSET;
			}
		}
	}

	public void render(Graphics g) {
		if (!isDead) { // Only render if the enemy is not dead
			g.drawImage(image, x, y, null);
		}
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, image.getWidth(), image.getHeight());
	}

	public int getHealth() {
		return this.health;
	}

	public void takeDamage() {
		this.health -= 1; // Adjust the damage value as needed
		if (this.health <= 0) {
			this.health = 0; // Ensure health doesn't go below 0
			this.isDead = true; // Set flag to indicate enemy is dead
		}
	}

	public boolean isDead() {
		return this.isDead;
	}
}
