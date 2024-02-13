package Maze;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static Maze.Cell.*;
import static Maze.Helpers.*;

/** This class is the maze itself and is where the maze data is stored
 * and generation algorithms are implemented. The size, name, date made and author are all stored within
 * the properties of the object. The image's property also stores the images that are on the maze within
 * an array of object Image
 */
public class Maze extends JPanel {
    private final int mazeWidth;
    private final int mazeHeight;

    public static boolean showSolution = false;
    public static int rows;
    public static int cols;
    public static Cell mazeEntrance;
    public static Cell mazeExit;
    private int zoomIndex;
    private Random rand = new Random();

    private List<Cell> grid = new ArrayList<>();
    protected List<Cell> maze = new ArrayList<>();
    ArrayList<MazeImage> images;

    /**
     * Constructor for class Maze.
     * @param inputHeight takes an integer representing the height of the maze being constructed.
     * @param inputWidth takes an integer representing the width of the maze being constructed.
     */
    public Maze(int inputWidth,int inputHeight) throws Exception {
        this.images = new ArrayList<>();
        zoomIndex = 0;

        this.mazeWidth = inputWidth;
        this.mazeHeight = inputHeight;
        rows = inputWidth / cellSize;
        cols = inputHeight / cellSize;
        if((rows > 100) || rows < 1) {throw new Exception("Out of Bound Height");}
        if((cols > 100) || cols < 1) {throw new Exception("Out of bound Width");}
    }

    /**
     * Provides public access to Maze's height.
     * @return the Maze's height.
     */
    public int getMazeHeight() {
        return mazeHeight;
    }


    /**
     * Provides public access to Maze's width.
     * @return the Maze's width.
     */
    public int getMazeWidth(){
        return mazeWidth;
    }

    /**
     * Provides public access to Maze's zoomIndex.
     * @return the Maze's zoomIndex.
     */
    public int getZoomIndex(){return zoomIndex;}

    /**
     * This method generates a grid for random maze generation
     */
    public void createGrid () {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                grid.add(new Cell(x, y));
            }
        }
    }

    /**
     * This method generates the maze with size x and y which are the axes that are desired
     */
    //Code from https://github.com/TheTurkeyDev/YouTube-Videos/blob/master/MazeGeneration%20-%20Prim/src/MazeGenerator.java
    public void randomGenerate() {
        Stack<Cell> toVisit = new Stack<>();
        List<Cell> neighbour = new ArrayList<>();

        //Set first and last item of grid array as maze entrance and exit
        mazeEntrance = grid.get(0);
        mazeExit = grid.get(grid.size() - 1);

        Cell lastCell = mazeEntrance;

        //Create entrance and exit openings
        mazeEntrance.walls[0] = false;
        mazeExit.walls[2] = false;

        //Initialize queue
        toVisit.push(mazeEntrance);

        while (toVisit.size() > 0) {
            Cell currentCell;

            if (neighbour.isEmpty()) {
                currentCell = toVisit.pop();
            } else {
                for (int i = 0; i < neighbour.size(); i++) {
                    Cell thisCell = neighbour.get(i);
                    if (maze.contains(thisCell)) {
                        neighbour.remove(thisCell);
                    }
                }
                int randomIndex = rand.nextInt(neighbour.size());
                currentCell = neighbour.remove(randomIndex);
                neighbour.clear();
            }

            if (maze.contains(currentCell)) {
                continue;
            }

            currentCell.visited = true;
            maze.add(currentCell);

            //Check if neighbours are out of bounds to prevent index out of bound
            Cell aboveNeighbour = checkAbove(currentCell,grid);
            saveCell(aboveNeighbour,toVisit,neighbour);

            Cell rightNeighbour = checkRight(currentCell,grid);
            saveCell(rightNeighbour,toVisit,neighbour);

            Cell bottomNeighbour = checkBottom(currentCell,grid);
            saveCell(bottomNeighbour,toVisit,neighbour);

            Cell leftNeighbour = checkLeft(currentCell,grid);
            saveCell(leftNeighbour,toVisit,neighbour);

            // Set walls right and left
            int x = distX(lastCell, currentCell);
            if (x == 1) {
                currentCell.walls[3] = false;
                lastCell.walls[1] = false;
            } else if (x == -1) {
                currentCell.walls[1] = false;
                lastCell.walls[3] = false;
            } else if (x > 1 || x < -1){
                if (rightNeighbour != null) {
                    rightNeighbour.walls[3] = false;
                    currentCell.walls[1] = false;
                }

                if (leftNeighbour != null) {
                    leftNeighbour.walls[1] = false;
                    currentCell.walls[3] = false;
                }
            }

            //Set walls top and bottom
            int y = distY(lastCell, currentCell);
            if (y == 1) {
                currentCell.walls[0] = false;
                lastCell.walls[2] = false;
            } else if (y == -1) {
                currentCell.walls[2] = false;
                lastCell.walls[0] = false;
            } else if (y > 1 || y < -1) {
                if (aboveNeighbour != null) {
                    aboveNeighbour.walls[2] = false;
                    currentCell.walls[0] = false;
                }

                if (bottomNeighbour != null) {
                    bottomNeighbour.walls[0] = false;
                    currentCell.walls[2] = false;
                }
            }
            lastCell = currentCell;
        }
    }

    /**
     *This methods adds a MazeImage object to the Maze's images list
     */
    public void addImage(BufferedImage image, int x, int y) throws Exception {
        if((y/7 > mazeHeight) || y < 1) {throw new Exception("Out of Bound Height");}
        if((x/7 > mazeWidth) || x < 1) {throw new Exception("Out of bound Width");}
        this.images.add( new MazeImage(x,y,image));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        if(this.images.size() > 0){
            for(MazeImage image: images){
                int imageSize = cellSize*this.zoomIndex*2;
                //CODE FROM https://stackoverflow.com/questions/16497853/scale-a-bufferedimage-the-fastest-and-easiest-way
                java.awt.Image tmp = image.getImage().getScaledInstance(imageSize, imageSize, BufferedImage.SCALE_FAST);
                BufferedImage buffered = new BufferedImage(imageSize,imageSize,BufferedImage.TYPE_INT_RGB);
                buffered.getGraphics().drawImage(tmp, 0, 0, null);

                //Image a = images[0].image.getScaledInstance(20,20,Image.SCALE_SMOOTH);
                g2.drawImage(buffered, null, image.getX_pos(), image.getY_pos());
            }
        }


        // Make line width bigger and make sure color is black
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.BLACK);

        // Determine what size to display the maze
        int zoomIndex;
        if (mazeWidth <= 10 && mazeHeight <= 10) {
            zoomIndex = 80; this.zoomIndex = 80;
        } else if (mazeWidth <= 20 && mazeHeight <= 20) {
            zoomIndex = 40; this.zoomIndex = 40;
        } else if (mazeWidth <= 30 && mazeHeight <= 30) {
            zoomIndex = 25; this.zoomIndex = 25;
        } else if (mazeWidth <= 40 && mazeHeight <= 40) {
            zoomIndex = 20; this.zoomIndex = 20;
        } else if (mazeWidth <= 50 && mazeHeight <= 50) {
            zoomIndex = 15; this.zoomIndex = 15;
        } else if (mazeWidth <= 60 && mazeHeight <= 60) {
            zoomIndex = 13; this.zoomIndex = 13;
        } else if (mazeWidth <= 70 && mazeHeight <= 70) {
            zoomIndex = 11; this.zoomIndex = 11;
        } else if (mazeWidth <= 80 && mazeHeight <= 80) {
            zoomIndex = 10; this.zoomIndex = 10;
        } else if (mazeWidth <= 90 && mazeHeight <= 90) {
            zoomIndex = 9; this.zoomIndex = 9;
        }
        else {
            zoomIndex = 8; this.zoomIndex = 8;
        }

        //Top border
        g.drawLine(cellSize * zoomIndex,
                0,
                rows * cellSize * zoomIndex,
                0);

        //Right border
        g.drawLine(rows * cellSize * zoomIndex,
                0,
                rows * cellSize * zoomIndex,
                cols * cellSize * zoomIndex);

        //Bottom border
        g.drawLine(0,
                cols * cellSize * zoomIndex,
                (rows - 1) * cellSize * zoomIndex,
                cols * cellSize * zoomIndex);

        //Left border
        g.drawLine(0,
                0,
                0,
                cols * cellSize * zoomIndex);

        for (Cell cell : maze) {
            g2.setColor(Color.BLACK);

            Cell checkingCell = cell;

            int x = checkingCell.start * cellSize;
            int y = checkingCell.end * cellSize;

//            boolean isSol = false;
            boolean isSol = checkingCell.isSol;

            boolean topWall = checkingCell.walls[0];
            boolean rightWall = checkingCell.walls[1];
            boolean bottomWall = checkingCell.walls[2];
            boolean leftWall = checkingCell.walls[3];

            if (showSolution) {
                if (isSol) {
                    if (topWall) {
                        g2.drawLine(x * zoomIndex, y * zoomIndex, (x + cellSize) * zoomIndex, y * zoomIndex);
                    }

                    //Right Wall
                    if (rightWall) {
                        g2.drawLine((x + cellSize) * zoomIndex, y * zoomIndex, (x + cellSize) * zoomIndex, (y + cellSize) * zoomIndex);
                    }

                    //Bottom Wall
                    if (bottomWall) {
                        g2.drawLine((x + cellSize) * zoomIndex, (y + cellSize) * zoomIndex, x * zoomIndex, (y + cellSize) * zoomIndex);
                    }

                    //Left Wall
                    if (leftWall) {
                        g2.drawLine(x * zoomIndex, (y + cellSize) * zoomIndex, x * zoomIndex, y * zoomIndex);
                    }

                    g2.setColor(Color.red);
                    g2.fillRect((x + (cellSize/ 3 * zoomIndex)) * zoomIndex , (y + cellSize/ 3) * zoomIndex, (cellSize * zoomIndex), (cellSize* zoomIndex));
                }
                g2.setColor(Color.BLACK);
                // Top Wall
                if (topWall) {
                    g2.drawLine(x * zoomIndex, y * zoomIndex, (x + cellSize) * zoomIndex, y * zoomIndex);
                }

                //Right Wall
                if (rightWall) {
                    g2.drawLine((x + cellSize) * zoomIndex, y * zoomIndex, (x + cellSize) * zoomIndex, (y + cellSize) * zoomIndex);
                }

                //Bottom Wall
                if (bottomWall) {
                    g2.drawLine((x + cellSize) * zoomIndex, (y + cellSize) * zoomIndex, x * zoomIndex, (y + cellSize) * zoomIndex);
                }

                //Left Wall
                if (leftWall) {
                    g2.drawLine(x * zoomIndex, (y + cellSize) * zoomIndex, x * zoomIndex, y * zoomIndex);
                }
            }
            g2.setColor(Color.BLACK);
            // Top Wall
            if (topWall) {
                g2.drawLine(x * zoomIndex, y * zoomIndex, (x + cellSize) * zoomIndex, y * zoomIndex);
            }

            //Right Wall
            if (rightWall) {
                g2.drawLine((x + cellSize) * zoomIndex, y * zoomIndex, (x + cellSize) * zoomIndex, (y + cellSize) * zoomIndex);
            }

            //Bottom Wall
            if (bottomWall) {
                g2.drawLine((x + cellSize) * zoomIndex, (y + cellSize) * zoomIndex, x * zoomIndex, (y + cellSize) * zoomIndex);
            }

            //Left Wall
            if (leftWall) {
                g2.drawLine(x * zoomIndex, (y + cellSize) * zoomIndex, x * zoomIndex, y * zoomIndex);
            }
        }
    }
}
