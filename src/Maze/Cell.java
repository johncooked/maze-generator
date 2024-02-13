package Maze;

import java.util.List;
import static Maze.Maze.cols;
import static Maze.Maze.rows;

/** This class is Cell and is used to create, store and compare coordinates for each cell of the maze.
 */
//class from https://github.com/TheTurkeyDev/YouTube-Videos/blob/master/MazeGeneration%20-%20Prim/src/Vector2I.java
public class Cell {
    public int start;
    public int end;
    public static int cellSize = 1;
    public boolean[] walls;
    public boolean visited;
    public boolean isSol;

    /**
     * Constructor for class Cell.
     * @param start takes an integer and store it as the x coordinate of the cell.
     * @param end takes an integer and store it as the y coordinate of the cell.
     */
    public Cell(int start, int end) {
        this.start = start;
        this.end = end;
        this.visited = false;
        this.isSol = false;
        this.walls = new boolean[]{true, true, true, true};
    }

    /**
     * Override the toString method to return the cell coordinates in (start, end) format.
     * @return Coordinate of cell. For example, (0, 1).
     */
    @Override
    public String toString() {
        return "(" + start + ", " + end + ")";
    }

    /**
     * Override the equals method to allow comparison of the coordinates.
     * @param obj takes the current cell to compare
     * @return return true if the start and end values matches and false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cell cell)) {
            return false;
        }

        return cell.start == start && cell.end == end;
    }

    /**
     * Generate a hash of the cell.
     * @return integer value generated by hashing algorithm.
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     *Check if the above neighbouring cell is inbound. Returns the cell if it is, otherwise returns null.
     * @param currentCell this is the cell object that needs to be checked.
     * @param grid this grid provides the boundary to check against.
     * @return above neighbour cell.
     */
    public static Cell checkAbove(Cell currentCell, List<Cell> grid) {
        Cell above = null;
        int currentCellX = currentCell.start;
        int currentCellY = currentCell.end;

        int indexAbove = currentCellX * cols + (currentCellY - 1) ;
        if (indexAbove >= 0 && indexAbove < grid.size() && currentCellY != 0) {
            above = grid.get(indexAbove);
        }

        return above;
    }

    /**
     *Check if the right neighbouring cell is inbound. Returns the cell if it is, otherwise returns null.
     * @param currentCell this is the cell object that needs to be checked.
     * @param grid this grid provides the boundary to check against.
     * @return right neighbour cell.
     */
    public static Cell checkRight(Cell currentCell, List<Cell> grid) {
        Cell right = null;
        int currentCellX = currentCell.start;
        int currentCellY = currentCell.end;

        int indexRight = (currentCellX + 1) * cols + currentCellY  ;
        if (indexRight >= 0 && indexRight < grid.size() && currentCellX != rows - 1) {
            right = grid.get(indexRight);
        }

        return right;
    }

    /**
     *Check if the bottom neighbouring cell is inbound. Returns the cell if it is, otherwise returns null.
     * @param currentCell this is the cell object that needs to be checked.
     * @param grid this grid provides the boundary to check against.
     * @return bottom neighbour cell.
     */
    public static Cell checkBottom(Cell currentCell, List<Cell> grid) {
        Cell bottom = null;
        int currentCellX = currentCell.start;
        int currentCellY = currentCell.end;

        int indexBelow = currentCellX * cols + (currentCellY + 1) ;
        if (indexBelow >= 0 && indexBelow < grid.size() && currentCellY != cols - 1) {
            bottom = grid.get(indexBelow);
        }

        return bottom;
    }

    /**
     *Check if the left neighbouring cell is inbound. Returns the cell if it is, otherwise returns null.
     * @param currentCell this is the cell object that needs to be checked.
     * @param grid this grid provides the boundary to check against.
     * @return left neighbour cell.
     */
    public static Cell checkLeft(Cell currentCell, List<Cell> grid) {
        Cell left = null;
        int currentCellX = currentCell.start;
        int currentCellY = currentCell.end;

        int indexLeft = (currentCellX - 1) * cols + currentCellY ;
        if (indexLeft >= 0 && indexLeft < grid.size() && currentCellX != 0) {
            left = grid.get(indexLeft);
        }

        return left;
    }
}
