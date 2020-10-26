import java.io.IOException;
import java.util.Date;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
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
	
	// Resources
	public static final String BACKGROUND_IMG = "resource/background.png";
	public static final String HUDBACKGROUND_IMG = "resource/hudbackground.png";
	public static final String HEDGEHOGRIGHT_IMG = "resource/hedgehog_right.png";
	public static final String HEDGEHOGLEFT_IMG = "resource/hedgehog_left.png";
	public static final String BADGERRIGHT_IMG = "resource/badger_right.png";
	public static final String BADGERLEFT_IMG = "resource/badger_left.png";
	public static final String BLOCK_MOVABLE_IMG = "resource/block_movable.png";
	public static final String BLOCK_IMMOVABLE_IMG = "resource/block_immovable.png";
	public static final String BUG_IMG = "resource/bug.png";
	public static final String PATH_DEBUG_IMG = "resource/path_debug.png";
	public static final boolean debug = true;

	public final int ScreenWidth;
	public final int ScreenHeight;
	public final int HUDHeight;
	
	int fps;
	int score;
	int lives;
	int currentLevel;
	int second;
	long previousTime;
	
	Tile[][] grid;
	Hedgehog hedgehog;
	
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
		
		// Pre-loading all image resources
		ResourceManager.loadImage(BACKGROUND_IMG);
		ResourceManager.loadImage(HUDBACKGROUND_IMG);
		ResourceManager.loadImage(HEDGEHOGLEFT_IMG);
		ResourceManager.loadImage(HEDGEHOGRIGHT_IMG);
		ResourceManager.loadImage(BADGERLEFT_IMG);
		ResourceManager.loadImage(BADGERRIGHT_IMG);
		ResourceManager.loadImage(BLOCK_MOVABLE_IMG);
		ResourceManager.loadImage(BLOCK_IMMOVABLE_IMG);
		ResourceManager.loadImage(BUG_IMG);
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
		second = 60;
		currentLevel = 1;
		score = 0;
		lives = 3;
		hedgehog = new Hedgehog(11, 11, HUDHeight);
		grid[11][11].setHedgehog(hedgehog);
		grid[11][11].setIsHedgehog(true);
	}
	
	public void renderStats(Graphics g) {
		Date tempTime = new Date();
		int tempSecond = (int) ((tempTime.getTime() - previousTime) / 1000);
		if (tempSecond >= 1) {
			second -= 1;
			previousTime = tempTime.getTime();
		}
		
		g.drawImage(ResourceManager.getImage(HUDBACKGROUND_IMG), 0, 0);
		g.setColor(Color.black);
		g.drawString("FPS: " + fps, 10, 10);
		g.drawString("Score: " + score, 100, 10);
		g.drawString("Timer: " + String.format("%02d", second), 300, 10);
		g.drawString("Lives: " + lives, 500, 10);
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
		second = 60;
		currentLevel = 1;
		score = 0;
		lives = 3;
		hedgehog = new Hedgehog(11, 11, HUDHeight);
		grid[11][11].setHedgehog(hedgehog);
		grid[11][11].setIsHedgehog(true);
	}
	
	public void getFPS() {
		fps = app.getFPS();
	}
	
	public static void main(String[] args) throws IOException {
		try
		{
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
