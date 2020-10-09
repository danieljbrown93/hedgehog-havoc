import java.io.IOException;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
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
	public static final String HEDGEHOG_IMG = "resource/hedgehog.png";
	public static final String BADGER_IMG = "resource/badger.png";
	public static final String BLOCK_MOVABLE_IMG = "resource/block_movable.png";
	public static final String BLOCK_IMMOVABLE_IMG = "resource/block_immovable.png";
	public static final String BUG_IMG = "resource/bug.png";

	public final int ScreenWidth;
	public final int ScreenHeight;
	int fps;
	
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
		ResourceManager.loadImage(HEDGEHOG_IMG);
		ResourceManager.loadImage(BADGER_IMG);
		ResourceManager.loadImage(BLOCK_MOVABLE_IMG);
		ResourceManager.loadImage(BLOCK_IMMOVABLE_IMG);
		ResourceManager.loadImage(BUG_IMG);
		
		// Initialize entities
		hedgehog = new Hedgehog(ScreenWidth / 2, ScreenHeight / 2);
	}
	
	public void renderStats(Graphics g) {
		g.drawString("FPS: " + fps, 10, 10);
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
