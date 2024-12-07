package main;

//MainMenu.java  
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MainMenu extends JPanel {
	private Main main;
	private String[] options = { "New Run", "Resume Game (WIP)", "Options", "Exit Game" };
	private int selectedIndex = 0;
	private Clip menuTheme;
	private BufferedImage backgroundImage;

	public MainMenu(Main main) {
		// Setup the main window like usual
		this.main = main;
		setPreferredSize(new Dimension(1920, 1080));
		setFocusable(true); // Ensure this JPanel can receive key events
		requestFocusInWindow(); // Request focus for key event listening

		// Start the music or give me an error if it's not working
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(getClass().getResource("/sfx/menutheme.wav"));
			menuTheme = AudioSystem.getClip();
			menuTheme.open(audioInputStream);
			menuTheme.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (Exception e) {
			System.out.println("Error loading menu theme: " + e.getMessage());
		}

		// Set the background image of the main menu
		try {
			backgroundImage = ImageIO.read(getClass().getResourceAsStream("/images/menubackground.jpg"));
		} catch (IOException e) {
			System.out.println("Error loading background image: " + e.getMessage());
		}

		// Listen for key events to navigate menu options
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

				// Tells us which index is selected at that given moment
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

		// Set background image or give a backup if the image isn't there
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null); // send the image, 0,0 is top left corner
																				// of image, width and height are the
																				// 1920x1080, null is irrelevant
		} else {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
		}

		// Load my custom font
		Font customFont = null;
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/upheavtt.ttf"));
		} catch (Exception e) {
			System.out.println("Error loading custom font: " + e.getMessage());
		}

		// Set text properties for the title
		if (customFont != null) {
			Font titleFont = customFont.deriveFont(80f); // Set font size to 80
			g.setFont(titleFont);
		}
		g.setColor(Color.WHITE);
		String title = "Main Menu";
		int titleWidth = g.getFontMetrics().stringWidth(title);
		g.drawString(title, getWidth() / 2 - titleWidth / 2, 150);

		// Set text properties for the menu options
		if (customFont != null) {
			Font optionFont = customFont.deriveFont(30f); // Set font size to 30
			g.setFont(optionFont);
		}
		int optionY = getHeight() / 2 - (options.length * 60) / 2;
		for (int i = 0; i < options.length; i++) {
			if (i == selectedIndex) {
				g.setColor(Color.RED); // Set text color to red for selected option
			} else {
				g.setColor(Color.WHITE);
			}
			String option = options[i];
			int optionWidth = g.getFontMetrics().stringWidth(option);
			g.drawString(option, getWidth() / 2 - optionWidth / 2, optionY + i * 60 + 30);
		}
	}

	// Function with switch statement for the different menu options
	private void handleSelection() {
		switch (selectedIndex) {
		case 0:
			// Start new run [THIS COULD BE CAUSING PROBLEMS]
			stopMenuTheme();
			// I literally removed the next 3 lines and nothing changed... this is not how
			// this should work
			new Thread(() -> {
				main.getGameLogic().startNewGame();
			}).start();
			main.showGamePanel();
			break;
		case 1:
			// Resume game (WIP)
		case 2:
			// Options menu (WIP)
			JOptionPane.showMessageDialog(null, "This feature is a work in progress");
			break;
		case 3:
			// Exit game
			System.exit(0);
			break;
		}
	}

	// Stop the menu music
	public void stopMenuTheme() {
		menuTheme.stop();
	}

	// Lets the menu get focus so the keystrokes work
	public void requestFocusForMenu() {
		requestFocusInWindow();
	}
}
