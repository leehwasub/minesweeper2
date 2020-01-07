package main;

public class CustomProperty {
	int width;
	int height;
	int bomb;
	
	public CustomProperty(int width, int height, int bomb){
		this.width = width;
		this.height = height;
		this.bomb = bomb;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getBomb() {
		return bomb;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setBomb(int bomb) {
		this.bomb = bomb;
	}
	
	
	
}
