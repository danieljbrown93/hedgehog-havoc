import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.Vector;


/**
 * This state is active when the game is being played.
 * 
 * Transitions From (Initialization)
 * 
 * Transitions To PlayingState
 */
class PlayingState extends BasicGameState {
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		HedgehogHavoc hh = (HedgehogHavoc)game;
		hh.renderStats(g);
		hh.hedgehog.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		HedgehogHavoc hh = (HedgehogHavoc)game;
		hh.getFPS();
		
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			hh.hedgehog.setVelocity(new Vector(0.5f, 0f));
		} else if (input.isKeyDown(Input.KEY_LEFT)) {
			hh.hedgehog.setVelocity(new Vector(-0.5f, 0f));
		} else if (input.isKeyDown(Input.KEY_UP)) {
			hh.hedgehog.setVelocity(new Vector(0f, -0.5f));
		} else if (input.isKeyDown(Input.KEY_DOWN)) {
			hh.hedgehog.setVelocity(new Vector(0f, 0.5f));
		} else {
			hh.hedgehog.setVelocity(new Vector(0f, 0f));
		}
		
		hh.hedgehog.update(delta);
	}

	@Override
	public int getID() {
		return HedgehogHavoc.PLAYINGSTATE;
	}
}