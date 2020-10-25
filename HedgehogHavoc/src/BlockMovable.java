import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

class BlockMovable extends Entity {
	private Vector velocity;
	public int moveCount;
	public String moveDir;
	
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
		velocity = new Vector(0, 0);
		moveCount = 0;
		moveDir = "";
	}
	
	public BlockMovable clone(BlockMovable block) {
		BlockMovable newBlock = new BlockMovable(0, 0);
		newBlock.setPosition(block.getPosition());
		newBlock.velocity = block.velocity;
		newBlock.moveCount = block.moveCount;
		newBlock.moveDir = block.moveDir;
		return newBlock;
	}
	
	/**
	 * Sets the velocity of the Block.
	 * @param v The velocity to set the Block at.
	 */
	public void setVelocity(final Vector v) {
		velocity = v;
	}
	
	/**
	 * Get the current velocity of the Block.
	 * @return Returns the Velocity of the Block.
	 */
	public Vector getVelocity() {
		return velocity;
	}
	
	/**
	 * Update the Hedgehog.
	 * @param delta The delta value for which to translate the velocity of the Hedgehog.
	 */
	public void update(final int delta) {
		translate(velocity.scale(delta));
	}
}