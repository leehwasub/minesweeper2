package main;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class MainWindow extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	private GameManager gameManager = GameManager.getInstance();
	
	private MainPanel mainPanel = new MainPanel();
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem beginnerMenu;
	private JMenuItem advancedMenu;
	private JMenuItem expertMenu;
	private JMenuItem customMenu;

	public MainWindow() {
		setTitle("지뢰 찾기");
		setSize(1280, 720);
		setResizable(false);
		initMenu();
		getContentPane().add(mainPanel);
		gameManager.initMap(10, 10, 10);
		setSize(mainPanel.getNeedWidth(), mainPanel.getNeedHeight());
		mainPanel.changeButtonLoc();
	}
	
	private void initMenu() {
		menuBar = new JMenuBar();
		menu = new JMenu("메뉴");
		menuBar.add(menu);
		beginnerMenu = new JMenuItem("초급");
		advancedMenu = new JMenuItem("중급");
		expertMenu = new JMenuItem("고급");
		customMenu = new JMenuItem("커스텀");
		beginnerMenu.addActionListener(this);
		advancedMenu.addActionListener(this);
		expertMenu.addActionListener(this);
		customMenu.addActionListener(this);
		menu.add(beginnerMenu);
		menu.add(advancedMenu);
		menu.add(expertMenu);
		menu.add(customMenu);
		setJMenuBar(menuBar);
	}
	
	@Override
	public void paintComponents(Graphics g) {
		super.paintComponents(g);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == beginnerMenu) {
			gameManager.initMap(10, 10, 10);
			setSize(mainPanel.getNeedWidth(), mainPanel.getNeedHeight());
			mainPanel.changeButtonLoc();
		} else if(source == advancedMenu) {
			gameManager.initMap(16, 16, 40);
			setSize(mainPanel.getNeedWidth(), mainPanel.getNeedHeight());
			mainPanel.changeButtonLoc();
		} else if(source == expertMenu) {
			gameManager.initMap(30, 30, 160);
			setSize(mainPanel.getNeedWidth(), mainPanel.getNeedHeight());
			mainPanel.changeButtonLoc();
		} else if(source == customMenu) {
			CustomProperty property = showCustomDialog();
			if(property != null) {
				gameManager.initMap(property.getWidth(), property.getHeight(), property.getBomb());
				setSize(mainPanel.getNeedWidth(), mainPanel.getNeedHeight());
				mainPanel.changeButtonLoc();
			}
		}
	}
	
	public CustomProperty showCustomDialog() {
		JTextField widthTextField = new JTextField();
		JTextField heightTextField = new JTextField();
		JTextField bombTextField = new JTextField();
		final JComponent[] inputs = new JComponent[] {
				new JLabel("너비"),
				widthTextField,
				new JLabel("높이"),
				heightTextField,
				new JLabel("폭탄 개수"),
				bombTextField
		};
		int result = JOptionPane.showConfirmDialog(this, inputs, "Custom", JOptionPane.PLAIN_MESSAGE);
		if(result == JOptionPane.OK_OPTION) {
			int width = Integer.parseInt(widthTextField.getText().trim());
			int height = Integer.parseInt(heightTextField.getText().trim());
			int bomb = Integer.parseInt(bombTextField.getText().trim());
			return new CustomProperty(width, height, bomb);
		}
		return null;
	}

}
