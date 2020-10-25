import jig.Entity;
import jig.ResourceManager;

class Badger extends Entity {
	public int moveCount;
	public String moveDir;
	
	/**
	 * Create a new Hedgehog that will be controlled by the player.
	 * @param x The initial X position within the grid.
	 * @param y The initial Y position within the grid.
	 */
	public Badger(final int x, final int y, final int HUDOffset) {
		super(
				(x * 26f) + (ResourceManager.getImage(HedgehogHavoc.BADGERRIGHT_IMG).getWidth() / 2),
				(y * 26f) + (ResourceManager.getImage(HedgehogHavoc.BADGERRIGHT_IMG).getHeight() / 2) + HUDOffset);
		addImageWithBoundingBox(ResourceManager.getImage(HedgehogHavoc.BADGERRIGHT_IMG));
		moveDir = "";
		antiAliasing = false;
	}
	
	public Badger clone(Badger badger) {
		Badger newBadger = new Badger(0, 0, 0);
		newBadger.setPosition(badger.getPosition());
		newBadger.moveCount = badger.moveCount;
		newBadger.moveDir = badger.moveDir;
		return newBadger;
	}
	
	/**
	 * Update the Hedgehog.
	 * @param delta The delta value for which to translate the velocity of the Hedgehog.
	 */
	public void update(final int delta) {
	}
}