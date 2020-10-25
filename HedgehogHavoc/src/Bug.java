import jig.Entity;
import jig.ResourceManager;

class Bug extends Entity {	
	/**
	 * Create a new immovable block that can not be moved by the player.
	 * @param x The initial X-position of the block.
	 * @param y The initial Y-position of the block.
	 */
	public Bug(final int x, final int y) {
		super(
				(x * 26f) + (ResourceManager.getImage(HedgehogHavoc.BUG_IMG).getWidth() / 2),
				(y * 26f) + (ResourceManager.getImage(HedgehogHavoc.BUG_IMG).getHeight() / 2));
		addImageWithBoundingBox(ResourceManager.getImage(HedgehogHavoc.BUG_IMG));
		antiAliasing = false;
	}

	public void update(final int delta) {
	}
}