package Maze;

import java.util.ArrayList;
import java.util.List;

/**
 * The Solver class contains the algorithm to mark which cell is part of the solution path.
 */
public class Solver {
    /**
     * This solution uses the same backtracking algorithm as the maze generation. It will mark the cells that lead to the maze exit starting from the maze entrance.
     * @param mazeEntrance start location of the maze.
     * @param mazeExit end location of the maze.
     * @param visited generated maze.
     */
    public void showSolution (Cell mazeEntrance, Cell mazeExit, List<Cell> visited) {
        List<Cell> wasHere = new ArrayList<>();

        mazeEntrance.isSol = true;
        mazeExit.isSol = true;

        Cell lastCell = mazeEntrance;

        for (int i = 0; i <= visited.lastIndexOf(mazeExit); i++) {
            int lastCellX = lastCell.start;
            int lastCellY = lastCell.end;

            Cell currentCell = visited.get(i);
            int currentCellX = currentCell.start;
            int currentCellY = currentCell.end;

            int x = currentCellX - lastCellX;
            if (x == 1) {
                if (!currentCell.walls[3] && !lastCell.walls[1]) {
                    currentCell.isSol = !wasHere.contains(currentCell);
                    wasHere.add(currentCell);
                }
            } else if (x == -1) {
                if (!currentCell.walls[1] && !lastCell.walls[3]) {
                    currentCell.isSol = !wasHere.contains(currentCell);
                    wasHere.add(currentCell);
                }
            }

            int y = currentCellY - lastCellY;
            if (y == 1) {
                if (!currentCell.walls[0] && !lastCell.walls[2]) {
                    currentCell.isSol = !wasHere.contains(currentCell);
                    wasHere.add(currentCell);
                }
            } else if (y == -1) {
                if (!currentCell.walls[2] && !lastCell.walls[0]) {
                    currentCell.isSol = !wasHere.contains(currentCell);
                    wasHere.add(currentCell);
                }
            }

            lastCell = currentCell;
        }
    }
}

