import java.io.IOException;

import jig.Entity;

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

	public final int ScreenWidth;
	public final int ScreenHeight;
	int fps;
	
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
			app = new AppGameContainer(new HedgehogHavoc("Hedgehog Havoc", 800, 600));
			app.setDisplayMode(800, 600, false);
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
