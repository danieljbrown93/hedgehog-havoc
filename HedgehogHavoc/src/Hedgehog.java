import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

class Hedgehog extends Entity {
	private Vector velocity;
	public int moveCount;
	public String moveDir;
	
	/**
	 * Create a new Hedgehog that will be controlled by the player.
	 * @param x The initial X position within the grid.
	 * @param y The initial Y position within the grid.
	 */
	public Hedgehog(final int x, final int y, final int HUDOffset) {
		super(
				(x * 26f) + (ResourceManager.getImage(HedgehogHavoc.HEDGEHOGDOWN_IMG).getWidth() / 2),
				(y * 26f) + (ResourceManager.getImage(HedgehogHavoc.HEDGEHOGDOWN_IMG).getHeight() / 2) + HUDOffset);
		addImageWithBoundingBox(ResourceManager.getImage(HedgehogHavoc.HEDGEHOGDOWN_IMG));
		velocity = new Vector(0, 0);
		moveDir = "";
		antiAliasing = false;
	}
	
	public Hedgehog clone(Hedgehog hog) {
		Hedgehog newHog = new Hedgehog(0, 0, 0);
		newHog.setPosition(hog.getPosition());
		newHog.velocity = hog.velocity;
		newHog.moveCount = hog.moveCount;
		newHog.moveDir = hog.moveDir;
		return newHog;
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