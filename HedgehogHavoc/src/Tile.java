import jig.Entity;

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
		hedgehog = hog;
	}
	
	public BlockMovable getBlockMovable() {
		return blockMovable;
	}
	
	public void setBlockMovable(BlockMovable block) {
		blockMovable = block;
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
			hedgehog.update(delta);
		} else if (isBlockMovable) {
			blockMovable.update(delta);
		}
	}
}