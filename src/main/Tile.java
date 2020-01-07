package main;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Tile {
	
	private int x;
	private int y;
	private int num;
	private boolean isOpened;
	private boolean isFlaged;
	
	private Image image;
	
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
		isOpened = false;
	}
	
	public void open() {
		isOpened = true;
		setImage();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getNum() {
		return num;
	}

	public boolean isOpened() {
		return isOpened;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	private void setImage() {
		if(isOpened) {
			if(num >= 0 && num <= 9) {
				image = new ImageIcon(Main.class.getResource("/image/"+num+".png")).getImage();
			} else if(num == -1) {
				image = new ImageIcon(Main.class.getResource("/image/bomb.png")).getImage();
			}
		} else {
			if(isFlaged) {
				image = new ImageIcon(Main.class.getResource("/image/flag.png")).getImage();
			} else {
				image = new ImageIcon(Main.class.getResource("/image/none.png")).getImage();
			}
		}
	}

	public void setNum(int num) {
		this.num = num;
		setImage();
	}

	public void setOpened(boolean isOpened) {
		this.isOpened = isOpened;
		setImage();
	}

	public boolean isFlaged() {
		return isFlaged;
	}

	public void setFlaged(boolean isFlaged) {
		this.isFlaged = isFlaged;
		setImage();
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
}
