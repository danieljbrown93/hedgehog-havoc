import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

class Hedgehog extends Entity {
	private Vector velocity;
	public int gridX;
	public int gridY;
	public int moveCount;
	public String moveDir;
	
	/**
	 * Create a new Hedgehog that will be controlled by the player.
	 * @param x The initial X position within the grid.
	 * @param y The initial Y position within the grid.
	 */
	public Hedgehog(final int x, final int y) {
		super(
				(x * 26f) + (ResourceManager.getImage(HedgehogHavoc.HEDGEHOGLEFT_IMG).getWidth() / 2),
				(y * 26f) + (ResourceManager.getImage(HedgehogHavoc.HEDGEHOGLEFT_IMG).getHeight() / 2));
		addImageWithBoundingBox(ResourceManager.getImage(HedgehogHavoc.HEDGEHOGLEFT_IMG));
		velocity = new Vector(0, 0);
		gridX = x;
		gridY = y;
		moveDir = "";
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