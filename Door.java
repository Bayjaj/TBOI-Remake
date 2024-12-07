package main;

import java.awt.Rectangle;

public class Door {
	private int id;
	private Room room1, room2;
	private int x, y, width, height;
	private Direction direction; // Enum for door direction (LEFT, RIGHT, TOP, BOTTOM)

	public Door(int id, Room room1, Room room2, int x, int y, int width, int height) {
		this.id = id;
		this.room1 = room1;
		this.room2 = room2;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public Room getRoom1() {
		return room1;
	}

	public Room getRoom2() {
		return room2;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Direction getDirection() {
		return direction;
	}
}
