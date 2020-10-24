import java.util.ArrayList;
import java.util.Iterator;

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
	private ArrayList<BlockMovable> movingBlocks;
	private Graphics graphics;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		movingBlocks = new ArrayList<BlockMovable>(23);
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		HedgehogHavoc hh = (HedgehogHavoc)game;
		graphics = g;
		hh.renderStats(graphics);
		hh.hedgehog.render(graphics);
		hh.renderLevel(graphics);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		HedgehogHavoc hh = (HedgehogHavoc)game;
		hh.getFPS();
		
		if (input.isKeyDown(Input.KEY_RIGHT) && hh.hedgehog.gridX < 22 && hh.hedgehog.moveCount <= 0) {
			if (hh.grid.get(hh.hedgehog.gridX + 1).get(hh.hedgehog.gridY).getIsBlockMovable()) {
				if (moveBlock(hh, hh.hedgehog.gridX, hh.hedgehog.gridY, "R")) {
					moveRight(hh);
				}
			} else if (hh.grid.get(hh.hedgehog.gridX + 1).get(hh.hedgehog.gridY).getIsGround()) {
				moveRight(hh);
			}
		} else if (input.isKeyDown(Input.KEY_LEFT) && hh.hedgehog.gridX > 0 && hh.hedgehog.moveCount <= 0) {
			if (hh.grid.get(hh.hedgehog.gridX - 1).get(hh.hedgehog.gridY).getIsBlockMovable()) {
				if (moveBlock(hh, hh.hedgehog.gridX, hh.hedgehog.gridY, "L")) {
					moveLeft(hh);
				}
			} else if (hh.grid.get(hh.hedgehog.gridX - 1).get(hh.hedgehog.gridY).getIsGround()) {
				moveLeft(hh);
			}
		} else if (input.isKeyDown(Input.KEY_UP) && hh.hedgehog.gridY > 0 && hh.hedgehog.moveCount <= 0) {
			if (hh.grid.get(hh.hedgehog.gridX).get(hh.hedgehog.gridY - 1).getIsBlockMovable()) {
				if (moveBlock(hh, hh.hedgehog.gridX, hh.hedgehog.gridY, "U")) {
					moveUp(hh);
				}
			} else if (hh.grid.get(hh.hedgehog.gridX).get(hh.hedgehog.gridY - 1).getIsGround()) {
				moveUp(hh);
			}
		} else if (input.isKeyDown(Input.KEY_DOWN) && hh.hedgehog.gridY < 22 && hh.hedgehog.moveCount <= 0) {
			if (hh.grid.get(hh.hedgehog.gridX).get(hh.hedgehog.gridY + 1).getIsBlockMovable()) {
				if (moveBlock(hh, hh.hedgehog.gridX, hh.hedgehog.gridY, "D")) {
					moveDown(hh);
				}
			} else if (hh.grid.get(hh.hedgehog.gridX).get(hh.hedgehog.gridY + 1).getIsGround()) {
				moveDown(hh);
			}
		} else {
			if (hh.hedgehog.moveCount > 0) {
				hh.hedgehog.moveCount -= 2;
				if (hh.hedgehog.moveDir.equals("R")) {
					hh.hedgehog.setX(hh.hedgehog.getX() + 2);
				} else if (hh.hedgehog.moveDir.equals("L")) {
					hh.hedgehog.setX(hh.hedgehog.getX() - 2);
				} else if (hh.hedgehog.moveDir.equals("U")) {
					hh.hedgehog.setY(hh.hedgehog.getY() - 2);
				} else if (hh.hedgehog.moveDir.equals("D")) {
					hh.hedgehog.setY(hh.hedgehog.getY() + 2);
				}
			}
		}
		
		for (Iterator<BlockMovable> i = movingBlocks.iterator(); i.hasNext();) {
			BlockMovable currentBlock = i.next();
			if (currentBlock.moveTimer > 0) {
				if (currentBlock.moveDirection.equals("R")) {
					currentBlock.setX(currentBlock.getX() + 2);
				} else if (currentBlock.moveDirection.equals("L")) {
					currentBlock.setX(currentBlock.getX() - 2);
				} else if (currentBlock.moveDirection.equals("U")) {
					currentBlock.setY(currentBlock.getY() - 2);
				} else if (currentBlock.moveDirection.equals("D")) {
					currentBlock.setY(currentBlock.getY() + 2);
				}
				
				currentBlock.moveTimer -= 2;
				currentBlock.update(delta);
			} else {
				i.remove();
			}
		}
		
		updatePositions(hh, delta);
	}

	@Override
	public int getID() {
		return HedgehogHavoc.PLAYINGSTATE;
	}
	
	private void moveRight(HedgehogHavoc hh) {
		hh.hedgehog.addImage(ResourceManager.getImage(HedgehogHavoc.HEDGEHOGRIGHT_IMG));
		hh.hedgehog.removeImage(ResourceManager.getImage(HedgehogHavoc.HEDGEHOGLEFT_IMG));
		hh.hedgehog.moveCount = (int) hh.hedgehog.getCoarseGrainedWidth();
		hh.hedgehog.moveDir = "R";
		hh.grid.get(hh.hedgehog.gridX).get(hh.hedgehog.gridY).setIsGround(true);
		hh.hedgehog.gridX += 1;
		hh.grid.get(hh.hedgehog.gridX).get(hh.hedgehog.gridY).setIsHedgehog(true);
		hh.grid.get(hh.hedgehog.gridX).get(hh.hedgehog.gridY).setHedgehog(hh.hedgehog);
	}
	
	private void moveLeft(HedgehogHavoc hh) {
		hh.hedgehog.addImage(ResourceManager.getImage(HedgehogHavoc.HEDGEHOGLEFT_IMG));
		hh.hedgehog.removeImage(ResourceManager.getImage(HedgehogHavoc.HEDGEHOGRIGHT_IMG));
		hh.hedgehog.moveCount = (int) hh.hedgehog.getCoarseGrainedWidth();
		hh.hedgehog.moveDir = "L";
		hh.grid.get(hh.hedgehog.gridX).get(hh.hedgehog.gridY).setIsGround(true);
		hh.hedgehog.gridX -= 1;
		hh.grid.get(hh.hedgehog.gridX).get(hh.hedgehog.gridY).setIsHedgehog(true);
		hh.grid.get(hh.hedgehog.gridX).get(hh.hedgehog.gridY).setHedgehog(hh.hedgehog);
	}
	
	private void moveUp(HedgehogHavoc hh) {
		hh.hedgehog.moveCount = (int) hh.hedgehog.getCoarseGrainedWidth();
		hh.hedgehog.moveDir = "U";
		hh.grid.get(hh.hedgehog.gridX).get(hh.hedgehog.gridY).setIsGround(true);
		hh.hedgehog.gridY -= 1;
		hh.grid.get(hh.hedgehog.gridX).get(hh.hedgehog.gridY).setIsHedgehog(true);
		hh.grid.get(hh.hedgehog.gridX).get(hh.hedgehog.gridY).setHedgehog(hh.hedgehog);
	}
	
	private void moveDown(HedgehogHavoc hh) {
		hh.hedgehog.moveCount = (int) hh.hedgehog.getCoarseGrainedWidth();
		hh.hedgehog.moveDir = "D";
		hh.grid.get(hh.hedgehog.gridX).get(hh.hedgehog.gridY).setIsGround(true);
		hh.hedgehog.gridY += 1;
		hh.grid.get(hh.hedgehog.gridX).get(hh.hedgehog.gridY).setIsHedgehog(true);
		hh.grid.get(hh.hedgehog.gridX).get(hh.hedgehog.gridY).setHedgehog(hh.hedgehog);
	}
	
	private void updatePositions(HedgehogHavoc hh, int delta) {
//		float widthSegment = hh.ScreenWidth / 23;
//		float heightSegment = hh.ScreenHeight / 23;
		
		// Update hedgehog position.
		hh.hedgehog.update(delta);
		
		// Update badger positions.
		// TODO
		
		// Update block positions.
//		for (int i = 0; i < 23; i++) {
//			for (int j = 0; j < 23; j++) {
//				BlockMovable currentBlock = hh.movableBlocks.get(i).get(j);
//				currentBlock.update(delta);
//				if (blockVel.getX() != 0 || blockVel.getY() != 0) {
//					if (currentBlock.getX() >= widthSegment * i && currentBlock.getX() <= widthSegment * (i + 1)) {
//						if (currentBlock.getY() >= heightSegment * j && currentBlock.getY() <= heightSegment * (j + 1)) {
//							if (blockVel.getX() > 0) {
//								// Block moving right.
//								if (hh.grid.get(currentBlock.gridX - 1).get(currentBlock.gridY).equals("H")) {
//									hh.grid.get(currentBlock.gridX - 1).set(currentBlock.gridY, "G");
//									hh.grid.get(currentBlock.gridX).set(currentBlock.gridY, "H");
//								}
//							} else if (blockVel.getX() < 0) {
//								// Block moving left.
//								if (hh.grid.get(currentBlock.gridX + 1).get(currentBlock.gridY).equals("H")) {
//									hh.grid.get(currentBlock.gridX + 1).set(currentBlock.gridY, "G");
//									hh.grid.get(currentBlock.gridX).set(currentBlock.gridY, "H");
//								}
//							} else if (blockVel.getY() < 0) {
//								// Block moving up.
//								if (hh.grid.get(currentBlock.gridX).get(currentBlock.gridY + 1).equals("H")) {
//									hh.grid.get(currentBlock.gridX).set(currentBlock.gridY + 1, "G");
//									hh.grid.get(currentBlock.gridX).set(currentBlock.gridY, "H");
//								}
//							} else if (blockVel.getY() > 0) {
//								// Block moving down.
//								if (hh.grid.get(currentBlock.gridX).get(currentBlock.gridY - 1).equals("H")) {
//									hh.grid.get(currentBlock.gridX).set(currentBlock.gridY - 1, "G");
//									hh.grid.get(currentBlock.gridX).set(currentBlock.gridY, "H");
//								}
//							}
//							
//							hh.grid.get(i).set(j, "M");
//							hh.movableBlocks.get(i).get(j).gridX = i;
//							hh.movableBlocks.get(i).get(j).gridY = j;
//						}
//					}
//				}
//			}
//		}
	}
	
	private boolean moveBlock(HedgehogHavoc hh, int x, int y, String direction) {
		if (x < 1 || x > 21 || y < 1 || y > 21) {
			// We don't want to go outside of our array bounds.
			return false;
		}
		
		if (direction.equals("R")) {
			x++;
		} else if (direction.equals("L")) {
			x--;
		} else if (direction.equals("U")) {
			y--;
		} else if (direction.equals("D")) {
			y++;
		}
		
		if (hh.grid.get(x).get(y).getIsBlockMovable()) {
			if (moveBlock(hh, x, y, direction)) {
				if (direction.equals("R")) {
					hh.grid.get(x).get(y).getBlockMovable().moveDirection = "R";
					hh.grid.get(x).get(y).getBlockMovable().gridX += 1;
					hh.grid.get(x).get(y).getBlockMovable().moveTimer = (int) hh.grid.get(x).get(y).getBlockMovable().getCoarseGrainedWidth();
					hh.grid.get(x + 1).get(y).setBlockMovable(hh.grid.get(x).get(y).getBlockMovable());
					hh.grid.get(x + 1).get(y).setIsBlockMovable(true);
					movingBlocks.add(hh.grid.get(x + 1).get(y).getBlockMovable());
				} else if (direction.equals("L")) {
					hh.grid.get(x).get(y).getBlockMovable().moveDirection = "L";
					hh.grid.get(x).get(y).getBlockMovable().gridX -= 1;
					hh.grid.get(x).get(y).getBlockMovable().moveTimer = (int) hh.grid.get(x).get(y).getBlockMovable().getCoarseGrainedWidth();
					hh.grid.get(x - 1).get(y).setBlockMovable(hh.grid.get(x).get(y).getBlockMovable());
					hh.grid.get(x - 1).get(y).setIsBlockMovable(true);
					movingBlocks.add(hh.grid.get(x - 1).get(y).getBlockMovable());
				} else if (direction.equals("U")) {
					hh.grid.get(x).get(y).getBlockMovable().moveDirection = "U";
					hh.grid.get(x).get(y).getBlockMovable().gridY -= 1;
					hh.grid.get(x).get(y).getBlockMovable().moveTimer = (int) hh.grid.get(x).get(y).getBlockMovable().getCoarseGrainedHeight();
					hh.grid.get(x).get(y - 1).setBlockMovable(hh.grid.get(x).get(y).getBlockMovable());
					hh.grid.get(x).get(y - 1).setIsBlockMovable(true);
					movingBlocks.add(hh.grid.get(x).get(y - 1).getBlockMovable());
				} else if (direction.equals("D")) {
					hh.grid.get(x).get(y).getBlockMovable().moveDirection = "D";
					hh.grid.get(x).get(y).getBlockMovable().gridY += 1;
					hh.grid.get(x).get(y).getBlockMovable().moveTimer = (int) hh.grid.get(x).get(y).getBlockMovable().getCoarseGrainedHeight();
					hh.grid.get(x).get(y + 1).setBlockMovable(hh.grid.get(x).get(y).getBlockMovable());
					hh.grid.get(x).get(y + 1).setIsBlockMovable(true);
					movingBlocks.add(hh.grid.get(x).get(y + 1).getBlockMovable());
				}

				hh.grid.get(x).get(y).setIsGround(true);
				return true;
			} else {
				return false;
			}
		} else if (hh.grid.get(x).get(y).getIsGround()) {
			return true;
		} else {
			return false;
		}
	}
}