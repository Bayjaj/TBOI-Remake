package main;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

//Modify your Room class to include the enemy spawning logic:
public class Room {
 private int id;
 private BufferedImage backgroundImage;
 private int width, height;
 private List<Door> doors;
 private List<Enemy> enemies;
 private int playerStartX;
 private int playerStartY;

 public Room(int id, BufferedImage backgroundImage, int width, int height) {
     this.id = id;
     this.backgroundImage = backgroundImage;
     this.width = width;
     this.height = height;
     this.doors = new ArrayList<>();
     this.enemies = new ArrayList<>();
     // Call spawnEnemies method to add enemies if the room is 5 or 6
     spawnEnemies();
 }

 // Method to spawn enemies
 private void spawnEnemies() {
     // Only spawn enemies for room 5 and 6
     if (id == 5 || id == 6) {
         for (int i = 0; i < 4; i++) { // 4 enemies per room
             int randomX = (int) (Math.random() * (width - 100)); // Assuming 100 is the enemy size
             int randomY = (int) (Math.random() * (height - 100)); // Assuming 100 is the enemy size
             enemies.add(new Enemy(randomX, randomY)); // Create and add a new enemy
         }
     }
 }

 public int getPlayerStartX() {
     return playerStartX;
 }

 public int getPlayerStartY() {
     return playerStartY;
 }

 public void addDoor(Door door) {
     doors.add(door);
 }

 public void addEnemy(Enemy enemy) {
     enemies.add(enemy);
 }

 public List<Door> getDoors() {
     return doors;
 }

 public List<Enemy> getEnemies() {
     return enemies;
 }

 public BufferedImage getBackgroundImage() {
     return backgroundImage;
 }

 public Point getDoorPosition() {
     // Assuming the door is at the center of the wall
     return new Point(width / 2, height / 2);
 }
}
