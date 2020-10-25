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
class PlayingState extends BasicGameState {
	private int hedgehogX;
	private int hedgehogY;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		HedgehogHavoc hh = (HedgehogHavoc)game;
		setLevel(hh);
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		HedgehogHavoc hh = (HedgehogHavoc)game;
		hh.renderStats(g);
		for (int i = 0; i < 23; i++) {
			for (int j = 0; j < 23; j++) {
				hh.grid[i][j].render(g);
			}
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		HedgehogHavoc hh = (HedgehogHavoc)game;
		hh.getFPS();
		
		if (input.isKeyDown(Input.KEY_RIGHT) && hedgehogX < 22 && hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount <= 0) {
			if (hh.grid[hedgehogX + 1][hedgehogY].getIsBlockMovable()) {
				if (moveBlock(hh, hedgehogX, hedgehogY, "R")) {
					moveRight(hh);
				}
			} else if (hh.grid[hedgehogX + 1][hedgehogY].getIsGround()) {
				moveRight(hh);
			}
		} else if (input.isKeyDown(Input.KEY_LEFT) && hedgehogX > 0 && hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount <= 0) {
			if (hh.grid[hedgehogX - 1][hedgehogY].getIsBlockMovable()) {
				if (moveBlock(hh, hedgehogX, hedgehogY, "L")) {
					moveLeft(hh);
				}
			} else if (hh.grid[hedgehogX - 1][hedgehogY].getIsGround()) {
				moveLeft(hh);
			}
		} else if (input.isKeyDown(Input.KEY_UP) && hedgehogY > 0 && hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount <= 0) {
			if (hh.grid[hedgehogX][hedgehogY - 1].getIsBlockMovable()) {
				if (moveBlock(hh, hedgehogX, hedgehogY, "U")) {
					moveUp(hh);
				}
			} else if (hh.grid[hedgehogX][hedgehogY - 1].getIsGround()) {
				moveUp(hh);
			}
		} else if (input.isKeyDown(Input.KEY_DOWN) && hedgehogY < 22 && hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount <= 0) {
			if (hh.grid[hedgehogX][hedgehogY + 1].getIsBlockMovable()) {
				if (moveBlock(hh, hedgehogX, hedgehogY, "D")) {
					moveDown(hh);
				}
			} else if (hh.grid[hedgehogX][hedgehogY + 1].getIsGround()) {
				moveDown(hh);
			}
		}
		
		for (int i = 0; i < 23; i++) {
			for (int j = 0; j < 23; j++) {
				hh.grid[i][j].update(delta);
			}
		}
	}

	@Override
	public int getID() {
		return HedgehogHavoc.PLAYINGSTATE;
	}
	
	private void moveRight(HedgehogHavoc hh) {
		hh.grid[hedgehogX][hedgehogY].getHedgehog().addImage(ResourceManager.getImage(HedgehogHavoc.HEDGEHOGRIGHT_IMG));
		hh.grid[hedgehogX][hedgehogY].getHedgehog().removeImage(ResourceManager.getImage(HedgehogHavoc.HEDGEHOGLEFT_IMG));
		hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount = (int) hh.grid[hedgehogX][hedgehogY].getHedgehog().getCoarseGrainedWidth();
		hh.grid[hedgehogX][hedgehogY].getHedgehog().moveDir = "R";
		hh.grid[hedgehogX + 1][hedgehogY].setHedgehog(hh.grid[hedgehogX][hedgehogY].getHedgehog().clone(hh.grid[hedgehogX][hedgehogY].getHedgehog()));
		hh.grid[hedgehogX][hedgehogY].setHedgehog(null);
		hedgehogX += 1;
	}
	
	private void moveLeft(HedgehogHavoc hh) {
		hh.grid[hedgehogX][hedgehogY].getHedgehog().addImage(ResourceManager.getImage(HedgehogHavoc.HEDGEHOGLEFT_IMG));
		hh.grid[hedgehogX][hedgehogY].getHedgehog().removeImage(ResourceManager.getImage(HedgehogHavoc.HEDGEHOGRIGHT_IMG));
		hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount = (int) hh.grid[hedgehogX][hedgehogY].getHedgehog().getCoarseGrainedWidth();
		hh.grid[hedgehogX][hedgehogY].getHedgehog().moveDir = "L";
		hh.grid[hedgehogX - 1][hedgehogY].setHedgehog(hh.grid[hedgehogX][hedgehogY].getHedgehog().clone(hh.grid[hedgehogX][hedgehogY].getHedgehog()));
		hh.grid[hedgehogX][hedgehogY].setHedgehog(null);
		hedgehogX -= 1;
	}
	
	private void moveUp(HedgehogHavoc hh) {
		hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount = (int) hh.grid[hedgehogX][hedgehogY].getHedgehog().getCoarseGrainedHeight();
		hh.grid[hedgehogX][hedgehogY].getHedgehog().moveDir = "U";
		hh.grid[hedgehogX][hedgehogY - 1].setHedgehog(hh.grid[hedgehogX][hedgehogY].getHedgehog().clone(hh.grid[hedgehogX][hedgehogY].getHedgehog()));
		hh.grid[hedgehogX][hedgehogY].setHedgehog(null);
		hedgehogY -= 1;
	}
	
	private void moveDown(HedgehogHavoc hh) {
		hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount = (int) hh.grid[hedgehogX][hedgehogY].getHedgehog().getCoarseGrainedHeight();
		hh.grid[hedgehogX][hedgehogY].getHedgehog().moveDir = "D";
		hh.grid[hedgehogX][hedgehogY + 1].setHedgehog(hh.grid[hedgehogX][hedgehogY].getHedgehog().clone(hh.grid[hedgehogX][hedgehogY].getHedgehog()));
		hh.grid[hedgehogX][hedgehogY].setHedgehog(null);
		hedgehogY += 1;
	}
	
	private boolean moveBlock(HedgehogHavoc hh, int x, int y, String direction) {
		if (direction.equals("R")) {
			if (x > 21) return false;
			x++;
		} else if (direction.equals("L")) {
			if (x < 1) return false;
			x--;
		} else if (direction.equals("U")) {
			if (y < 1) return false;
			y--;
		} else if (direction.equals("D")) {
			if (y > 21) return false;
			y++;
		}
		
		if (hh.grid[x][y].getIsBlockMovable()) {
			if (moveBlock(hh, x, y, direction)) {				
				if (direction.equals("R")) {
					hh.grid[x + 1][y].setBlockMovable(hh.grid[x][y].getBlockMovable().clone(hh.grid[x][y].getBlockMovable()));
					hh.grid[x + 1][y].getBlockMovable().moveDir = "R";
					hh.grid[x + 1][y].getBlockMovable().moveCount = (int) hh.grid[x][y].getBlockMovable().getCoarseGrainedWidth();
				} else if (direction.equals("L")) {
					hh.grid[x - 1][y].setBlockMovable(hh.grid[x][y].getBlockMovable().clone(hh.grid[x][y].getBlockMovable()));
					hh.grid[x - 1][y].getBlockMovable().moveDir = "L";
					hh.grid[x - 1][y].getBlockMovable().moveCount = (int) hh.grid[x][y].getBlockMovable().getCoarseGrainedWidth();
				} else if (direction.equals("U")) {
					hh.grid[x][y - 1].setBlockMovable(hh.grid[x][y].getBlockMovable().clone(hh.grid[x][y].getBlockMovable()));
					hh.grid[x][y - 1].getBlockMovable().moveDir = "U";
					hh.grid[x][y - 1].getBlockMovable().moveCount = (int) hh.grid[x][y].getBlockMovable().getCoarseGrainedHeight();
				} else if (direction.equals("D")) {
					hh.grid[x][y + 1].setBlockMovable(hh.grid[x][y].getBlockMovable().clone(hh.grid[x][y].getBlockMovable()));
					hh.grid[x][y + 1].getBlockMovable().moveDir = "D";
					hh.grid[x][y + 1].getBlockMovable().moveCount = (int) hh.grid[x][y].getBlockMovable().getCoarseGrainedHeight();
				}

				hh.grid[x][y].setBlockMovable(null);
				return true;
			} else {
				return false;
			}
		} else if (hh.grid[x][y].getIsGround()) {
			return true;
		} else {
			return false;
		}
	}
	
	private void setLevel(HedgehogHavoc hh) {
		if (hh.currentLevel == 1) {
			for (int i = 4; i < 19; i++) {
				for (int j = 4; j < 19; j++) {
					// Making sure we're not overwriting the hedgehog with this if-statement.
					if (i != 11 || j != 11) {
						hh.grid[i][j].setBlockMovable(new BlockMovable(i, j));
					}
				}
			}
			
			hedgehogX = 11;
			hedgehogY = 11;
		}
	}
}