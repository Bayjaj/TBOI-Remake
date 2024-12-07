package main;

//PauseMenu.java  
import java.awt.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PauseMenu extends JPanel {
	private GameLogic gameLogic;
	private String[] options = { "Resume Run", "Options", "Exit Game" };
	private int selectedIndex = 0;
	private Font customFont;
	private BufferedImage backgroundImage;

	public PauseMenu(GameLogic gameLogic) {
		this.gameLogic = gameLogic;
		setPreferredSize(new Dimension(640, 360)); // 1/3 the size of the main menu
		setFocusable(true);
		requestFocusInWindow();

		try {
			InputStream fontStream = getClass().getResourceAsStream("/fonts/upheavtt.ttf");
			customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
		} catch (FontFormatException | IOException e) {
			System.out.println("Error loading custom font: " + e.getMessage());
		}

		try {
			backgroundImage = ImageIO.read(getClass().getResourceAsStream("/images/menubackground.jpg"));
		} catch (IOException e) {
			System.out.println("Error loading background image: " + e.getMessage());
		}

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP
						|| e.getKeyCode() == KeyEvent.VK_ENTER) {
					// Play mouse click sound
					try {
						AudioInputStream audioInputStream = AudioSystem
								.getAudioInputStream(getClass().getResource("/sfx/clickmouse.wav"));
						Clip mouseClickSound = AudioSystem.getClip();
						mouseClickSound.open(audioInputStream);
						mouseClickSound.start();
					} catch (Exception ex) {
						System.out.println("Error playing mouse click sound: " + ex.getMessage());
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					selectedIndex = (selectedIndex + 1) % options.length;
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
					selectedIndex = (selectedIndex - 1 + options.length) % options.length;
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					handleSelection();
				}
				repaint();
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Draw background image
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
		} else {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
		}

		// Draw white border
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		// Draw title
		if (customFont != null) {
			Font titleFont = customFont.deriveFont(40f); // Smaller font size
			g.setFont(titleFont);
		}
		g.setColor(Color.WHITE);
		String title = "PAUSED";
		int titleWidth = g.getFontMetrics().stringWidth(title);
		g.drawString(title, getWidth() / 2 - titleWidth / 2, 150);

		// Draw menu options
		if (customFont != null) {
			Font optionFont = customFont.deriveFont(20f); // Smaller font size
			g.setFont(optionFont);
		}
		int optionY = getHeight() / 2 - (options.length * 30) / 2 + 100;
		for (int i = 0; i < options.length; i++) {
			if (i == selectedIndex) {
				g.setColor(Color.RED); // Red highlight
			} else {
				g.setColor(Color.WHITE);
			}
			String option = options[i];
			int optionWidth = g.getFontMetrics().stringWidth(option);
			g.drawString(option, getWidth() / 2 - optionWidth / 2, optionY + i * 30 + 20);
		}
	}

	private void handleSelection() {
		switch (selectedIndex) {
		case 0:
			// Resume game
			gameLogic.resumeGame();
			break;
		case 1:
			// Handle options (no functionality right now)
			JOptionPane.showMessageDialog(null, "I am working on this feature and will implement it soon");
			break;
		case 2:
			// Exit game
			System.exit(0);
			break;
		}
	}

	@Override
	public void addNotify() {
		super.addNotify();
		requestFocusInWindow(); // Request focus for key events
	}
}
