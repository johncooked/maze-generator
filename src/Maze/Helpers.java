package Maze;

import java.util.List;
import java.util.Stack;

public class Helpers {
    /**
     * Calculate the distance between two cells' x coordinates.
     * @param startCell previous cell
     * @param goalCell current cell
     * @return distance integer
     */
    public static int distX(Cell startCell, Cell goalCell) {
        int startCellX = startCell.start;
        int goalCellX = goalCell.start;

        return goalCellX - startCellX;
    }

    /**
     * Calculate the distance between two cells' y coordinates.
     * @param startCell previous cell
     * @param goalCell current cell
     * @return distance integer
     */
    public static int distY(Cell startCell, Cell goalCell) {
        int startCellX = startCell.end;
        int goalCellX = goalCell.end;

        return goalCellX - startCellX;
    }

    /**
     * Check if cell is null, if not add it to array.
     * @param cell current cell to check.
     * @param toVisit queue array.
     * @param neighbour neighbour array.
     */
    public static void saveCell(Cell cell, Stack<Cell> toVisit, List<Cell> neighbour) {
        if (cell != null) {
            toVisit.push(cell);
            neighbour.add(cell);
        }
    }
}
