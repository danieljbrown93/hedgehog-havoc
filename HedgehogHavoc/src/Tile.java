import org.newdawn.slick.Graphics;

import jig.Entity;
import jig.ResourceManager;

class Tile extends Entity {
	public DebugTile debugTile;
	
	private final int xPos;
	private final int yPos;
	private Hedgehog hedgehog;
	private Badger badger;
	private Bug bug;
	private BlockMovable blockMovable;
	private BlockImmovable blockImmovable;
	private boolean isHedgehog;
	private boolean isBadger;
	private boolean isBug;
	private boolean isBlockMovable;
	private boolean isBlockImmovable;
	private boolean isGround;
	private boolean discovered;
	private boolean tileDebug;
	private int score;
	private Tile parent;
	
	/**
	 * Create a new movable block that will be movable by the player.
	 * @param x The initial X-position of the block.
	 * @param y The initial Y-position of the block.
	 */
	public Tile(final int x, final int y) {
		xPos = x;
		yPos = y;
		score = 0;
		discovered = false;
		parent = null;
		isGround = true;
		isHedgehog = false;
		isBadger = false;
		isBug = false;
		isBlockMovable = false;
		isBlockImmovable = false;
		tileDebug = false;
	}
	
	@Override
	public void render(Graphics g) {
		if (HedgehogHavoc.debug) {
			if (tileDebug) {
				debugTile.render(g);
			}
		}
		
		if (isHedgehog) {
			hedgehog.render(g);
		} else if (isBadger) {
			badger.render(g);
		} else if (isBlockMovable) {
			blockMovable.render(g);
		} else if (isBlockImmovable) {
			blockImmovable.render(g);
		} else if (isBug) {
			bug.render(g);
		}
	}
	
	public final int getXPos() {
		return xPos;
	}
	
	public final int getYPos() {
		return yPos;
	}
	
	public Hedgehog getHedgehog() {
		return hedgehog;
	}
	
	public void setHedgehog(Hedgehog hog) {
		if (hog == null) {
			hedgehog.removeImage(ResourceManager.getImage(HedgehogHavoc.HEDGEHOGLEFT_IMG));
			hedgehog.removeImage(ResourceManager.getImage(HedgehogHavoc.HEDGEHOGRIGHT_IMG));
			hedgehog = null;
			setIsHedgehog(false);
		} else {
			hedgehog = hog;
			setIsHedgehog(true);
		}
	}
	
	public Badger getBadger() {
		return badger;
	}
	
	public void setBadger(Badger b) {
		if (b == null) {
			badger.removeImage(ResourceManager.getImage(HedgehogHavoc.BADGERLEFT_IMG));
			badger.removeImage(ResourceManager.getImage(HedgehogHavoc.BADGERRIGHT_IMG));
			badger = null;
			setIsBadger(false);
		} else {
			badger = b;
			setIsBadger(true);
		}
	}
	
	public Bug getBug() {
		return bug;
	}
	
	public void setBug(Bug b) {
		if (b == null) {
			bug.removeImage(ResourceManager.getImage(HedgehogHavoc.BUG_IMG));
			bug = null;
			setIsBug(false);
		} else {
			bug = b;
			setIsBug(true);
		}
	}
	
	public BlockMovable getBlockMovable() {
		return blockMovable;
	}
	
	public void setBlockMovable(BlockMovable block) {
		if (block == null) {
			blockMovable.removeImage(ResourceManager.getImage(HedgehogHavoc.BLOCK_MOVABLE_IMG));
			blockMovable = null;
			setIsBlockMovable(false);
		} else {
			blockMovable = block;
			setIsBlockMovable(true);
		}
	}
	
	public BlockImmovable getBlockImmovable() {
		return blockImmovable;
	}
	
	public void setBlockImmovable(BlockImmovable block) {
		if (block == null) {
			blockImmovable.removeImage(ResourceManager.getImage(HedgehogHavoc.BLOCK_IMMOVABLE_IMG));
			blockImmovable = null;
			setIsBlockImmovable(false);
		} else {
			blockImmovable = block;
			setIsBlockImmovable(true);
		}
	}
	
	public boolean getIsHedgehog() {
		return isHedgehog;
	}
	
	public void setIsHedgehog(boolean value) {
		if (value) {
			isHedgehog = true;
			isBadger = false;
			isBug = false;
			isGround = false;
			isBlockMovable = false;
			isBlockImmovable = false;
		} else {
			isHedgehog = false;
			isBadger = false;
			isBug = false;
			isBlockMovable = false;
			isBlockImmovable = false;
			isGround = true;
		}
	}
	
	public boolean getIsBadger() {
		return isBadger;
	}
	
	public void setIsBadger(boolean value) {
		if (value) {
			isBadger = true;
			isHedgehog = false;
			isBug = false;
			isGround = false;
			isBlockMovable = false;
			isBlockImmovable = false;
		} else {
			isBadger = false;
			isHedgehog = false;
			isBug = false;
			isBlockMovable = false;
			isBlockImmovable = false;
			isGround = true;
		}
	}
	
	public boolean getIsBug() {
		return isBug;
	}
	
	public void setIsBug(boolean value) {
		if (value) {
			isBug = true;
			isHedgehog = false;
			isBadger = false;
			isGround = false;
			isBlockMovable = false;
			isBlockImmovable = false;
		} else {
			isBug = false;
			isBadger = false;
			isHedgehog = false;
			isBlockMovable = false;
			isBlockImmovable = false;
			isGround = true;
		}
	}
	
	public boolean getIsBlockMovable() {
		return isBlockMovable;
	}
	
	public void setIsBlockMovable(boolean value) {
		if (value) {
			isBlockMovable = true;
			isBlockImmovable = false;
			isHedgehog = false;
			isBadger = false;
			isBug = false;
			isGround = false;
		} else {
			isBlockMovable = false;
			isBlockImmovable = false;
			isHedgehog = false;
			isBadger = false;
			isBug = false;
			isGround = true;
		}
	}
	
	public boolean getIsBlockImmovable() {
		return isBlockImmovable;
	}
	
	public void setIsBlockImmovable(boolean value) {
		if (value) {
			isBlockImmovable = true;
			isBlockMovable = false;
			isHedgehog = false;
			isBadger = false;
			isBug = false;
			isGround = false;
		} else {
			isBlockImmovable = false;
			isBlockMovable = false;
			isHedgehog = false;
			isBadger = false;
			isBug = false;
			isGround = true;
		}
	}
	
	public boolean getIsGround() {
		return isGround;
	}
	
	public void setIsGround(boolean value) {
		if (value) {
			isGround = true;
			isHedgehog = false;
			isBadger = false;
			isBug = false;
			isBlockMovable = false;
			isBlockImmovable = false;
		} else {
			isGround = false;
			isHedgehog = false;
			isBadger = false;
			isBug = false;
			isBlockMovable = false;
			isBlockImmovable = false;
		}
	}
	
	public void setScore(int s) {
		score = s;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setDiscovered(boolean value) {
		discovered = value;
	}
	
	public boolean isDiscovered() {
		return discovered;
	}
	
	public void setParent(Tile p) {
		parent = p;
	}
	
	public Tile getParent() {
		return parent;
	}
	
	public void setDebugActive(boolean value) {
		if (value) {
			debugTile = new DebugTile(
					xPos,
					yPos,
					ResourceManager.getImage(HedgehogHavoc.HUDBACKGROUND_IMG).getHeight());
			tileDebug = true;
		} else {
			debugTile = null;
			tileDebug = false;
		}
	}
	
	/**
	 * Update the Hedgehog.
	 * @param delta The delta value for which to translate the velocity of the Hedgehog.
	 */
	public void update(final int delta) {
		if (isHedgehog) {
			if (hedgehog.moveCount > 0) {
				hedgehog.moveCount -= 2;
				if (hedgehog.moveDir.equals("R")) {
					hedgehog.setX(hedgehog.getX() + 2);
				} else if (hedgehog.moveDir.equals("L")) {
					hedgehog.setX(hedgehog.getX() - 2);
				} else if (hedgehog.moveDir.equals("U")) {
					hedgehog.setY(hedgehog.getY() - 2);
				} else if (hedgehog.moveDir.equals("D")) {
					hedgehog.setY(hedgehog.getY() + 2);
				}
			}
		} else if (isBadger) {
			if (badger.moveCount > 0) {
				badger.moveCount -= 1;
				if (badger.moveDir.equals("R")) {
					badger.setX(badger.getX() + 1);
				} else if (badger.moveDir.equals("L")) {
					badger.setX(badger.getX() - 1);
				} else if (badger.moveDir.equals("U")) {
					badger.setY(badger.getY() - 1);
				} else if (badger.moveDir.equals("D")) {
					badger.setY(badger.getY() + 1);
				} else if (badger.moveDir.equals("DL")) {
					badger.setX(badger.getX() - 1);
					badger.setY(badger.getY() + 1);
				} else if (badger.moveDir.equals("DR")) {
					badger.setX(badger.getX() + 1);
					badger.setY(badger.getY() + 1);
				} else if (badger.moveDir.equals("UR")) {
					badger.setX(badger.getX() + 1);
					badger.setY(badger.getY() - 1);
				} else if (badger.moveDir.equals("UL")) {
					badger.setX(badger.getX() - 1);
					badger.setY(badger.getY() - 1);
				}
			}
		} else if (isBlockMovable) {
			if (blockMovable.moveCount > 0) {
				blockMovable.moveCount -= 2;
				if (blockMovable.moveDir.equals("R")) {
					blockMovable.setX(blockMovable.getX() + 2);
				} else if (blockMovable.moveDir.equals("L")) {
					blockMovable.setX(blockMovable.getX() - 2);
				} else if (blockMovable.moveDir.equals("U")) {
					blockMovable.setY(blockMovable.getY() - 2);
				} else if (blockMovable.moveDir.equals("D")) {
					blockMovable.setY(blockMovable.getY() + 2);
				}
			}
		}
	}
}