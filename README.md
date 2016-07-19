# Hidden surface removal using the painter’s algorithm 
## Description
This project allows the user to place a point of view inside a scene composed of colored segments and render the scene as seen by the point of view. To do so, the scene is transformed into a Binary Space Partitioning tree using one of three creation heuristics (designed to minimize the size or height of the tree) and then rendered using the painter's algorithm.

![alt text](https://github.com/Skar0/sddII/blob/master/report/gui.png "GUI")

## Structure
The source code of the project can be found in src/be/ac/umons

    .
    ├── bsp       #Implementation of BSP tree data structure, creation heuristics and Segment data structure
    ├── cui       #Console user interface (allowing performance tests between creation heuristics)
    ├── gui       #Graphical user interface (allowing the user to place a point of view and render the scene)
    ├── painter   #Painter's algorithm and point of view implementation
    └── test      #Tests

## How to run
### GUI
Run gui/TestGui.java to launch the gui. Once a heuristic and a file containing a scene is chosen (example files can be found in sddII/assets/first/), the user can place a point of view by drawing it or choosing an angle and then render the scene as viewed by the point of view.

