package main;  
  
import java.awt.BorderLayout;  
import java.awt.Color;  
import java.awt.FlowLayout;  
import java.awt.Font;  
import java.awt.FontFormatException;  
import java.awt.Graphics;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.awt.event.KeyEvent;  
import java.awt.event.KeyListener;  
import java.awt.image.BufferedImage;  
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;  
import javax.sound.sampled.AudioSystem;  
import javax.sound.sampled.Clip;  
import javax.swing.JButton;  
import javax.swing.JLabel;  
import javax.swing.JPanel;  
  
public class DeathMenu extends JPanel {  
   private GameLogic gameLogic;  
   private int selectedOption = 0;  
   private JButton newRunButton;  
   private JButton exitButton;  
   private BufferedImage backgroundImage;  
  
   public DeathMenu(GameLogic gameLogic) {  
      this.gameLogic = gameLogic;  
      setLayout(new BorderLayout());  
      try {  
        backgroundImage = ImageIO.read(getClass().getResourceAsStream("/images/menubackground.jpg"));  
      } catch (IOException e) {  
        System.out.println("Error loading background image: " + e.getMessage());  
      }  
  
      JLabel deathLabel = new JLabel("YOU DIED");  
      try {  
        Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/upheavtt.ttf"));  
        font = font.deriveFont(48f);  
        deathLabel.setFont(font);  
      } catch (FontFormatException | IOException e) {  
        System.out.println("Error loading font: " + e.getMessage());  
      }  
      deathLabel.setForeground(Color.WHITE);  
      add(deathLabel, BorderLayout.NORTH);  
  
      JPanel buttonPanel = new JPanel();  
      buttonPanel.setLayout(new FlowLayout());  
      buttonPanel.setBackground(new Color(0, 0, 0, 0));  
  
      newRunButton = new JButton("New Run");  
      newRunButton.addActionListener(new ActionListener() {  
        @Override  
        public void actionPerformed(ActionEvent e) {  
           gameLogic.startNewGame();  
        }  
      });  
      buttonPanel.add(newRunButton);  
  
      exitButton = new JButton("Exit Game");  
      exitButton.addActionListener(new ActionListener() {  
        @Override  
        public void actionPerformed(ActionEvent e) {  
           System.exit(0);  
        }  
      });  
      buttonPanel.add(exitButton);  
  
      add(buttonPanel, BorderLayout.SOUTH);  
  
      addKeyListener(new KeyListener() {  
        @Override  
        public void keyTyped(KeyEvent e) {}  
  
        @Override  
        public void keyPressed(KeyEvent e) {  
           if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_UP) {  
              selectedOption--;  
              if (selectedOption < 0) {  
                selectedOption = 1;  
              }  
           } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_DOWN) {  
              selectedOption++;  
              if (selectedOption > 1) {  
                selectedOption = 0;  
              }  
           } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
              if (selectedOption == 0) {  
                gameLogic.startNewGame();  
              } else if (selectedOption == 1) {  
                System.exit(0);  
              }  
           }  
           updateButtonColors();  
           playClickSound();  
        }  
  
        @Override  
        public void keyReleased(KeyEvent e) {}  
      });  
      updateButtonColors();  
      setFocusable(true);  
      requestFocusInWindow();  
   }  
  
   private void updateButtonColors() {  
      if (selectedOption == 0) {  
        newRunButton.setForeground(Color.RED);  
        exitButton.setForeground(Color.WHITE);  
      } else if (selectedOption == 1) {  
        newRunButton.setForeground(Color.WHITE);  
        exitButton.setForeground(Color.RED);  
      }  
   }  
  
   private void playClickSound() {  
      try {  
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/sfx/clickmouse.wav"));  
        Clip clickSound = AudioSystem.getClip();  
        clickSound.open(audioInputStream);  
        clickSound.start();  
      } catch (Exception e) {  
        System.out.println("Error playing click sound: " + e.getMessage());  
      }  
   }  
  
   @Override  
   protected void paintComponent(Graphics g) {  
      super.paintComponent(g);  
      if (backgroundImage != null) {  
        g.drawImage(backgroundImage, 0, 0, 400, 200, this);  
      }  
   }  
}
