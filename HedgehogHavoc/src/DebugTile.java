import jig.Entity;
import jig.ResourceManager;

class DebugTile extends Entity {
	/**
	 * Create a new tile that will show pathing for the badgers.
	 * @param x The initial X position within the grid.
	 * @param y The initial Y position within the grid.
	 * @param HUDOffset The offset in the Y-direction for the HUD.
	 */
	public DebugTile(final int x, final int y, final int HUDOffset) {
		super(
				(x * 26f) + (ResourceManager.getImage(HedgehogHavoc.PATH_DEBUG_IMG).getWidth() / 2),
				(y * 26f) + (ResourceManager.getImage(HedgehogHavoc.PATH_DEBUG_IMG).getHeight() / 2) + HUDOffset);
		addImageWithBoundingBox(ResourceManager.getImage(HedgehogHavoc.PATH_DEBUG_IMG));
		antiAliasing = false;
	}
	
	/**
	 * Update the DebugTile.
	 * @param delta The delta value for which to translate the velocity of the DebugTile.
	 */
	public void update(final int delta) {
	}
}