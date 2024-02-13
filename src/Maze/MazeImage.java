package Maze;

import java.awt.image.BufferedImage;

/** This class is the MazeImage class where the information for images are stored. The integer properties
 * x_pos and y_pos are both used to indicate the images position on a maze. The image property is a
 * BufferedImage object which is used to store the actual image data
 */
public class MazeImage {
    private int x_pos;
    private int y_pos;
    private BufferedImage image;

    /** This is a getter function for MazeImage x_pos
     * @return returns MazeImage's x_pos
     */
    public int getX_pos() {
        return x_pos;
    }
    /** This is a getter function for MazeImage y_pos
     * @return returns MazeImage's y_pos
     */
    public int getY_pos() {
        return y_pos;
    }

    /** This is a getter function for MazeImage image
     * @return returns MazeImage's image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Constructor for class MazeImage.
     * @param x the x coordinate of the MazeImage ontop of the Maze its it exists on.
     * @param y the y coordinate of the MazeImage ontop of the Maze its it exists on.
     * @param image The image data for the actual image being stored
     */
    public MazeImage(int x, int y, BufferedImage image){
        this.image = image;
        this.x_pos = x;
        this.y_pos = y;
    }
}
