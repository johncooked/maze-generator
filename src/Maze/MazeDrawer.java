package Maze;
import java.awt.*;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import static Maze.Cell.cellSize;

/**
 * This class is the manual maze creation mode. It is done by implementing a mouse listener.
 * The class stores the x and y values of the mouse click location and use it to determine where to draw the lines.
 * MazeDrawer needs access to width and height input of the maze in order to determine if a line should be where the user clicked.
 * The class will also hold the graphics component and will store an instance of the draw area and display it.
 */
// Code from http://www.ssaurel.com/blog/learn-how-to-make-a-swing-painting-and-drawing-application/
public class MazeDrawer extends JComponent{
    private Image image;

    private Graphics2D g2;

    private int mouseX, mouseY;
    private final int rows, cols;

    int lineWidth = 3;
    int lineHeight;
    int zoomIndex;

    /**
     * This is the constructor for this class. When called it initiate the mouse listener.
     * @param width maze width from user input.
     * @param height maze height from user input.
     */
    public MazeDrawer(int width, int height) {
        this.rows = width/cellSize;
        System.out.println(rows);
        this.cols = height/cellSize;

        if (width <= 10 && HEIGHT <= 10) {
            zoomIndex = 10;
        } else if (width <= 20 && HEIGHT <= 20) {
            zoomIndex = 20;
        } else if (width <= 30 && HEIGHT <= 30) {
            zoomIndex = 30;
        } else if (width <= 40 && HEIGHT <= 40) {
            zoomIndex = 40;
        } else if (width <= 50 && HEIGHT <= 50) {
            zoomIndex = 50;
        } else if (width <= 60 && HEIGHT <= 60) {
            zoomIndex = 60;
        } else if (width <= 70 && HEIGHT <= 70) {
            zoomIndex = 70;
        } else if (width <= 80 && HEIGHT <= 80) {
            zoomIndex = 80;
        } else if (width <= 90 && HEIGHT <= 90) {
            zoomIndex = 90;
        }
        else {
            zoomIndex = 100;
        }

        lineHeight = cellSize * zoomIndex;

        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                mouseX = e.getX();
                mouseY = e.getY();

                int drawX = (mouseX/rows) * rows;
                int drawY = (mouseY/cols) * cols;

                g2.fillRect(drawX,drawY, lineWidth, lineHeight);
                repaint();

            }
        });
    }

    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);
            g2 = (Graphics2D) image.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clear();
        }
        g.drawImage(image, 0, 0, null);
    }

    /**
     * This method clears the current draw area.
     */
    public void clear() {
        g2.setPaint(Color.white);
        g2.fillRect(0,0, getSize().width, getSize().height);
        g2.setPaint(Color.black);
        repaint();
    }

    /**
     * This method set the line drawn to be horizontal by swapping the width and height values.
     */
    public void horizontalWall() {
        lineWidth = cellSize * zoomIndex;
        lineHeight = 3;
    }

    /**
     * This method set the line drawn to be vertical, this is the default mode.
     */
    public void verticalWall() {
        lineWidth = 3;
        lineHeight = cellSize * zoomIndex;
    }

    /**
     * Set the paint color to match with the background. Drawing over an existing line with this will be like erasing the line.
     */
    public void erase() {
        g2.setColor(Color.white);
    }

    /**
     * Change the paint color to black.
     */
    public void draw() {
        g2.setPaint(Color.black);
    }
}
