import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Hedgehog Havoc
 * A strategy game about a hedgehog trapping badgers.
 * 
 * @author Daniel Brown
 * 
 */
public class HedgehogHavoc extends StateBasedGame {
	// State IDs for each state.
	public static final int PLAYINGSTATE = 0;
	public static final int PAUSESTATE = 1;
	public static final int LEVELSELECTSTATE = 2;
	public static final int GAMEOVERSTATE = 3;
	
	// Resources
	public static final String BACKGROUND_IMG = "resource/background.png";
	public static final String HUDBACKGROUND_IMG = "resource/hudbackground.png";
	public static final String LEVELSELECTBACKGROUND_IMG = "resource/level_select.png";
	public static final String PAUSEBACKGROUND_IMG = "resource/paused.png";
	public static final String GAMEOVERSCREEN_IMG = "resource/youwin.png";
	public static final String LOGO_IMG = "resource/hedgehoghavoc_logo.png";
	public static final String LEVEL1_IMG = "resource/level1.png";
	public static final String LEVEL2_IMG = "resource/level2.png";
	public static final String LEVEL3_IMG = "resource/level3.png";
	public static final String LEVEL4_IMG = "resource/level4.png";
	public static final String LEVEL5_IMG = "resource/level5.png";
	public static final String GAMEOVER_IMG = "resource/gameover.png";
	public static final String HEDGEHOGRIGHT_IMG = "resource/hedgehog_right.png";
	public static final String HEDGEHOGLEFT_IMG = "resource/hedgehog_left.png";
	public static final String HEDGEHOGUP_IMG = "resource/hedgehog_up.png";
	public static final String HEDGEHOGDOWN_IMG = "resource/hedgehog_down.png";
	public static final String BADGERRIGHT_IMG = "resource/badger_right.png";
	public static final String BADGERLEFT_IMG = "resource/badger_left.png";
	public static final String BLOCK_MOVABLE_IMG = "resource/block_movable.png";
	public static final String BLOCK_IMMOVABLE_IMG = "resource/block_immovable.png";
	public static final String BUG_IMG = "resource/bug.png";
	public static final String HOLE_IMG = "resource/hole.png";
	public static final String PATH_DEBUG_IMG = "resource/path_debug.png";
	public static final int BADGERCOUNT = 6;
	public static final int MAXBADGERS = 6;
	public static final int TIMERCOUNT = 60;
	public static final int TRANSITIONCOUNT = 150;

	public final int ScreenWidth;
	public final int ScreenHeight;
	public final int HUDHeight;
	
	int fps;
	int score;
	int highScore;
	int lives;
	int currentLevel;
	int second;
	int remainingBadgers;
	long previousTime;
	boolean confirmLevel;
	
	Tile[][] grid;
	Hedgehog hedgehog;
	
	public static boolean debug = false;
	public static boolean godMode = false;
	private static AppGameContainer app;

	/**
	 * Create the BounceGame frame, saving the width and height for later use.
	 * 
	 * @param title
	 *            the window's title
	 * @param width
	 *            the window's width
	 * @param height
	 *            the window's height
	 */
	public HedgehogHavoc(String title, int width, int height, int hudheight) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;
		HUDHeight = hudheight;

		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
	}


	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new PlayingState());
		addState(new PauseState());
		addState(new LevelSelectState());
		addState(new GameOverState());
		
		// Pre-loading all image resources
		ResourceManager.loadImage(BACKGROUND_IMG);
		ResourceManager.loadImage(HUDBACKGROUND_IMG);
		ResourceManager.loadImage(PAUSEBACKGROUND_IMG);
		ResourceManager.loadImage(GAMEOVERSCREEN_IMG);
		ResourceManager.loadImage(LEVELSELECTBACKGROUND_IMG);
		ResourceManager.loadImage(LOGO_IMG);
		ResourceManager.loadImage(LEVEL1_IMG);
		ResourceManager.loadImage(LEVEL2_IMG);
		ResourceManager.loadImage(LEVEL3_IMG);
		ResourceManager.loadImage(LEVEL4_IMG);
		ResourceManager.loadImage(LEVEL5_IMG);
		ResourceManager.loadImage(GAMEOVER_IMG);
		ResourceManager.loadImage(HEDGEHOGLEFT_IMG);
		ResourceManager.loadImage(HEDGEHOGRIGHT_IMG);
		ResourceManager.loadImage(HEDGEHOGUP_IMG);
		ResourceManager.loadImage(HEDGEHOGDOWN_IMG);
		ResourceManager.loadImage(BADGERLEFT_IMG);
		ResourceManager.loadImage(BADGERRIGHT_IMG);
		ResourceManager.loadImage(BLOCK_MOVABLE_IMG);
		ResourceManager.loadImage(BLOCK_IMMOVABLE_IMG);
		ResourceManager.loadImage(BUG_IMG);
		ResourceManager.loadImage(HOLE_IMG);
		ResourceManager.loadImage(PATH_DEBUG_IMG);
		
		// Initialize entities
		grid = new Tile[23][23];
		for (int i = 0; i < 23; i++) {
			for (int j = 0; j < 23; j++) {
				grid[i][j] = new Tile(i, j);
				grid[i][j].setDebugActive(false);
			}
		}
		
		Date tempTime = new Date();
		previousTime = tempTime.getTime();
		second = TIMERCOUNT;
		currentLevel = 1;
		score = 0;
		lives = 3;
		remainingBadgers = BADGERCOUNT;
		confirmLevel = false;
		hedgehog = new Hedgehog(11, 11, HUDHeight);
		grid[11][11].setHedgehog(hedgehog);
		grid[11][11].setIsHedgehog(true);
		
		// Load score from score file (if it exists).
		File scoreFile = new File("score.txt");
		Scanner fileReader;
		String fileScore = "";
		try {
			fileReader = new Scanner(scoreFile);
			while (fileReader.hasNextLine()) {
				fileScore += fileReader.nextLine();
			}
			
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		if (!fileScore.trim().isEmpty()) {
			highScore = Integer.parseInt(fileScore);
		} else {
			highScore = 0;
		}
	}
	
	public void renderStats(Graphics g) {
		Date tempTime = new Date();
		int tempSecond = (int) ((tempTime.getTime() - previousTime) / 1000);
		if (tempSecond >= 1) {
			second -= 1;
			previousTime = tempTime.getTime();
		}
		
		Font font = g.getFont();
		g.drawImage(ResourceManager.getImage(HUDBACKGROUND_IMG), 0, 0);
		g.setColor(Color.white);
		g.drawString("FPS: " + fps,
				10,
				ScreenHeight - font.getHeight("FPS:") - 10);
		g.drawString(
				"High Score: " + highScore,
				ScreenWidth - font.getWidth("High Score: " + highScore) - 10,
				5);
		g.drawString(
				"Score: " + score,
				ScreenWidth - font.getWidth("Score: " + score) - 10,
				10 + font.getHeight("Score"));
		g.drawString(
				"Timer: " + String.format("%02d", second),
				(ScreenWidth / 2) - (font.getWidth("Timer: " + String.format("%02d", second)) / 2),
				5);
		g.drawString(
				"Remaining Badgers: " + remainingBadgers,
				(ScreenWidth / 2) - (font.getWidth("Remaining Badgers: " + remainingBadgers) / 2),
				10 + font.getHeight("Haha"));
		
		if (godMode) {
			g.drawString("God Mode", 10, 18);
		} else if (lives <= 3) {
			for (int i = 0; i < lives - 1; i++) {
				g.drawImage(ResourceManager.getImage(HEDGEHOGDOWN_IMG),
						ResourceManager.getImage(HEDGEHOGDOWN_IMG).getWidth() * i + 10 * (i + 1),
						10);
			}
		} else {
			g.drawImage(ResourceManager.getImage(HEDGEHOGDOWN_IMG),
					10,
					10);
			g.drawString(" x " + lives, ResourceManager.getImage(HEDGEHOGDOWN_IMG).getWidth() + 10, 18);
		}
	}
	
	public void setLevel() {
		if (currentLevel == 1) {
			for (int i = 4; i < 19; i++) {
				for (int j = 4; j < 19; j++) {
					// Making sure we're not overwriting the hedgehog with this if-statement.
					if (i != 11 || j != 11) {
						grid[i][j].setBlockMovable(new BlockMovable(i, j, HUDHeight));
					}
				}
			}
			
			for (int i = 0; i < 23; i++) {
				grid[i][0].setBlockImmovable(new BlockImmovable(i, 0, HUDHeight));
				grid[0][i].setBlockImmovable(new BlockImmovable(0, i, HUDHeight));
				grid[22][i].setBlockImmovable(new BlockImmovable(22, i, HUDHeight));
				grid[i][22].setBlockImmovable(new BlockImmovable(i, 22, HUDHeight));
			}
			
			grid[4][3].setBadger(new Badger(4, 3, HUDHeight));
		} else if (currentLevel == 2) {
			for (int i = 4; i < 19; i++) {
				for (int j = 4; j < 19; j++) {
					// Making sure we're not overwriting the hedgehog with this if-statement.
					if (i != 11 || j != 11) {
						grid[i][j].setBlockMovable(new BlockMovable(i, j, HUDHeight));
					}
				}
			}
			
			for (int i = 0; i < 23; i++) {
				grid[i][0].setBlockImmovable(new BlockImmovable(i, 0, HUDHeight));
				grid[0][i].setBlockImmovable(new BlockImmovable(0, i, HUDHeight));
				grid[22][i].setBlockImmovable(new BlockImmovable(22, i, HUDHeight));
				grid[i][22].setBlockImmovable(new BlockImmovable(i, 22, HUDHeight));
			}
			
			grid[1][10].setBlockImmovable(new BlockImmovable(1, 10, HUDHeight));
			grid[1][12].setBlockImmovable(new BlockImmovable(1, 12, HUDHeight));
			grid[2][14].setBlockImmovable(new BlockImmovable(2, 14, HUDHeight));
			grid[5][6].setBlockImmovable(new BlockImmovable(5, 6, HUDHeight));
			grid[7][2].setBlockImmovable(new BlockImmovable(7, 2, HUDHeight));
			grid[8][8].setBlockImmovable(new BlockImmovable(8, 8, HUDHeight));
			grid[9][14].setBlockImmovable(new BlockImmovable(9, 14, HUDHeight));
			grid[10][9].setBlockImmovable(new BlockImmovable(10, 9, HUDHeight));
			grid[14][6].setBlockImmovable(new BlockImmovable(14, 6, HUDHeight));
			grid[14][11].setBlockImmovable(new BlockImmovable(14, 11, HUDHeight));
			grid[14][14].setBlockImmovable(new BlockImmovable(14, 14, HUDHeight));
			grid[14][17].setBlockImmovable(new BlockImmovable(14, 17, HUDHeight));
			grid[16][14].setBlockImmovable(new BlockImmovable(16, 14, HUDHeight));
			grid[18][14].setBlockImmovable(new BlockImmovable(18, 14, HUDHeight));
			grid[18][17].setBlockImmovable(new BlockImmovable(18, 17, HUDHeight));
			grid[20][5].setBlockImmovable(new BlockImmovable(20, 5, HUDHeight));
			grid[20][12].setBlockImmovable(new BlockImmovable(20, 12, HUDHeight));
			
			grid[11][20].setBadger(new Badger(11, 20, HUDHeight));
			grid[11][20].getBadger().moveSpeed = 2;
			grid[20][18].setBadger(new Badger(20, 18, HUDHeight));
			grid[20][18].getBadger().moveSpeed = 2;
		} else if (currentLevel == 3) {
			for (int i = 0; i < 23; i++) {
				grid[i][0].setBlockImmovable(new BlockImmovable(i, 0, HUDHeight));
				grid[0][i].setBlockImmovable(new BlockImmovable(0, i, HUDHeight));
				grid[22][i].setBlockImmovable(new BlockImmovable(22, i, HUDHeight));
				grid[i][22].setBlockImmovable(new BlockImmovable(i, 22, HUDHeight));
			}
			
			int[][] immovableBlocks = {
					{3, 4}, {5, 15}, {7, 17}, {10, 6}, {11, 12}, {12, 5}, {12, 8}, {14, 12},
					{15, 8}, {16, 19}, {17, 4}, {18, 9}, {20, 7}};
			
			int[][] movableBlocks = {
					{1, 3}, {1, 4}, {1, 7}, {1, 9}, {1, 10}, {1, 15}, {1, 20},
					{2, 1}, {2, 2}, {2, 3}, {2, 6}, {2, 8}, {2, 12}, {2, 13}, {2, 16}, {2, 19}, {2, 20},
					{3, 8}, {3, 9}, {3, 10}, {3, 13}, {3, 16}, {3, 19}, {3, 20},
					{4, 8}, {4, 9}, {4, 10}, {4, 13}, {4, 16}, {4, 18},
					{5, 3}, {5, 4}, {5, 7}, {5, 8}, {5, 12}, {5, 16}, {5, 17}, {5, 18},
					{6, 2}, {6, 8}, {6, 11}, {6, 12}, {6, 14}, {6, 16}, {6, 18},
					{7, 2}, {7, 3}, {7, 16}, {7, 20},
					{8, 4}, {8, 8}, {8, 13}, {8, 17},
					{9, 1}, {9, 4}, {9, 5}, {9, 7}, {9, 11}, {9, 14}, {9, 20},
					{10, 1}, {10, 2}, {10, 4}, {10, 5}, {10, 10}, {10, 14}, {10, 16}, {10, 17}, {10, 20},
					{11, 1}, {11, 2}, {11, 4}, {11, 15}, {11, 17},
					{12, 1}, {12, 2}, {12, 3}, {12, 6}, {12, 9}, {12, 15}, {12, 19},
					{13, 7}, {13, 8}, {13, 9}, {13, 10}, {13, 12}, {13, 17},
					{14, 6}, {14, 7}, {14, 9}, {14, 11}, {14, 19},
					{15, 2}, {15, 4}, {15, 5}, {15, 10}, {15, 11}, {15, 16}, {15, 18},
					{16, 3}, {16, 4}, {16, 8}, {16, 9}, {16, 11}, {16, 12}, {16, 14}, {16, 17},
					{17, 2}, {17, 3}, {17, 8},
					{18, 1}, {18, 2}, {18, 6}, {18, 14}, {18, 16}, {18, 20},
					{19, 3}, {19, 6}, {19, 7}, {19, 13}, {19, 15}, {19, 17}, {19, 20},
					{20, 1}, {20, 5}, {20, 9}, {20, 14}, {20, 16}, {20, 17}, {20, 20}};
			
			for(int i = 0; i < immovableBlocks.length; i++) {
				grid[immovableBlocks[i][0]][immovableBlocks[i][1]].setBlockImmovable(
						new BlockImmovable(immovableBlocks[i][0], immovableBlocks[i][1], HUDHeight));
			}
			
			for(int i = 0; i < movableBlocks.length; i++) {
				grid[movableBlocks[i][0]][movableBlocks[i][1]].setBlockMovable(
						new BlockMovable(movableBlocks[i][0], movableBlocks[i][1], HUDHeight));
			}
			
			grid[2][20].setBadger(new Badger(2, 20, HUDHeight));
			grid[2][20].getBadger().moveSpeed = 3;
			grid[11][20].setBadger(new Badger(11, 20, HUDHeight));
			grid[11][20].getBadger().moveSpeed = 3;
			grid[21][20].setBadger(new Badger(21, 20, HUDHeight));
			grid[21][20].getBadger().moveSpeed = 3;
		} else if (currentLevel == 4) {
			for (int i = 0; i < 23; i++) {
				grid[i][0].setBlockImmovable(new BlockImmovable(i, 0, HUDHeight));
				grid[0][i].setBlockImmovable(new BlockImmovable(0, i, HUDHeight));
				grid[22][i].setBlockImmovable(new BlockImmovable(22, i, HUDHeight));
				grid[i][22].setBlockImmovable(new BlockImmovable(i, 22, HUDHeight));
			}
			
			for (int i = 2; i < 21; i++) {
				for (int j = 2; j < 21; j++) {
					if (i % 2 == 0 && j % 2 != 0) {
						grid[i][j].setBlockMovable(new BlockMovable(i, j, HUDHeight));
					} else if (i % 2 != 0 && j % 2 == 0) {
						grid[i][j].setBlockMovable(new BlockMovable(i, j, HUDHeight));
					}
				}
			}
			
			grid[2][6].setHole(new Hole(2, 6, HUDHeight));
			grid[3][12].setBlockMovable(null);
			grid[3][12].setHole(new Hole(3, 12, HUDHeight));
			grid[5][5].setHole(new Hole(5, 5, HUDHeight));
			grid[10][8].setHole(new Hole(10, 8, HUDHeight));
			grid[12][7].setBlockMovable(null);
			grid[12][7].setHole(new Hole(12, 7, HUDHeight));
			grid[15][12].setBlockMovable(null);
			grid[15][12].setHole(new Hole(15, 12, HUDHeight));
			
			grid[21][20].setBadger(new Badger(21, 20, HUDHeight));
			grid[21][20].getBadger().moveSpeed = 3;
		} else if (currentLevel == 5) {
			for (int i = 0; i < 23; i++) {
				grid[i][0].setBlockImmovable(new BlockImmovable(i, 0, HUDHeight));
				grid[0][i].setBlockImmovable(new BlockImmovable(0, i, HUDHeight));
				grid[22][i].setBlockImmovable(new BlockImmovable(22, i, HUDHeight));
				grid[i][22].setBlockImmovable(new BlockImmovable(i, 22, HUDHeight));
			}
			
			int[][] immovableBlocks = {{1, 19}, {2, 5}, {2, 7}, {2, 9}, {2, 17}, {3, 2}, {3, 3},
					{4, 10}, {4, 13}, {5, 7}, {5, 8}, {5, 16}, {6, 5}, {6, 8}, {6, 17},
					{7, 2}, {7, 13}, {7, 17}, {7, 18}, {7, 20}, {8, 9}, {8, 11}, {8, 18},
					{9, 10}, {10, 4}, {10, 12}, {10, 15}, {10, 20}, {11, 19},
					{12, 7}, {12, 11}, {12, 14}, {12, 16}, {12, 19}, {13, 2}, {13, 12}, {13, 17},
					{14, 1}, {15, 11}, {15, 18}, {15, 19}, {16, 7}, {16, 10},
					{17, 3}, {17, 7}, {17, 8}, {17, 11}, {17, 15}, {17, 16}, {18, 10}, {18, 20},
					{19, 3}, {19, 16}, {19, 20}, {20, 4}, {20, 16}};
			
			int[][] movableBlocks = {{2, 19}, {3, 4}, {3, 6}, {3, 7}, {3, 8}, {3, 9}, {3, 11}, {3, 14}, {3, 15},
					{4, 12}, {4, 15}, {4, 20}, {5, 17}, {6, 4}, {6, 13}, {6, 14}, {6, 15}, {6, 16}, {6, 20},
					{7, 7}, {7, 8}, {8, 3}, {8, 7}, {8, 17}, {9, 1}, {9, 9},
					{10, 2}, {10, 7}, {10, 8}, {10, 9}, {10, 13}, {10, 17}, {11, 1}, {11, 5}, {11, 9},
					{12, 4}, {12, 12}, {12, 15}, {13, 3}, {13, 16}, {13, 19}, {13, 20},
					{14, 3}, {14, 5}, {14, 12}, {14, 15}, {14, 20}, {15, 9}, {15, 17},
					{16, 11}, {16, 15}, {17, 12}, {17, 14}, {17, 17}, {17, 20},
					{18, 5}, {20, 7}, {20, 9}, {20, 13}, {20, 15}, {20, 18}};
			
			for(int i = 0; i < immovableBlocks.length; i++) {
				grid[immovableBlocks[i][0]][immovableBlocks[i][1]].setBlockImmovable(
						new BlockImmovable(immovableBlocks[i][0], immovableBlocks[i][1], HUDHeight));
			}
			
			for(int i = 0; i < movableBlocks.length; i++) {
				grid[movableBlocks[i][0]][movableBlocks[i][1]].setBlockMovable(
						new BlockMovable(movableBlocks[i][0], movableBlocks[i][1], HUDHeight));
			}
			
			grid[2][8].setHole(new Hole(2, 8, HUDHeight));
			grid[3][12].setHole(new Hole(3, 12, HUDHeight));
			grid[5][11].setHole(new Hole(5, 11, HUDHeight));
			grid[7][9].setHole(new Hole(7, 9, HUDHeight));
			grid[8][17].setHole(new Hole(8, 17, HUDHeight));
			grid[13][6].setHole(new Hole(13, 6, HUDHeight));
			grid[13][19].setHole(new Hole(13, 19, HUDHeight));
			grid[15][6].setHole(new Hole(15, 6, HUDHeight));
			grid[15][12].setHole(new Hole(15, 12, HUDHeight));
			grid[16][5].setHole(new Hole(16, 5, HUDHeight));
			grid[19][9].setHole(new Hole(19, 9, HUDHeight));
			
			grid[2][9].setBadger(new Badger(2, 9, HUDHeight));
			grid[2][9].getBadger().moveSpeed = 3;
			grid[18][8].setBadger(new Badger(18, 8, HUDHeight));
			grid[18][8].getBadger().moveSpeed = 3;
		}
	}
	
	public void restartGame() {
		// Initialize entities
		grid = new Tile[23][23];
		for (int i = 0; i < 23; i++) {
			for (int j = 0; j < 23; j++) {
				grid[i][j] = new Tile(i, j);
				grid[i][j].setDebugActive(false);
			}
		}
		
		Date tempTime = new Date();
		previousTime = tempTime.getTime();
		second = TIMERCOUNT;
		currentLevel = 1;
		score = 0;
		lives = 3;
		hedgehog = new Hedgehog(11, 11, HUDHeight);
		grid[11][11].setHedgehog(hedgehog);
		setLevel();
	}
	
	public void changeLevel() {
		// Initialize entities
		grid = new Tile[23][23];
		for (int i = 0; i < 23; i++) {
			for (int j = 0; j < 23; j++) {
				grid[i][j] = new Tile(i, j);
				grid[i][j].setDebugActive(false);
			}
		}
		
		Date tempTime = new Date();
		previousTime = tempTime.getTime();
		second = TIMERCOUNT;
		hedgehog = new Hedgehog(11, 11, HUDHeight);
		grid[11][11].setHedgehog(hedgehog);
		setLevel();
	}
	
	public void getFPS() {
		fps = app.getFPS();
	}
	
	public static void main(String[] args) throws IOException {
		try
		{
			File scoreFile = new File("score.txt");
			scoreFile.createNewFile();
			
			// 23x23 grid with 26x26 pixel images = 598x598 resolution
			app = new AppGameContainer(new HedgehogHavoc("Hedgehog Havoc", 598, 648, 50));
			app.setDisplayMode(598, 648, false);
			app.setShowFPS(false);
			app.setVSync(true);
			app.start();
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}

	}

	
}
