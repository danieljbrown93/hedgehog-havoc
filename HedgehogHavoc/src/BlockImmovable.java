import jig.Entity;
import jig.ResourceManager;

class BlockImmovable extends Entity {	
	/**
	 * Create a new immovable block that can not be moved by the player.
	 * @param x The initial X-position of the block.
	 * @param y The initial Y-position of the block.
	 */
	public BlockImmovable(final int x, final int y) {
		super(
				(x * 26f) + (ResourceManager.getImage(HedgehogHavoc.BLOCK_IMMOVABLE_IMG).getWidth() / 2),
				(y * 26f) + (ResourceManager.getImage(HedgehogHavoc.BLOCK_IMMOVABLE_IMG).getHeight() / 2));
		addImageWithBoundingBox(ResourceManager.getImage(HedgehogHavoc.BLOCK_IMMOVABLE_IMG));
	}
}