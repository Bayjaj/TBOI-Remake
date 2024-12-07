package main;

//Tear.java   
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Rectangle;

public class Tear {
	private int x, y;
	private int velocityX, velocityY;
	private BufferedImage image;
	private int distanceTraveled;

	public Tear(int x, int y, int velocityX, int velocityY) {
		this.x = x;
		this.y = y;
		this.velocityX = velocityX * 2;
		this.velocityY = velocityY * 2;
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/images/tear.png"));
		} catch (IOException e) {
			System.out.println("Error loading tear image: " + e.getMessage());
		}
		distanceTraveled = 0;
	}

	public void update() {
		x += velocityX;
		y += velocityY;
		distanceTraveled += Math.abs(velocityX) + Math.abs(velocityY);
	}

	public void render(Graphics g) {
		g.drawImage(image, x, y, null);
	}

	public boolean isDead() {
		return distanceTraveled >= 500;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, image.getWidth(), image.getHeight());
	}
}
