package main;

//SplashScreen.java  
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;

public class SplashScreen extends JPanel {
	private JFXPanel jfxPanel;

	public SplashScreen() {
		// Create a new jfxpanel and add it
		jfxPanel = new JFXPanel();
		add(jfxPanel);

		// Get the media, create a media player, add the video pane to the scene, set the scene size
		Platform.runLater(() -> {
			Media media = new Media(getClass().getResource("/videos/tboi.mp4").toExternalForm());
			MediaPlayer mediaPlayer = new MediaPlayer(media);
			MediaView mediaView = new MediaView(mediaPlayer);

			StackPane stackPane = new StackPane();
			stackPane.getChildren().add(mediaView);

			Scene scene = new Scene(stackPane, 1920, 1080);
			jfxPanel.setScene(scene);
			
			// Set the media to play once and play it 
			mediaPlayer.setCycleCount(1);
			mediaPlayer.play();

			// When the media ends, get rid of the splash screen and start the main class
			mediaPlayer.setOnEndOfMedia(() -> {
				SwingUtilities.invokeLater(() -> {
					JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(SplashScreen.this);
					frame.dispose();
					Main.main(null);
				});
			});
		});
	}

	// Create the frame for the splash screen and set it visible
	public static void main(String[] args) {
		JFrame frame = new JFrame("The Binding of Isaac Revived");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1920, 1080);
		frame.add(new SplashScreen());
		frame.setVisible(true);
	}
}
