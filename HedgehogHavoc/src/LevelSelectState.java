import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
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
class LevelSelectState extends BasicGameState {
	private int pauseTimer;
	private String levelNumber;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		pauseTimer = 10;
		levelNumber = "_";
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		HedgehogHavoc hh = (HedgehogHavoc)game;
		g.setColor(Color.black);
		g.drawImage(ResourceManager.getImage(HedgehogHavoc.LEVELSELECTBACKGROUND_IMG), 0, 0);
		g.drawImage(ResourceManager.getImage(HedgehogHavoc.LOGO_IMG), 0, 30);
		
		
		Font font = g.getFont();
		int fontHeight = font.getHeight(levelNumber);
		int fontWidth = font.getWidth(levelNumber);
		
		g.drawString(
				levelNumber,
				(hh.ScreenWidth / 2) - (fontWidth / 2),
				(hh.ScreenHeight / 2) - (fontHeight / 2));
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		HedgehogHavoc hh = (HedgehogHavoc)game;
				
		hh.getFPS();
		
		if (input.isKeyDown(Input.KEY_ESCAPE) && pauseTimer <= 0) {
			hh.confirmLevel = false;
			game.enterState(HedgehogHavoc.PLAYINGSTATE);
		} else if (input.isKeyDown(Input.KEY_1) && pauseTimer <= 0) {
			levelNumber = "1";
			pauseTimer = 10;
		} else if (input.isKeyDown(Input.KEY_2) && pauseTimer <= 0) {
			levelNumber = "2";
			pauseTimer = 10;
		} else if (input.isKeyDown(Input.KEY_3) && pauseTimer <= 0) {
			levelNumber = "3";
			pauseTimer = 10;
		} else if (input.isKeyDown(Input.KEY_4) && pauseTimer <= 0) {
			levelNumber = "4";
			pauseTimer = 10;
		} else if (input.isKeyDown(Input.KEY_5) && pauseTimer <= 0) {
			levelNumber = "5";
			pauseTimer = 10;
		} else if ((input.isKeyDown(Input.KEY_DELETE) || input.isKeyDown(Input.KEY_BACK)) && pauseTimer <= 0) {
			levelNumber = "_";
			pauseTimer = 10;
		} else if (input.isKeyDown(Input.KEY_ENTER) && pauseTimer <= 0 && !levelNumber.equals("_")) {
			hh.currentLevel = Integer.parseInt(levelNumber);
			hh.confirmLevel = true;
			game.enterState(HedgehogHavoc.PLAYINGSTATE);
		}
		
		if (pauseTimer > 0) {
			pauseTimer -= 1;
		}
	}

	@Override
	public int getID() {
		return HedgehogHavoc.LEVELSELECTSTATE;
	}
}