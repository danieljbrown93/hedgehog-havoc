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
	private Hole hole;
	private BlockMovable blockMovable;
	private BlockImmovable blockImmovable;
	private boolean isHedgehog;
	private boolean isBadger;
	private boolean isBug;
	private boolean isHole;
	private boolean isBlockMovable;
	private boolean isBlockImmovable;
	private boolean isGround;
	private boolean discovered;
	private boolean tileDebug;
	private Tile parent;
	
	/**
	 * Create a new movable block that will be movable by the player.
	 * @param x The initial X-position of the block.
	 * @param y The initial Y-position of the block.
	 */
	public Tile(final int x, final int y) {
		xPos = x;
		yPos = y;
		discovered = false;
		parent = null;
		isGround = true;
		isHedgehog = false;
		isBadger = false;
		isBug = false;
		isHole = false;
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
		} else if (isHole) {
			hole.render(g);
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
	
	public Hole getHole() {
		return hole;
	}
	
	public void setHole(Hole h) {
		if (h == null) {
			hole.removeImage(ResourceManager.getImage(HedgehogHavoc.HOLE_IMG));
			hole = null;
			setIsHole(false);
		} else {
			hole = h;
			setIsHole(true);
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
			isHole = false;
			isGround = false;
			isBlockMovable = false;
			isBlockImmovable = false;
		} else {
			isHedgehog = false;
			isBadger = false;
			isBug = false;
			isHole = false;
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
			isHole = false;
			isGround = false;
			isBlockMovable = false;
			isBlockImmovable = false;
		} else {
			isBadger = false;
			isHedgehog = false;
			isBug = false;
			isHole = false;
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
			isHole = false;
			isHedgehog = false;
			isBadger = false;
			isGround = false;
			isBlockMovable = false;
			isBlockImmovable = false;
		} else {
			isBug = false;
			isHole = false;
			isBadger = false;
			isHedgehog = false;
			isBlockMovable = false;
			isBlockImmovable = false;
			isGround = true;
		}
	}
	
	public boolean getIsHole() {
		return isHole;
	}
	
	public void setIsHole(boolean value) {
		if (value) {
			isHole = true;
			isBug = false;
			isHedgehog = false;
			isBadger = false;
			isGround = false;
			isBlockMovable = false;
			isBlockImmovable = false;
		} else {
			isHole = false;
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
			isHole = false;
			isGround = false;
		} else {
			isBlockMovable = false;
			isBlockImmovable = false;
			isHedgehog = false;
			isBadger = false;
			isBug = false;
			isHole = false;
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
			isHole = false;
			isGround = false;
		} else {
			isBlockImmovable = false;
			isBlockMovable = false;
			isHedgehog = false;
			isBadger = false;
			isBug = false;
			isHole = false;
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
			isHole = false;
			isBlockMovable = false;
			isBlockImmovable = false;
		} else {
			isGround = false;
			isHedgehog = false;
			isBadger = false;
			isBug = false;
			isHole = false;
			isBlockMovable = false;
			isBlockImmovable = false;
		}
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
				int moveAmount = 3;
				if (hedgehog.moveCount < moveAmount) moveAmount = hedgehog.moveCount;
				hedgehog.moveCount -= moveAmount;
				if (hedgehog.moveDir.equals("R")) {
					hedgehog.setX(hedgehog.getX() + moveAmount);
				} else if (hedgehog.moveDir.equals("L")) {
					hedgehog.setX(hedgehog.getX() - moveAmount);
				} else if (hedgehog.moveDir.equals("U")) {
					hedgehog.setY(hedgehog.getY() - moveAmount);
				} else if (hedgehog.moveDir.equals("D")) {
					hedgehog.setY(hedgehog.getY() + moveAmount);
				}
			}
		} else if (isBadger) {
			if (badger.moveCount > 0) {
				int moveAmount = badger.moveSpeed;
				if (badger.moveCount < moveAmount) moveAmount = badger.moveCount;
				badger.moveCount -= moveAmount;
				if (badger.moveDir.equals("R")) {
					badger.setX(badger.getX() + moveAmount);
				} else if (badger.moveDir.equals("L")) {
					badger.setX(badger.getX() - moveAmount);
				} else if (badger.moveDir.equals("U")) {
					badger.setY(badger.getY() - moveAmount);
				} else if (badger.moveDir.equals("D")) {
					badger.setY(badger.getY() + moveAmount);
				} else if (badger.moveDir.equals("DL")) {
					badger.setX(badger.getX() - moveAmount);
					badger.setY(badger.getY() + moveAmount);
				} else if (badger.moveDir.equals("DR")) {
					badger.setX(badger.getX() + moveAmount);
					badger.setY(badger.getY() + moveAmount);
				} else if (badger.moveDir.equals("UR")) {
					badger.setX(badger.getX() + moveAmount);
					badger.setY(badger.getY() - moveAmount);
				} else if (badger.moveDir.equals("UL")) {
					badger.setX(badger.getX() - moveAmount);
					badger.setY(badger.getY() - moveAmount);
				}
			}
		} else if (isBlockMovable) {
			if (blockMovable.moveCount > 0) {
				int moveAmount = 3;
				if (blockMovable.moveCount < moveAmount) moveAmount = blockMovable.moveCount;
				blockMovable.moveCount -= moveAmount;
				if (blockMovable.moveDir.equals("R")) {
					blockMovable.setX(blockMovable.getX() + moveAmount);
				} else if (blockMovable.moveDir.equals("L")) {
					blockMovable.setX(blockMovable.getX() - moveAmount);
				} else if (blockMovable.moveDir.equals("U")) {
					blockMovable.setY(blockMovable.getY() - moveAmount);
				} else if (blockMovable.moveDir.equals("D")) {
					blockMovable.setY(blockMovable.getY() + moveAmount);
				}
			}
		}
	}
}