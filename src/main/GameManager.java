package main;

import java.util.LinkedList;
import java.util.Queue;

import javax.swing.ImageIcon;

public class GameManager {
	
	private static GameManager instance;
	private int mapWidth;
	private int mapHeight;
	private int mapBomb;
	private int flagNum;
	private Tile[][] gameMap;
	private boolean[][] visited;
	private boolean isGameEnd;
	private boolean isGameStarted;
	private int time;
	private Queue<Tile> tileQueue = new LinkedList<Tile>();
	
	private boolean isReady;
	
	private final int dx[] = {1,0,1,1,-1,-1,0,-1};
	private final int dy[] = {1,1,0,-1,1,-1,-1,0};

	private GameManager() {
		
	}
	
	public static GameManager getInstance() {
		if(instance == null) {
			instance = new GameManager();
		}
		return instance;
	}
	
	public void initMap() {
		initMap(mapWidth, mapHeight, mapBomb);
	}
	
	public void initMap(int width, int height, int bomb) {
		isReady = false;
		isGameEnd = false;
		isGameStarted = false;
		time = 0;
		this.mapWidth = width;
		this.mapHeight = height;
		this.mapBomb = bomb;
		gameMap = new Tile[mapWidth][mapHeight];
		visited = new boolean[mapWidth][mapHeight];
		for(int i = 0; i < mapWidth; i++) {
			for(int j = 0; j < mapHeight; j++) {
				gameMap[i][j] = new Tile(i, j);
				tileQueue.add(gameMap[i][j]);
			}
		}
		int remainBomb = this.mapBomb;
		while(!tileQueue.isEmpty() && remainBomb != 0) {
			Tile tile = tileQueue.poll();
			int random = (int)(Math.random() * (tileQueue.size()));
			if(remainBomb >= random) {
				tile.setNum(-1);
				remainBomb--;
			} else {
				tileQueue.add(tile);
			}
		}
		
		for(int i = 0; i < mapWidth; i++) {
			for(int j = 0; j < mapHeight; j++) {
				if(gameMap[i][j].getNum() != -1) {
					tileQueue.add(gameMap[i][j]);
				}
			}
		}
		
		while(!tileQueue.isEmpty()) {
			Tile tile = tileQueue.poll();
			int findBomb = 0;
			for(int i = 0; i < 8; i++) {
				int xx = tile.getX() + dx[i];
				int yy = tile.getY() + dy[i];
				if(xx >= 0 && yy >= 0 && xx < mapWidth && yy < mapHeight && gameMap[xx][yy].getNum() == -1) {
					findBomb++;
				}
			}
			tile.setNum(findBomb);
		}
		
		isReady = true;
	}

	public void initVisited() {
		for(int i = 0; i < mapWidth; i++) {
			for(int j = 0; j < mapHeight; j++) {
				visited[i][j] = false;
			}
		}
	}
	
	public void openAllBombTile() {
		for(int i = 0; i < mapWidth; i++) {
			for(int j = 0; j < mapHeight; j++) {
				if(gameMap[i][j].getNum() == -1) {
					gameMap[i][j].open();
				}
			}
		}
	}
	
	public GameState clickTile(int x, int y) {
		if(isReady && !gameMap[x][y].isOpened() && !gameMap[x][y].isFlaged() && !isGameEnd) {
			gameMap[x][y].setOpened(true);
			
			if(!isGameStarted) {
				isGameStarted = true;
				Thread thread = new Thread() {
					public void run() {
						while(isGameStarted) {
							try {
								sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if(!isGameStarted)break;
							time++;
						}
					}
				};
				thread.start();
			}
			
			//비어있을경우
			if(gameMap[x][y].getNum() == 0) {
				spreadTileClick(x, y);
				if(isGameEnd()) {
					setFlagOnBomb();
					return GameState.WIN;
				}
				return GameState.NOTHING;
			}
			else if(gameMap[x][y].getNum() == -1) {
				isGameEnd = true;
				openAllBombTile();
				gameMap[x][y].setImage(new ImageIcon(Main.class.getResource("/image/bombed.png")).getImage());
				return GameState.LOSE;
			}
			else if(isGameEnd()) {
				setFlagOnBomb();
				return GameState.WIN;
			}
			countFlag();
		}
		return GameState.NOTHING;
	}
	
	public void setFlagOnBomb() {
		for(int i = 0; i < mapWidth; i++) {
			for(int j = 0; j < mapHeight; j++) {
				if(gameMap[i][j].getNum() == -1 && !gameMap[i][j].isFlaged()) {
					gameMap[i][j].setFlaged(true);
				}
			}
		}
	}
	
	private boolean isGameEnd() {
		for(int i = 0; i < mapWidth; i++) {
			for(int j = 0; j < mapHeight; j++) {
				if(!gameMap[i][j].isOpened() && gameMap[i][j].getNum() >= 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public int countRemainFlag() {
		countFlag();
		return this.mapBomb - flagNum;
	}
	
	public void spreadTileClick(int x, int y) {
		initVisited();
		tileQueue.add(gameMap[x][y]);
		visited[x][y] = true;
		while(!tileQueue.isEmpty()) {
			Tile nowTile = tileQueue.poll();
			nowTile.setOpened(true);
			nowTile.setFlaged(false);
			if(nowTile.getNum() > 0) continue;
			
			for(int i = 0; i < 8; i++) {
				int xx = nowTile.getX() + dx[i];
				int yy = nowTile.getY() + dy[i];
				if(xx >= 0 && yy >= 0 && xx < mapWidth && yy < mapHeight && gameMap[xx][yy].getNum() >= 0 && !visited[xx][yy]) {
					tileQueue.add(gameMap[xx][yy]);
					visited[xx][yy] = true;
				}
			}
		}
	}
	
	public void countFlag() {
		int cal = 0;
		for(int i = 0; i < mapWidth; i++) {
			for(int j = 0; j < mapHeight; j++) {
				if(gameMap[i][j].isFlaged()) {
					cal++;
				}
			}
		}
		flagNum = cal;
	}
	
	public void setFlagOnTile(int x, int y) {
		if(!gameMap[x][y].isOpened() && !isGameEnd) {
			if(gameMap[x][y].isFlaged()) {
				gameMap[x][y].setFlaged(false);
			}else {
				gameMap[x][y].setFlaged(true);
			}
			countFlag();
		}
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	public int getMapBomb() {
		return mapBomb;
	}

	public void setMapWidth(int mapWidth) {
		this.mapWidth = mapWidth;
	}

	public void setMapHeight(int mapHeight) {
		this.mapHeight = mapHeight;
	}

	public void setMapBomb(int mapBomb) {
		this.mapBomb = mapBomb;
	}

	public Tile[][] getGameMap() {
		return gameMap;
	}
	
	public Tile getNameMap(int x, int y) {
		return gameMap[x][y];
	}

	public void setGameMap(Tile[][] gameMap) {
		this.gameMap = gameMap;
	}

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
}
