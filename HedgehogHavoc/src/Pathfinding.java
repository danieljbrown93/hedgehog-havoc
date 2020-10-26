import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Path finding class for badgers to find the shortest route to the hedgehog.
 * 
 * @author Daniel Brown
 *
 */
class Pathfinding {	
	public List<Tile> BFS(Tile[][] tiles, int startX, int startY) {
		resetTiles(tiles);
		Queue<Tile> queue = new LinkedList<Tile>();
		tiles[startX][startY].setDiscovered(true);
		queue.add(tiles[startX][startY]);
		while (!queue.isEmpty()) {
			Tile currentTile = queue.remove();
			if (currentTile.getIsHedgehog()) {
				return getRoute(currentTile);
			}
			
			int currentX = currentTile.getXPos();
			int currentY = currentTile.getYPos();
			if ((tiles[currentX + 1][currentY].getIsGround() || tiles[currentX + 1][currentY].getIsHedgehog()) && !tiles[currentX + 1][currentY].isDiscovered()) {
				tiles[currentX + 1][currentY].setDiscovered(true);
				tiles[currentX + 1][currentY].setParent(currentTile);
				queue.add(tiles[currentX + 1][currentY]);
			}
			
			if ((tiles[currentX - 1][currentY].getIsGround() || tiles[currentX - 1][currentY].getIsHedgehog()) && !tiles[currentX - 1][currentY].isDiscovered()) {
				tiles[currentX - 1][currentY].setDiscovered(true);
				tiles[currentX - 1][currentY].setParent(currentTile);
				queue.add(tiles[currentX - 1][currentY]);
			}
			
			if ((tiles[currentX][currentY + 1].getIsGround() || tiles[currentX][currentY + 1].getIsHedgehog()) && !tiles[currentX][currentY + 1].isDiscovered()) {
				tiles[currentX][currentY + 1].setDiscovered(true);
				tiles[currentX][currentY + 1].setParent(currentTile);
				queue.add(tiles[currentX][currentY + 1]);
			}
			
			if ((tiles[currentX][currentY - 1].getIsGround() || tiles[currentX][currentY - 1].getIsHedgehog()) && !tiles[currentX][currentY - 1].isDiscovered()) {
				tiles[currentX][currentY - 1].setDiscovered(true);
				tiles[currentX][currentY - 1].setParent(currentTile);
				queue.add(tiles[currentX][currentY - 1]);
			}
		}
		
		return new ArrayList<>();
	}
	
	private void resetTiles(Tile[][] tiles) {
		for (int i = 0; i < 23; i++) {
			for (int j = 0; j < 23; j++) {
				tiles[i][j].setDiscovered(false);
				tiles[i][j].setParent(null);
				tiles[i][j].setScore(0);
				tiles[i][j].setDebugActive(false);
			}
		}
	}
	
	private List<Tile> getRoute(Tile tile) {
		ArrayList<Tile> path = new ArrayList<Tile>();
		Tile tempTile = tile;
		while (tempTile.getParent() != null) {
			path.add(0, tempTile);
			tempTile.setDebugActive(true);
			tempTile = tempTile.getParent();
		}
		
		path.add(0, tempTile);
		return path;
	}
}