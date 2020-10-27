import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
class GameOverState extends BasicGameState {
	private int pauseTimer;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		HedgehogHavoc hh = (HedgehogHavoc)game;
		pauseTimer = 50;
		
		if (hh.score > hh.highScore) {
			hh.highScore = hh.score;
			File oldScore = new File("score.txt");
			oldScore.delete();
			File newScore = new File("score.txt");
			try {
				FileWriter fileWriter = new FileWriter(newScore, false);
				fileWriter.write(Integer.toString(hh.highScore));
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		HedgehogHavoc hh = (HedgehogHavoc)game;
		g.setColor(Color.white);
		g.drawImage(ResourceManager.getImage(HedgehogHavoc.GAMEOVERSCREEN_IMG), 0, 0);
		g.drawImage(
				ResourceManager.getImage(HedgehogHavoc.LOGO_IMG),
				(hh.ScreenWidth / 2) - (ResourceManager.getImage(HedgehogHavoc.LOGO_IMG).getWidth() / 2),
				ResourceManager.getImage(HedgehogHavoc.LOGO_IMG).getHeight());
		
		Font font = g.getFont();
		int fontHeight = font.getHeight("test");
		g.drawString(
				"Your Score: " + hh.score,
				(hh.ScreenWidth / 2) - (font.getWidth("Your Score: " + hh.score) / 2),
				(hh.ScreenHeight / 2) - fontHeight);
		g.drawString(
				"High Score: " + hh.highScore,
				(hh.ScreenWidth / 2) - (font.getWidth("High Score: " + hh.highScore) / 2),
				(hh.ScreenHeight / 2) + fontHeight);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		HedgehogHavoc hh = (HedgehogHavoc)game;
				
		hh.getFPS();
		
		if (input.isKeyDown(Input.KEY_ENTER) && pauseTimer <= 0) {
			hh.score = 0;
			game.enterState(HedgehogHavoc.PLAYINGSTATE);
		}
		
		if (pauseTimer > 0) {
			pauseTimer -= 1;
		}
	}

	@Override
	public int getID() {
		return HedgehogHavoc.GAMEOVERSTATE;
	}
}