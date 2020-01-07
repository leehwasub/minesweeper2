package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.Key;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class MainPanel extends JPanel implements MouseListener, ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	private GameManager gameManager = GameManager.getInstance();
	private final static int START_X = 10;
	private final static int START_Y = 50;
	private final static int INTERVAL = 2;
	private final static int TILE_WIDTH = 20;
	private final static int TILE_HEIGHT = 20;
	
	private JButton mainButton = new JButton();

	public MainPanel() {
		setLayout(null);
		addMouseListener(this);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		rendering((Graphics2D)g);
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(START_X, START_X, TILE_WIDTH * gameManager.getMapWidth() + INTERVAL * (gameManager.getMapWidth() - 1), 40 - INTERVAL);
		g.setColor(Color.black);
		
		for(int i = 0; i < gameManager.getMapWidth(); i++) {
			for(int j = 0; j < gameManager.getMapHeight(); j++) {
				g.drawImage(gameManager.getNameMap(i, j).getImage(), START_X + j * (TILE_WIDTH + INTERVAL), START_Y + i * (TILE_HEIGHT + INTERVAL), this);
			}
		}
		
		g.setColor(Color.YELLOW);
		g.setFont(new Font("메이플스토리", Font.BOLD, 20));
		drawBombNum((Graphics2D)g);
		drawTime((Graphics2D)g);
		repaint();
	}
	
	public void drawBombNum(Graphics2D g) {
		int count = gameManager.countRemainFlag();
		String zero="";
		if(count < 10) {
			zero = "00";
		} else if(count >= 10 && count <= 99) {
			zero = "0";
		}
		g.drawString(zero+""+count, START_X * 2, START_Y - 15);
	}
	
	public void drawTime(Graphics2D g) {
		int count = gameManager.getTime();
		String zero="";
		if(count < 10) {
			zero = "00";
		} else if(count >= 10 && count <= 99) {
			zero = "0";
		}
		g.drawString(zero+""+count, getNeedWidth() - START_Y * 2 + 25, START_Y - 15);
	}
	
	public void rendering(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	}
	
	public int getNeedWidth() {
		return TILE_WIDTH * gameManager.getMapWidth() + INTERVAL * (gameManager.getMapWidth() - 1) + 30;
	}
	
	public int getNeedHeight() {
		return  TILE_HEIGHT * gameManager.getMapHeight() + INTERVAL * (gameManager.getMapHeight() - 1) + 110;
	}
	
	public void changeButtonLoc() {
		remove(mainButton);
		mainButton.removeActionListener(this);
		mainButton = new JButton();
		mainButton.setBounds(getNeedWidth()/2-20,15, 30, 30);
		mainButton.setVisible(true);
		mainButton.addActionListener(this);
		add(mainButton);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.isMetaDown()) {
			for(int i = 0; i < gameManager.getMapWidth(); i++) {
				for(int j = 0; j < gameManager.getMapHeight(); j++) {
					if(START_X + j * (TILE_WIDTH + INTERVAL) <= e.getX() && e.getX() <= START_X + j * (TILE_WIDTH + INTERVAL) + TILE_WIDTH 
							&& START_Y + i * (TILE_HEIGHT + INTERVAL) <= e.getY() && e.getY() <= START_Y + i * (TILE_HEIGHT + INTERVAL) + TILE_HEIGHT && !gameManager.getNameMap(i, j).isOpened()) {
						gameManager.setFlagOnTile(i, j);
					}
				}
			}
		} else {
			for(int i = 0; i < gameManager.getMapWidth(); i++) {
				for(int j = 0; j < gameManager.getMapHeight(); j++) {
					if(START_X + j * (TILE_WIDTH + INTERVAL) <= e.getX() && e.getX() <= START_X + j * (TILE_WIDTH + INTERVAL) + TILE_WIDTH 
							&& START_Y + i * (TILE_HEIGHT + INTERVAL) <= e.getY() && e.getY() <= START_Y + i * (TILE_HEIGHT + INTERVAL) + TILE_HEIGHT && !gameManager.getNameMap(i, j).isOpened()) {
						GameState state = gameManager.clickTile(i, j);
						if(state == GameState.LOSE) {
							UIManager.put("OptionPane.messageFont", new Font("메이플스토리", Font.PLAIN, 14));
							int selection = JOptionPane.showConfirmDialog(this, "아쉽네요! 다시한번 도전하실래요?", "게임 오버", JOptionPane.YES_NO_OPTION);
							if(selection == JOptionPane.YES_OPTION) {
								gameManager.initMap();
							}
						} else if(state == GameState.WIN) {
							UIManager.put("OptionPane.messageFont", new Font("메이플스토리", Font.PLAIN, 14));
							JOptionPane.showConfirmDialog(this, "훌륭하네요! 모든 지뢰를 찾았어요.", "승리", JOptionPane.PLAIN_MESSAGE);
						}
					}
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == mainButton) {
			gameManager.initMap();
		}
	}
	
}
