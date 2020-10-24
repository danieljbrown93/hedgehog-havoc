import java.io.IOException;
import java.util.ArrayList;

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
	public static final String HEDGEHOGRIGHT_IMG = "resource/hedgehog_right.png";
	public static final String HEDGEHOGLEFT_IMG = "resource/hedgehog_left.png";
	public static final String BADGER_IMG = "resource/badger.png";
	public static final String BLOCK_MOVABLE_IMG = "resource/block_movable.png";
	public static final String BLOCK_IMMOVABLE_IMG = "resource/block_immovable.png";
	public static final String BUG_IMG = "resource/bug.png";

	public final int ScreenWidth;
	public final int ScreenHeight;
	
	int fps;
	int currentLevel;
	
	ArrayList<ArrayList<Tile>> grid;
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
	public HedgehogHavoc(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;

		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
	}


	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new PlayingState());
		
		// Pre-loading all image resources
		ResourceManager.loadImage(BACKGROUND_IMG);
		ResourceManager.loadImage(HEDGEHOGLEFT_IMG);
		ResourceManager.loadImage(HEDGEHOGRIGHT_IMG);
		ResourceManager.loadImage(BADGER_IMG);
		ResourceManager.loadImage(BLOCK_MOVABLE_IMG);
		ResourceManager.loadImage(BLOCK_IMMOVABLE_IMG);
		ResourceManager.loadImage(BUG_IMG);
		
		// Initialize entities
		grid = new ArrayList<ArrayList<Tile>>(23);
		for (int i = 0; i < 23; i++) {
			ArrayList<Tile> newList = new ArrayList<Tile>(23);
			for (int j = 0; j < 23; j++) {
				Tile newTile = new Tile(i, j);
				newList.add(newTile);
			}
			
			grid.add(newList);
		}
		
		currentLevel = 1;
		hedgehog = new Hedgehog(11, 11);
		grid.get(11).get(11).setHedgehog(hedgehog);
		grid.get(11).get(11).setIsHedgehog(true);
	}
	
	public void renderStats(Graphics g) {
		g.setColor(Color.black);
		g.drawImage(ResourceManager.getImage(HedgehogHavoc.BACKGROUND_IMG), 0, 0);
		g.drawString("FPS: " + fps, 10, 10);
	}
	
	public void renderLevel(Graphics g) {
		if (currentLevel == 1) {
			for (int i = 4; i < 19; i++) {
				for (int j = 4; j < 19; j++) {
					// Making sure we're not overwriting the hedgehog with this if-statement.
					if (i != 11 || j != 11) {
						grid.get(i).get(j).setBlockMovable(new BlockMovable(i, j));
						grid.get(i).get(j).setIsBlockMovable(true);
						grid.get(i).get(j).getBlockMovable().render(g);
					}
				}
			}
		}
	}
	
	public void refreshLevel(Graphics g) {
		for (int i = 0; i < 23; i++) {
			for (int j = 0; j < 23; j++) {
				if (grid.get(i).get(j).getIsBlockMovable()) {
					grid.get(i).get(j).getBlockMovable().render(g);
				}
			}
		}
	}
	
	public void getFPS() {
		fps = app.getFPS();
	}
	
	public static void main(String[] args) throws IOException {
		try
		{
			// 23x23 grid with 26x26 pixel images = 598x598 resolution
			app = new AppGameContainer(new HedgehogHavoc("Hedgehog Havoc", 598, 598));
			app.setDisplayMode(598, 598, false);
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
