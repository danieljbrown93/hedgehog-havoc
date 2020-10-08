import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


/**
 * This state is active when the game is being played.
 * 
 * Transitions From (Initialization)
 * 
 * Transitions To PlayingState
 */
class PlayingState extends BasicGameState
{
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException
	{
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
	{
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException
	{
		HedgehogHavoc hh = (HedgehogHavoc)game;
		hh.renderStats(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException
	{
		HedgehogHavoc hh = (HedgehogHavoc)game;
		hh.getFPS();
	}

	@Override
	public int getID()
	{
		return HedgehogHavoc.PLAYINGSTATE;
	}
}