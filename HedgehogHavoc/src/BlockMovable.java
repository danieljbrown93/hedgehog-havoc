import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

class BlockMovable extends Entity {
	private Vector velocity;
	
	/**
	 * Create a new movable block that will be movable by the player.
	 * @param x The initial X-position of the block.
	 * @param y The initial Y-position of the block.
	 */
	public BlockMovable(final float x, final float y) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager.getImage(HedgehogHavoc.BLOCK_MOVABLE_IMG));
		velocity = new Vector(0, 0);
	}
	
	/**
	 * Sets the velocity of the Hedgehog.
	 * @param v The velocity to set the Hedgehog at.
	 */
	public void setVelocity(final Vector v) {
		velocity = v;
	}
	
	/**
	 * Get the current velocity of the Hedgehog.
	 * @return Returns the Velocity of the Hedgehog.
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