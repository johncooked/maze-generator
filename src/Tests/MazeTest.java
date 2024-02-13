package Tests;

import Maze.Maze;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;


public class MazeTest {

    Maze maze;

    @BeforeEach
    public void ConstructMaze() throws Exception {
        maze = new Maze(20,20);
    }

    @Test
    public void testGetHeight() {
        int height = maze.getMazeHeight();
        assertEquals(20, height);
    }

    @Test
    public void testGetWidth() {
        int width = maze.getMazeWidth();
        assertEquals(20, width);
    }

    @Test
    public void OverMaximumHeight() {
        assertThrows(Exception.class, () -> {
            maze = new Maze(40,600);
        });
    }

    @Test
    public void OverMaximumWidth() {
        assertThrows(Exception.class, () -> {
            maze = new Maze(600,30);
        });
    }

    @Test
    public void UnderMinimumWidth() {
        assertThrows(Exception.class, () -> {
            maze = new Maze(-20,30);
        });
    }

    @Test
    public void UnderMinimumHeight() {
        assertThrows(Exception.class, () -> {
            maze = new Maze(40,-40);
        });
    }

    @Test
    public void BoundaryTest1() throws Exception {
        maze = new Maze(99,20);
        int width = maze.getMazeWidth();
        assertEquals(width,99);
    }

    @Test
    public void BoundaryTest2() throws Exception {
        maze = new Maze(40,99);
        int height = maze.getMazeHeight();
        assertEquals(height,99);
    }

    @Test
    public void BoundaryTest3() throws Exception {
        maze = new Maze(10,30);
        int width = maze.getMazeWidth();
        assertEquals(width,10);
    }

    @Test
    public void BoundaryTest4() throws Exception {
        maze = new Maze(20,10);
        int height = maze.getMazeHeight();
        assertEquals(height,10);
    }

    @Test
    public void BoundaryTest5() {
        assertThrows(Exception.class, () -> {
            maze = new Maze(1000,40);
        });
    }

    @Test
    public void BoundaryTest6() {
        assertThrows(Exception.class, () -> {
            maze = new Maze(50,1000);
        });
    }

    @Test
    public void BoundaryTest7() {
        assertThrows(Exception.class, () -> {
            maze = new Maze(0,30);
        });
    }

    @Test
    public void BoundaryTest8() {
        assertThrows(Exception.class, () -> {
            maze = new Maze(60,0);
        });
    }

}
