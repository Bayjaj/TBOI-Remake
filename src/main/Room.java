package main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Room {  
	   private int id;  
	   private BufferedImage backgroundImage;  
	   private int width, height;  
	   private List<Door> doors;  
	   private List<Enemy> enemies;  
	   //private List<Item> items;  
	  
	   public Room(int id, BufferedImage backgroundImage, int width, int height) {  
	      this.id = id;  
	      this.backgroundImage = backgroundImage;  
	      this.width = width;  
	      this.height = height;  
	      this.doors = new ArrayList<>();  
	      this.enemies = new ArrayList<>();  
	      //this.items = new ArrayList<>();  
	   }  
	  
	   public void addDoor(Door door) {  
	      doors.add(door);  
	   }  
	  
	   public void addEnemy(Enemy enemy) {  
	      enemies.add(enemy);  
	   }  
	  
	   //public void addItem(Item item) {  
	   //   items.add(item);  
	   //}  
	  
	   public List<Door> getDoors() {  
	      return doors;  
	   }  
	   
	   public List<Enemy> getEnemies() {  
		      return enemies;  
		   } 
	  
	   public BufferedImage getBackgroundImage() {  
	      return backgroundImage;  
	   }  
	}
