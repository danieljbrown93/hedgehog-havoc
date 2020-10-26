import jig.Entity;
import jig.ResourceManager;

class Hole extends Entity {	
	/**
	 * Create a new hole that swallows blocks and immobilizes the player.
	 * @param x The initial X-position of the hole.
	 * @param y The initial Y-position of the hole.
	 */
	public Hole(final int x, final int y, final int HUDOffset) {
		super(
				(x * 26f) + (ResourceManager.getImage(HedgehogHavoc.HOLE_IMG).getWidth() / 2),
				(y * 26f) + (ResourceManager.getImage(HedgehogHavoc.HOLE_IMG).getHeight() / 2) + HUDOffset);
		addImageWithBoundingBox(ResourceManager.getImage(HedgehogHavoc.HOLE_IMG));
		antiAliasing = false;
	}

	public void update(final int delta) {
	}
}