# Maze Generator

Maze Generator is a program that allows users to randomly generate mazes with specified dimensions. It includes a GUI and features the ability for users to manually draw mazes, although this method is not very efficient.

## About
This project aims to provide a tool for generating mazes for various purposes, such as entertainment or educational use. Users can either generate mazes randomly or manually draw them using the provided GUI.

## How to Run

### 1. Prerequisites:

    - Intellij IDEA Community 2023 installed on your system.
    - Amazon Corretto 17 JDK installed on your system.
    
### 2. Installation:

    - Clone or download the code to your IDE.
    
### 3. Running the Program:

    - Open Intellij IDEA Community 2023.
    - Import the project by selecting File > Open and navigating to the directory 
      where you cloned or downloaded the code.
    - You can also clone the repo directly from the IDE.
    - Locate the run button, typically in the top-right corner of the IDE or directly in the MazeEditor class.
    - Start the program by clicking the run button.
    - Click the generate button on the right pane to randomly generate mazes.
    - You can change the dimensions of the maze by changing the values of the 2 inputs. It starts at 100x100.
    - You can toggle the solution, to turn on or off the solution path. 

## Issues

1. **Database Connection:** Connecting to the database no longer works.
2. **Glitchy Behavior on Resize:** The entire app is glitchy when resizing.
3. **Suboptimal Maze Solution:** The maze solution is not always optimal; sometimes it traverses the entire maze to find the exit.
4. **Visibility of Maze Solution:** The visibility of the maze solution is not very intuitive.
