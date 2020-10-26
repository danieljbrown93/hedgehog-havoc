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
	private Pathfinding pathFinder;
	private int pauseTimer;
	private int hedgehogX;
	private int hedgehogY;
	private int badgerCount;
	private int caughtBadgers;
	private int remainingBadgers;
	private boolean hedgehogMoved;
	private boolean hedgehogStuck;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		HedgehogHavoc hh = (HedgehogHavoc)game;
		hh.setLevel();
		hedgehogX = 11;
		hedgehogY = 11;
		
		if (hh.currentLevel == 1) badgerCount = 1;
		if (hh.currentLevel == 2) badgerCount = 2;
		
		caughtBadgers = 0;
		remainingBadgers = HedgehogHavoc.BADGERCOUNT;
		pathFinder = new Pathfinding();
		hedgehogMoved = false;
		hedgehogStuck = false;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		pauseTimer = 10;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		HedgehogHavoc hh = (HedgehogHavoc)game;
		g.drawImage(ResourceManager.getImage(HedgehogHavoc.BACKGROUND_IMG), 0, hh.HUDHeight);
		for (int i = 0; i < 23; i++) {
			for (int j = 0; j < 23; j++) {
				hh.grid[i][j].render(g);
			}
		}
		
		hh.renderStats(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		HedgehogHavoc hh = (HedgehogHavoc)game;
		
		if (hh.lives <= 0) {
			hh.restartGame();
			remainingBadgers = HedgehogHavoc.BADGERCOUNT;
			caughtBadgers = 0;
			badgerCount = countBadgers(hh.grid);
			hedgehogX = 11;
			hedgehogY = 11;
			game.enterState(HedgehogHavoc.PLAYINGSTATE);
		}
		
		if (remainingBadgers <= 0) {
			changeLevel(hh, game, hh.currentLevel + 1);
			return;
		}
		
		if (hedgehogStuck && hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount <= 0) {
			hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount = 300;
			hh.grid[hedgehogX][hedgehogY].getHedgehog().moveDir = "";
			hedgehogStuck = false;
		}
		
		hh.getFPS();
		
		if (input.isKeyDown(Input.KEY_RIGHT) && hedgehogX < 22 && hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount <= 0) {
			checkMoveRight(hh);
		} else if (input.isKeyDown(Input.KEY_LEFT) && hedgehogX > 0 && hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount <= 0) {
			checkMoveLeft(hh);
		} else if (input.isKeyDown(Input.KEY_UP) && hedgehogY > 0 && hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount <= 0) {
			checkMoveUp(hh);
		} else if (input.isKeyDown(Input.KEY_DOWN) && hedgehogY < 22 && hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount <= 0) {
			checkMoveDown(hh);
		} else if (input.isKeyDown(Input.KEY_ESCAPE) && pauseTimer <= 0) {
			pauseTimer = 10;
			game.enterState(HedgehogHavoc.PAUSESTATE);
			return;
		} else if (input.isKeyDown(Input.KEY_1)) {
			changeLevel(hh, game, 1);
			return;
		} else if (input.isKeyDown(Input.KEY_2)) {
			changeLevel(hh, game, 2);
			return;
		} else if (input.isKeyDown(Input.KEY_3)) {
			changeLevel(hh, game, 3);
			return;
		} else if (input.isKeyDown(Input.KEY_4)) {
			changeLevel(hh, game, 4);
			return;
		} else if (input.isKeyDown(Input.KEY_5)) {
			changeLevel(hh, game, 5);
			return;
		}
		
		for (int i = 0; i < 23; i++) {
			for (int j = 0; j < 23; j++) {
				if (hh.grid[i][j].getIsBadger() && hh.grid[i][j].getBadger().moveCount <= 0) {
					if (hedgehogMoved || hh.grid[i][j].getBadger().path.isEmpty()) {
						hh.grid[i][j].getBadger().path = pathFinder.BFS(hh.grid, i, j);
						hedgehogMoved = false;
					}
					
					if (hh.grid[i][j].getBadger().path.isEmpty()) {
						pathToHedgehog(hh, i, j);
					} else {
						moveBadger(hh, i, j);
					}
				}
				
				hh.grid[i][j].update(delta);
			}
		}
		
		if (hh.second <= 0 || badgerCount <= 0) {
			if (hh.second <= 0) {
				hh.score -= 10; // Penalty for letting timer run out.
				if (hh.score < 0) {
					hh.score = 0;
				}
			}
			
			hh.second = HedgehogHavoc.TIMERCOUNT;
			
			if (badgerCount < HedgehogHavoc.MAXBADGERS) spawnBadger(hh);
		}
		
		if (pauseTimer > 0) {
			pauseTimer -= 1;
		}
	}
	
	private void changeLevel(HedgehogHavoc hh, StateBasedGame game, int level) {
		hh.currentLevel = level;
		remainingBadgers = HedgehogHavoc.BADGERCOUNT;
		badgerCount = countBadgers(hh.grid);
		caughtBadgers = 0;
		hh.changeLevel();
		hedgehogX = 11;
		hedgehogY = 11;
		hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount = 10;
		game.enterState(HedgehogHavoc.PLAYINGSTATE);
	}
	
	private int countBadgers(Tile[][] grid) {
		int num = 0;
		for (int i = 0; i < 23; i++) {
			for (int j = 0; j < 23; j++) {
				if (grid[i][j].getIsBadger()) num++;
			}
		}
		
		return num;
	}
	
	private void respawnHedgehog(HedgehogHavoc hh) {
		for (int j = 11; j < 23; j++) {
			for (int i = 11; i < 23; i++) {
				if (hh.grid[i][j].getIsGround()) {
					// Checking to make sure we're not respawning directly next to a badger.
					if (!hh.grid[i - 1][j].getIsBadger() &&
							!hh.grid[i + 1][j].getIsBadger() &&
							!hh.grid[i][j - 1].getIsBadger() &&
							!hh.grid[i][j + 1].getIsBadger() &&
							!hh.grid[i - 1][j - 1].getIsBadger() &&
							!hh.grid[i - 1][j + 1].getIsBadger() &&
							!hh.grid[i + 1][j - 1].getIsBadger() &&
							!hh.grid[i + 1][j + 1].getIsBadger()) {
						hh.grid[hedgehogX][hedgehogY].setHedgehog(null);
						hh.grid[i][j].setHedgehog(new Hedgehog(i, j, hh.HUDHeight));
						hedgehogX = i;
						hedgehogY = j;
						return;
					}
				}
			}
		}
		
		for (int j = 10; j > 0; j--) {
			for (int i = 10; i > 0; i--) {
				if (hh.grid[i][j].getIsGround()) {
					// Checking to make sure we're not respawning directly next to a badger.
					if (!hh.grid[i - 1][j].getIsBadger() &&
							!hh.grid[i + 1][j].getIsBadger() &&
							!hh.grid[i][j - 1].getIsBadger() &&
							!hh.grid[i][j + 1].getIsBadger() &&
							!hh.grid[i - 1][j - 1].getIsBadger() &&
							!hh.grid[i - 1][j + 1].getIsBadger() &&
							!hh.grid[i + 1][j - 1].getIsBadger() &&
							!hh.grid[i + 1][j + 1].getIsBadger()) {
						hh.grid[hedgehogX][hedgehogY].setHedgehog(null);
						hh.grid[i][j].setHedgehog(new Hedgehog(i, j, hh.HUDHeight));
						hedgehogX = i;
						hedgehogY = j;
						return;
					}
				}
			}
		}
	}
	
	private void checkMoveRight(HedgehogHavoc hh) {
		if (hh.grid[hedgehogX + 1][hedgehogY].getIsBlockMovable()) {
			if (moveBlock(hh, hedgehogX, hedgehogY, "R")) {
				moveRight(hh);
			}
		} else if (hh.grid[hedgehogX + 1][hedgehogY].getIsGround()) {
			moveRight(hh);
		} else if (hh.grid[hedgehogX + 1][hedgehogY].getIsBug()) {
			hh.score += 100;
			moveRight(hh);
		} else if (hh.grid[hedgehogX + 1][hedgehogY].getIsBadger()) {
			hh.lives -= 1;
			respawnHedgehog(hh);
		} else if (hh.grid[hedgehogX + 1][hedgehogY].getIsHole()) {
			hh.grid[hedgehogX + 1][hedgehogY].setHole(null);
			hedgehogStuck = true;
			moveRight(hh);
		}
	}
	
	private void checkMoveLeft(HedgehogHavoc hh) {
		if (hh.grid[hedgehogX - 1][hedgehogY].getIsBlockMovable()) {
			if (moveBlock(hh, hedgehogX, hedgehogY, "L")) {
				moveLeft(hh);
			}
		} else if (hh.grid[hedgehogX - 1][hedgehogY].getIsGround()) {
			moveLeft(hh);
		} else if (hh.grid[hedgehogX - 1][hedgehogY].getIsBug()) {
			hh.score += 100;
			moveLeft(hh);
		} else if (hh.grid[hedgehogX - 1][hedgehogY].getIsBadger()) {
			hh.lives -= 1;
			respawnHedgehog(hh);
		} else if (hh.grid[hedgehogX - 1][hedgehogY].getIsHole()) {
			hh.grid[hedgehogX - 1][hedgehogY].setHole(null);
			hedgehogStuck = true;
			moveLeft(hh);
		}
	}
	
	private void checkMoveUp(HedgehogHavoc hh) {
		if (hh.grid[hedgehogX][hedgehogY - 1].getIsBlockMovable()) {
			if (moveBlock(hh, hedgehogX, hedgehogY, "U")) {
				moveUp(hh);
			}
		} else if (hh.grid[hedgehogX][hedgehogY - 1].getIsGround()) {
			moveUp(hh);
		} else if (hh.grid[hedgehogX][hedgehogY - 1].getIsBug()) {
			hh.score += 100;
			moveUp(hh);
		} else if (hh.grid[hedgehogX][hedgehogY - 1].getIsBadger()) {
			hh.lives -= 1;
			respawnHedgehog(hh);
		} else if (hh.grid[hedgehogX][hedgehogY - 1].getIsHole()) {
			hh.grid[hedgehogX][hedgehogY - 1].setHole(null);
			hedgehogStuck = true;
			moveUp(hh);
		}
	}
	
	private void checkMoveDown(HedgehogHavoc hh) {
		if (hh.grid[hedgehogX][hedgehogY + 1].getIsBlockMovable()) {
			if (moveBlock(hh, hedgehogX, hedgehogY, "D")) {
				moveDown(hh);
			}
		} else if (hh.grid[hedgehogX][hedgehogY + 1].getIsGround()) {
			moveDown(hh);
		} else if (hh.grid[hedgehogX][hedgehogY + 1].getIsBug()) {
			hh.score += 100;
			moveDown(hh);
		} else if (hh.grid[hedgehogX][hedgehogY + 1].getIsBadger()) {
			hh.lives -= 1;
			respawnHedgehog(hh);
		} else if (hh.grid[hedgehogX][hedgehogY + 1].getIsHole()) {
			hh.grid[hedgehogX][hedgehogY + 1].setHole(null);
			hedgehogStuck = true;
			moveDown(hh);
		}
	}
	
	private void moveRight(HedgehogHavoc hh) {
		hh.grid[hedgehogX][hedgehogY].getHedgehog().addImageWithBoundingBox(ResourceManager.getImage(HedgehogHavoc.HEDGEHOGRIGHT_IMG));
		hh.grid[hedgehogX][hedgehogY].getHedgehog().removeImage(ResourceManager.getImage(HedgehogHavoc.HEDGEHOGLEFT_IMG));
		hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount = (int) hh.grid[hedgehogX][hedgehogY].getHedgehog().getCoarseGrainedWidth();
		hh.grid[hedgehogX][hedgehogY].getHedgehog().moveDir = "R";
		hh.grid[hedgehogX + 1][hedgehogY].setHedgehog(hh.grid[hedgehogX][hedgehogY].getHedgehog().clone(hh.grid[hedgehogX][hedgehogY].getHedgehog()));
		hh.grid[hedgehogX][hedgehogY].setHedgehog(null);
		hedgehogX += 1;
		hedgehogMoved = true;
	}
	
	private void moveLeft(HedgehogHavoc hh) {
		hh.grid[hedgehogX][hedgehogY].getHedgehog().addImage(ResourceManager.getImage(HedgehogHavoc.HEDGEHOGLEFT_IMG));
		hh.grid[hedgehogX][hedgehogY].getHedgehog().removeImage(ResourceManager.getImage(HedgehogHavoc.HEDGEHOGRIGHT_IMG));
		hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount = (int) hh.grid[hedgehogX][hedgehogY].getHedgehog().getCoarseGrainedWidth();
		hh.grid[hedgehogX][hedgehogY].getHedgehog().moveDir = "L";
		hh.grid[hedgehogX - 1][hedgehogY].setHedgehog(hh.grid[hedgehogX][hedgehogY].getHedgehog().clone(hh.grid[hedgehogX][hedgehogY].getHedgehog()));
		hh.grid[hedgehogX][hedgehogY].setHedgehog(null);
		hedgehogX -= 1;
		hedgehogMoved = true;
	}
	
	private void moveUp(HedgehogHavoc hh) {
		hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount = (int) hh.grid[hedgehogX][hedgehogY].getHedgehog().getCoarseGrainedHeight();
		hh.grid[hedgehogX][hedgehogY].getHedgehog().moveDir = "U";
		hh.grid[hedgehogX][hedgehogY - 1].setHedgehog(hh.grid[hedgehogX][hedgehogY].getHedgehog().clone(hh.grid[hedgehogX][hedgehogY].getHedgehog()));
		hh.grid[hedgehogX][hedgehogY].setHedgehog(null);
		hedgehogY -= 1;
		hedgehogMoved = true;
	}
	
	private void moveDown(HedgehogHavoc hh) {
		hh.grid[hedgehogX][hedgehogY].getHedgehog().moveCount = (int) hh.grid[hedgehogX][hedgehogY].getHedgehog().getCoarseGrainedHeight();
		hh.grid[hedgehogX][hedgehogY].getHedgehog().moveDir = "D";
		hh.grid[hedgehogX][hedgehogY + 1].setHedgehog(hh.grid[hedgehogX][hedgehogY].getHedgehog().clone(hh.grid[hedgehogX][hedgehogY].getHedgehog()));
		hh.grid[hedgehogX][hedgehogY].setHedgehog(null);
		hedgehogY += 1;
		hedgehogMoved = true;
	}
	
	private void moveBadger(HedgehogHavoc hh, int x, int y) {
		Tile path = hh.grid[x][y].getBadger().path.remove(0);
		if (path.getXPos() > x) {
			moveBadgerRight(hh, x, y);
		} else if (path.getXPos() < x) {
			moveBadgerLeft(hh, x, y);
		} else if (path.getYPos() < y) {
			moveBadgerUp(hh, x, y);
		} else if (path.getYPos() > y) {
			moveBadgerDown(hh, x, y);
		}
	}
	
	private void pathToHedgehog(HedgehogHavoc hh, int badgerX, int badgerY) {
		boolean canMoveLeft = badgerX > 0 && (hh.grid[badgerX - 1][badgerY].getIsGround() || hh.grid[badgerX - 1][badgerY].getIsHedgehog());
		boolean canMoveRight = badgerX < 22 && (hh.grid[badgerX + 1][badgerY].getIsGround() || hh.grid[badgerX + 1][badgerY].getIsHedgehog());
		boolean canMoveUp = badgerY > 0 && (hh.grid[badgerX][badgerY - 1].getIsGround() || hh.grid[badgerX][badgerY - 1].getIsHedgehog());
		boolean canMoveDown = badgerY < 22 && (hh.grid[badgerX][badgerY + 1].getIsGround() || hh.grid[badgerX][badgerY + 1].getIsHedgehog());
		
		if (badgerX == hedgehogX && badgerY != hedgehogY) {
			if (badgerY < hedgehogY && canMoveDown) {
				// Move down.
				moveBadgerDown(hh, badgerX, badgerY);
				return;
			} else if (badgerY > hedgehogY && canMoveUp) {
				// Move up.
				moveBadgerUp(hh, badgerX, badgerY);
				return;
			}
		} else if (badgerY == hedgehogY && badgerX != hedgehogX) {
			if (badgerX < hedgehogX && canMoveRight) {
				// Move right.
				moveBadgerRight(hh, badgerX, badgerY);
				return;
			} else if (badgerX > hedgehogX && canMoveLeft) {
				// Move left.
				moveBadgerLeft(hh, badgerX, badgerY);
				return;
			}
		} else {
			if (badgerX < hedgehogX && canMoveRight) {
				// Move right.
				moveBadgerRight(hh, badgerX, badgerY);
				return;
			} else if (badgerX > hedgehogX && canMoveLeft) {
				// Move left.
				moveBadgerLeft(hh, badgerX, badgerY);
				return;
			} else if (badgerY < hedgehogY && canMoveDown) {
				// Move down.
				moveBadgerDown(hh, badgerX, badgerY);
				return;
			} else if (badgerY > hedgehogY && canMoveUp) {
				// Move up.
				moveBadgerUp(hh, badgerX, badgerY);
				return;
			}
		}
		
		// We will only reach this point if the badger couldn't make an optimal move.
		// We don't want the badger to be staying still, so move in whichever direction possible.
		int rand = (int) (Math.random() * 100);
		
		if (rand >= 0 && rand < 25) {
			if (canMoveLeft) {
				moveBadgerLeft(hh, badgerX, badgerY);
			} else if (canMoveRight) {
				moveBadgerRight(hh, badgerX, badgerY);
			} else if (canMoveUp) {
				moveBadgerUp(hh, badgerX, badgerY);
			} else if (canMoveDown) {
				moveBadgerDown(hh, badgerX, badgerY);
			} else {
				trappedBadger(hh, badgerX, badgerY);
			}
		} else if (rand >= 25 && rand < 50) {
			if (canMoveRight) {
				moveBadgerRight(hh, badgerX, badgerY);
			} else if (canMoveUp) {
				moveBadgerUp(hh, badgerX, badgerY);
			} else if (canMoveDown) {
				moveBadgerDown(hh, badgerX, badgerY);
			} else if (canMoveLeft) {
				moveBadgerLeft(hh, badgerX, badgerY);
			} else {
				trappedBadger(hh, badgerX, badgerY);
			}
		} else if (rand >= 50 && rand < 75) {
			if (canMoveUp) {
				moveBadgerUp(hh, badgerX, badgerY);
			} else if (canMoveDown) {
				moveBadgerDown(hh, badgerX, badgerY);
			} else if (canMoveLeft) {
				moveBadgerLeft(hh, badgerX, badgerY);
			} else if (canMoveRight) {
				moveBadgerRight(hh, badgerX, badgerY);
			} else {
				trappedBadger(hh, badgerX, badgerY);
			}
		} else if (rand >= 75 && rand <= 100) {
			if (canMoveDown) {
				moveBadgerDown(hh, badgerX, badgerY);
			} else if (canMoveLeft) {
				moveBadgerLeft(hh, badgerX, badgerY);
			} else if (canMoveRight) {
				moveBadgerRight(hh, badgerX, badgerY);
			} else if (canMoveUp) {
				moveBadgerUp(hh, badgerX, badgerY);
			} else {
				trappedBadger(hh, badgerX, badgerY);
			}
		}
	}
	
	private void moveBadgerRight(HedgehogHavoc hh, int x, int y) {
		hh.grid[x][y].getBadger().addImage(ResourceManager.getImage(HedgehogHavoc.BADGERRIGHT_IMG));
		hh.grid[x][y].getBadger().removeImage(ResourceManager.getImage(HedgehogHavoc.BADGERLEFT_IMG));
		hh.grid[x][y].getBadger().moveCount = (int) hh.grid[x][y].getBadger().getCoarseGrainedWidth();
		hh.grid[x][y].getBadger().moveDir = "R";
		if (hh.grid[x + 1][y].getIsHedgehog()) {
			hh.lives -= 1;
			respawnHedgehog(hh);
		}
		
		hh.grid[x + 1][y].setBadger(hh.grid[x][y].getBadger().clone(hh.grid[x][y].getBadger()));
		hh.grid[x][y].setBadger(null);
	}
	
	private void moveBadgerLeft(HedgehogHavoc hh, int x, int y) {
		hh.grid[x][y].getBadger().addImage(ResourceManager.getImage(HedgehogHavoc.BADGERLEFT_IMG));
		hh.grid[x][y].getBadger().removeImage(ResourceManager.getImage(HedgehogHavoc.BADGERRIGHT_IMG));
		hh.grid[x][y].getBadger().moveCount = (int) hh.grid[x][y].getBadger().getCoarseGrainedWidth();
		hh.grid[x][y].getBadger().moveDir = "L";
		if (hh.grid[x - 1][y].getIsHedgehog()) {
			hh.lives -= 1;
			respawnHedgehog(hh);
		}
		
		hh.grid[x - 1][y].setBadger(hh.grid[x][y].getBadger().clone(hh.grid[x][y].getBadger()));
		hh.grid[x][y].setBadger(null);
	}
	
	private void moveBadgerDown(HedgehogHavoc hh, int x, int y) {
		hh.grid[x][y].getBadger().moveCount = (int) hh.grid[x][y].getBadger().getCoarseGrainedHeight();
		hh.grid[x][y].getBadger().moveDir = "D";
		if (hh.grid[x][y + 1].getIsHedgehog()) {
			hh.lives -= 1;
			respawnHedgehog(hh);
		}
		
		hh.grid[x][y + 1].setBadger(hh.grid[x][y].getBadger().clone(hh.grid[x][y].getBadger()));
		hh.grid[x][y].setBadger(null);
	}
	
	private void moveBadgerUp(HedgehogHavoc hh, int x, int y) {
		hh.grid[x][y].getBadger().moveCount = (int) hh.grid[x][y].getBadger().getCoarseGrainedHeight();
		hh.grid[x][y].getBadger().moveDir = "U";
		if (hh.grid[x][y - 1].getIsHedgehog()) {
			hh.lives -= 1;
			respawnHedgehog(hh);
		}
		
		hh.grid[x][y - 1].setBadger(hh.grid[x][y].getBadger().clone(hh.grid[x][y].getBadger()));
		hh.grid[x][y].setBadger(null);
	}
	
	private void trappedBadger(HedgehogHavoc hh, int x, int y) {
		boolean checkLeft = x == 0 || (!hh.grid[x - 1][y].getIsGround() && !hh.grid[x - 1][y].getIsHedgehog());
		boolean checkRight = x == 22 || (!hh.grid[x + 1][y].getIsGround() && !hh.grid[x + 1][y].getIsHedgehog());
		boolean checkUp = y == 0 || (!hh.grid[x][y - 1].getIsGround() && !hh.grid[x][y - 1].getIsHedgehog());
		boolean checkDown = y == 23 || (!hh.grid[x][y + 1].getIsGround() && !hh.grid[x][y + 1].getIsHedgehog());
		boolean checkTopLeft = (x == 0 && y == 0) || (!hh.grid[x - 1][y - 1].getIsGround() && !hh.grid[x - 1][y - 1].getIsHedgehog());
		boolean checkTopRight = (x == 22 && y == 0) || (!hh.grid[x + 1][y - 1].getIsGround() && !hh.grid[x + 1][y - 1].getIsHedgehog());
		boolean checkBottomLeft = (x == 0 && y == 22) || (!hh.grid[x - 1][y + 1].getIsGround() && !hh.grid[x - 1][y + 1].getIsHedgehog());
		boolean checkBottomRight = (x == 22 && y == 22) || (!hh.grid[x + 1][y + 1].getIsGround() && !hh.grid[x + 1][y + 1].getIsHedgehog());
		boolean allCaught = badgerCount == caughtBadgers;
		if (checkLeft &&
				checkRight &&
				checkUp &&
				checkDown &&
				checkTopLeft &&
				checkTopRight &&
				checkBottomLeft &&
				checkBottomRight) {
			if (allCaught) {
				hh.grid[x][y].setBadger(null);
				hh.grid[x][y].setBug(new Bug(x, y, hh.HUDHeight));
				badgerCount -= 1;
				caughtBadgers -= 1;
				remainingBadgers -= 1;
				hh.score += 10;
			} else if (!hh.grid[x][y].getBadger().caught) {
				hh.grid[x][y].getBadger().caught = true;
				caughtBadgers += 1;
			}
		} else {
			if (hh.grid[x][y].getBadger().caught) {
				hh.grid[x][y].getBadger().caught = false;
				caughtBadgers -= 1;
			}
		}
	}
	
	private void spawnBadger(HedgehogHavoc hh) {
		int tempX = (int) (Math.random() * 23);
		int tempY = (int) (Math.random() * 23);
		if (tempX < 0) {
			tempX = 0;
		} else if (tempX > 22) {
			tempX = 22;
		}
		
		if (tempY < 0) {
			tempY = 0;
		} else if (tempY > 22) {
			tempY = 22;
		}
		
		for (int i = tempX; i > 0; i--) {
			for (int j = tempY; j > 0; j--) {
				if (hh.grid[i][j].getIsGround()) {
					boolean hedgehogNear = false;
					for (int k = -1; k <= 1; k += 2) {
						int a = i + k;
						int b = j + k;
						
						if (a < 0 || a > 22) continue;
						if (hh.grid[a][j].getIsHedgehog()) hedgehogNear = true;
						
						if (b < 0 || b > 22) continue;
						if (hh.grid[i][b].getIsHedgehog()) hedgehogNear = true;
					}
					
					if (!hedgehogNear) {
						hh.grid[i][j].setBadger(new Badger(i, j, hh.HUDHeight));
						badgerCount++;
						return;
					}
				}
			}
		}
		
		for (int i = tempX + 1; i < 23; i++) {
			for (int j = tempY + 1; j < 23; j++) {
				if (hh.grid[i][j].getIsGround()) {
					boolean hedgehogNear = false;
					for (int k = -1; k <= 1; k += 2) {
						int a = i + k;
						int b = j + k;
						
						if (a < 0 || a > 22) continue;
						if (hh.grid[a][j].getIsHedgehog()) hedgehogNear = true;
						
						if (b < 0 || b > 22) continue;
						if (hh.grid[i][b].getIsHedgehog()) hedgehogNear = true;
					}
					
					if (!hedgehogNear) {
						hh.grid[i][j].setBadger(new Badger(i, j, hh.HUDHeight));
						badgerCount++;
						return;
					}
				}
			}
		}
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
					if (!hh.grid[x + 1][y].getIsHole()) {
						hh.grid[x + 1][y].setBlockMovable(hh.grid[x][y].getBlockMovable().clone(hh.grid[x][y].getBlockMovable()));
						hh.grid[x + 1][y].getBlockMovable().moveDir = "R";
						hh.grid[x + 1][y].getBlockMovable().moveCount = (int) hh.grid[x][y].getBlockMovable().getCoarseGrainedWidth();
					}
				} else if (direction.equals("L")) {
					if (!hh.grid[x - 1][y].getIsHole()) {
						hh.grid[x - 1][y].setBlockMovable(hh.grid[x][y].getBlockMovable().clone(hh.grid[x][y].getBlockMovable()));
						hh.grid[x - 1][y].getBlockMovable().moveDir = "L";
						hh.grid[x - 1][y].getBlockMovable().moveCount = (int) hh.grid[x][y].getBlockMovable().getCoarseGrainedWidth();
					}
				} else if (direction.equals("U")) {
					if (!hh.grid[x][y - 1].getIsHole()) {
						hh.grid[x][y - 1].setBlockMovable(hh.grid[x][y].getBlockMovable().clone(hh.grid[x][y].getBlockMovable()));
						hh.grid[x][y - 1].getBlockMovable().moveDir = "U";
						hh.grid[x][y - 1].getBlockMovable().moveCount = (int) hh.grid[x][y].getBlockMovable().getCoarseGrainedHeight();
					}
				} else if (direction.equals("D")) {
					if (!hh.grid[x][y + 1].getIsHole()) {
						hh.grid[x][y + 1].setBlockMovable(hh.grid[x][y].getBlockMovable().clone(hh.grid[x][y].getBlockMovable()));
						hh.grid[x][y + 1].getBlockMovable().moveDir = "D";
						hh.grid[x][y + 1].getBlockMovable().moveCount = (int) hh.grid[x][y].getBlockMovable().getCoarseGrainedHeight();
					}
				}

				hh.grid[x][y].setBlockMovable(null);
				return true;
			} else {
				return false;
			}
		} else if (hh.grid[x][y].getIsGround() || hh.grid[x][y].getIsBug() || hh.grid[x][y].getIsHole()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getID() {
		return HedgehogHavoc.PLAYINGSTATE;
	}
}