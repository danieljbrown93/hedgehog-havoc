import jig.Entity;
import jig.ResourceManager;

class BlockMovable extends Entity {
	private boolean blockActive;
	public int moveTimer;
	public int gridX;
	public int gridY;
	public String moveDirection;
	
	/**
	 * Create a new movable block that will be movable by the player.
	 * @param x The initial X-position of the block.
	 * @param y The initial Y-position of the block.
	 */
	public BlockMovable(final int x, final int y) {
		super(
				(x * 26f) + (ResourceManager.getImage(HedgehogHavoc.BLOCK_MOVABLE_IMG).getWidth() / 2),
				(y * 26f) + (ResourceManager.getImage(HedgehogHavoc.BLOCK_MOVABLE_IMG).getHeight() / 2));
		addImageWithBoundingBox(ResourceManager.getImage(HedgehogHavoc.BLOCK_MOVABLE_IMG));
		gridX = x;
		gridY = y;
		blockActive = false;
		moveTimer = 0;
		moveDirection = "";
	}
	
	public BlockMovable clone() {
		BlockMovable newBlock = new BlockMovable(gridX, gridY);
		newBlock.blockActive = blockActive;
		newBlock.moveDirection = moveDirection;
		newBlock.moveTimer = moveTimer;
		
		return newBlock;
	}
	
	public void setActive(final boolean value) {
		blockActive = value;
	}
	
	public boolean getActive() {
		return blockActive;
	}
	
	/**
	 * Update the Hedgehog.
	 * @param delta The delta value for which to translate the velocity of the Hedgehog.
	 */
	public void update(final int delta) {}
}