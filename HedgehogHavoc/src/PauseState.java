import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.ResourceManager;

/**
 * This state is active when the game is being played.
 * 
 * Transitions From (Initialization)
 * 
 * Transitions To PlayingState
 */
class PauseState extends BasicGameState {
	private int pauseTimer;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		pauseTimer = 10;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.setColor(Color.white);
		g.drawImage(ResourceManager.getImage(HedgehogHavoc.PAUSEBACKGROUND_IMG), 0, 0);
		g.drawImage(
				ResourceManager.getImage(HedgehogHavoc.LOGO_IMG),
				0,
				ResourceManager.getImage(HedgehogHavoc.LOGO_IMG).getHeight());
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		HedgehogHavoc hh = (HedgehogHavoc)game;
				
		hh.getFPS();
		
		if (input.isKeyDown(Input.KEY_ESCAPE) && pauseTimer <= 0) {
			game.enterState(HedgehogHavoc.PLAYINGSTATE);
		}
		
		if (pauseTimer > 0) {
			pauseTimer -= 1;
		}
	}

	@Override
	public int getID() {
		return HedgehogHavoc.PAUSESTATE;
	}
}