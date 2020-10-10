import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.ResourceManager;
import jig.Vector;


/**
 * This state is active when the game is being played.
 * 
 * Transitions From (Initialization)
 * 
 * Transitions To PlayingState
 */
class PlayingState extends BasicGameState {
	boolean hedgehogMoved;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		hedgehogMoved = false;
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
		updatePositions(hh, delta);
		
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			hh.hedgehog.setVelocity(new Vector(0.25f, 0f));
			hedgehogMoved = true;
		} else if (input.isKeyDown(Input.KEY_LEFT)) {
			hh.hedgehog.setVelocity(new Vector(-0.25f, 0f));
			hedgehogMoved = true;
		} else if (input.isKeyDown(Input.KEY_UP)) {
			hh.hedgehog.setVelocity(new Vector(0f, -0.25f));
			hedgehogMoved = true;
		} else if (input.isKeyDown(Input.KEY_DOWN)) {
			hh.hedgehog.setVelocity(new Vector(0f, 0.25f));
			hedgehogMoved = true;
		} else if (hedgehogMoved) {
			stopHedgehog(hh);
		}
	}

	@Override
	public int getID() {
		return HedgehogHavoc.PLAYINGSTATE;
	}
	
	private void updatePositions(HedgehogHavoc hh, int delta) {
		float widthSegment = hh.ScreenWidth / 23;
		float heightSegment = hh.ScreenHeight / 23;
		
		// Update hedgehog position.
		hh.hedgehog.update(delta);
		for (int i = 0; i < 23; i++) {
			for (int j = 0; j < 23; j++) {
				if (hh.hedgehog.getX() >= widthSegment * i && hh.hedgehog.getX() <= widthSegment * (i + 1)) {
					if (hh.hedgehog.getY() >= heightSegment * j && hh.hedgehog.getY() <= heightSegment * (j + 1)) {
						hh.grid.get(hh.hedgehog.gridX).set(hh.hedgehog.gridY, "G");
						hh.grid.get(i).set(j, "H");
						hh.hedgehog.gridX = i;
						hh.hedgehog.gridY = j;
					}
				}
			}
		}
		
		// Update badger positions.
		// TODO
		
		// Update block positions.
		// TODO
	}
	
	private void stopHedgehog(HedgehogHavoc hh) {
		float widthSegment = hh.ScreenWidth / 23;
		float heightSegment = hh.ScreenHeight / 23;
		Vector hogVelocity = hh.hedgehog.getVelocity();
		if (hogVelocity.getX() != 0) {
			// Moving horizontally.
			if (hh.hedgehog.getCoarseGrainedMinX() <= widthSegment * hh.hedgehog.gridX) {
				hh.hedgehog.setVelocity(new Vector(0f, 0f));
				hh.hedgehog.setX((widthSegment * hh.hedgehog.gridX) + (ResourceManager.getImage(HedgehogHavoc.HEDGEHOG_IMG).getWidth() / 2));
			}
		} else if (hogVelocity.getY() != 0) {
			// Moving vertically.
			if (hh.hedgehog.getCoarseGrainedMinY() <= heightSegment * hh.hedgehog.gridY) {
				hh.hedgehog.setVelocity(new Vector(0f, 0f));
				hh.hedgehog.setY((heightSegment * hh.hedgehog.gridY) + (ResourceManager.getImage(HedgehogHavoc.HEDGEHOG_IMG).getHeight() / 2));
			}
		}
	}
}