import org.newdawn.slick.Graphics;

import jig.Entity;
import jig.ResourceManager;

class Tile extends Entity {
	private int xPos;
	private int yPos;
	private Hedgehog hedgehog;
	private BlockMovable blockMovable;
	private boolean isHedgehog;
	private boolean isBlockMovable;
	private boolean isGround;
	
	/**
	 * Create a new movable block that will be movable by the player.
	 * @param x The initial X-position of the block.
	 * @param y The initial Y-position of the block.
	 */
	public Tile(final int x, final int y) {
		xPos = x;
		yPos = y;
		isGround = true;
		isHedgehog = false;
		isBlockMovable = false;
	}
	
	@Override
	public void render(Graphics g) {
		if (isHedgehog) {
			hedgehog.render(g);
		} else if (isBlockMovable) {
			blockMovable.render(g);
		}
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public void setXPos(int x) {
		xPos = x;
	}
	
	public void setYPos(int y) {
		yPos = y;
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
	
	public boolean getIsHedgehog() {
		return isHedgehog;
	}
	
	public void setIsHedgehog(boolean value) {
		if (value) {
			isHedgehog = true;
			isGround = false;
			isBlockMovable = false;
		} else {
			isHedgehog = false;
			isBlockMovable = false;
			isGround = true;
		}
	}
	
	public boolean getIsBlockMovable() {
		return isBlockMovable;
	}
	
	public void setIsBlockMovable(boolean value) {
		if (value) {
			isBlockMovable = true;
			isHedgehog = false;
			isGround = false;
		} else {
			isBlockMovable = false;
			isHedgehog = false;
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
			isBlockMovable = false;
		} else {
			isGround = false;
			isHedgehog = false;
			isBlockMovable = false;
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