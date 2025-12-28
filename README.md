# Ex2 – Basic Object-Oriented Programming & 2D Maze Algorithms

# Course: Introduction to Computer Science
# School: Ariel University – School of Computer Science
# Language: Java

## Overview
This exercise combines foundational object-oriented programming with classic breadth-first-search (BFS) algorithms on a 2D grid. The codebase provides a reusable `Map` implementation, visualization tools, and test coverage so you can explore pathfinding, region detection, and drawing utilities on raster-style maps.

## Quick start
1. Compile the sources:
   ```bash
   javac src/*.java
   ```
2. Run the GUI on the default map:
   ```bash
   java -cp src Ex2_GUI
   ```
3. Run the JUnit tests (requires JUnit on the classpath):
   ```bash
   javac -cp ".:src:junit-4.13.2.jar:hamcrest-core-1.3.jar" src/*.java
   java  -cp ".:src:junit-4.13.2.jar:hamcrest-core-1.3.jar" org.junit.runner.JUnitCore MapTest Index2DTest
   ```

## Project structure
- `Map2D.java`, `Pixel2D.java` – Interfaces that define the 2D grid contract.
- `Index2D.java` – Concrete pixel implementation used throughout the project.
- `Map.java` – Core 2D map implementation with drawing tools and BFS-based algorithms.
- `Ex2_GUI.java` – GUI for editing grids, running fills, and visualizing shortest paths.
- `StdDraw.java` – Lightweight drawing helper adapted for the GUI.
- `MapTest.java`, `Index2DTest.java` – JUnit test suites validating map behavior and pixel utilities.

## Map algorithms and utilities
The `Map` class encapsulates the 2D grid and all related algorithms.

### Initialization and dimensions
- Constructors support rectangular grids, square grids, or deep-copying existing matrices.
- `init(int w, int h, int v)` and `init(int[][] arr)` reset the map to the requested size or data while validating dimensions.
- `getMap()`, `getWidth()`, and `getHeight()` expose deep copies and size information so external callers cannot mutate internal state accidentally.

### Pixel access and mutation
- `getPixel(int x, int y)` and `getPixel(Pixel2D p)` read cell values with bounds validation.
- `setPixel(int x, int y, int v)` and `setPixel(Pixel2D p, int v)` mutate cells while ensuring the map is initialized and coordinates are valid.
- `isInside(Pixel2D p)` guards against out-of-bounds access, and `sameDimensions(Map2D p)` compares grid shapes before combining maps.

### Map arithmetic and scaling
- `addMap2D(Map2D p)` adds another map cell-by-cell when dimensions match.
- `mul(double scalar)` scales every cell by a numeric factor.
- `rescale(double sx, double sy)` resizes the grid with nearest-neighbor sampling, rounding new dimensions and preserving values where possible.

### Drawing primitives
- `drawCircle(Pixel2D center, double rad, int color)` colors all pixels within a given radius.
- `drawLine(Pixel2D p1, Pixel2D p2, int color)` rasterizes a straight line between two points, choosing the dominant axis for smoothness.
- `drawRect(Pixel2D p1, Pixel2D p2, int color)` fills the rectangle formed by two opposing corners, including boundaries.

### Flood fill
- `fill(Pixel2D xy, int new_v, boolean cyclic)` repaints the connected component of a starting pixel using depth-first search. Components are defined by 4-neighbor connectivity and can wrap around the grid when `cyclic` is true.

### Distance mapping
- `allDistance(Pixel2D start, int obsColor, boolean cyclic)` performs BFS to compute the shortest distance from `start` to every reachable cell, marking obstacles with `obsColor` and unreachable cells with `-1`. Supports optional cyclic wrapping on both axes.

### Shortest path reconstruction
- `shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic)` builds a BFS distance map (via `allDistance`) and walks backward from the destination to reconstruct the minimal-length route as an array of consecutive pixels. Returns `null` when endpoints are blocked or disconnected.

### Helpers and connectivity notes
- `equals(Object ob)` compares maps by dimensions and cell values for testing and validation.
- `drawLine`, `drawRect`, `drawCircle`, and `fill` all leverage shared bounds checks and the `wrap` helper to support cyclic grids where enabled.
- Connected components use 4-neighbor adjacency (up, down, left, right) to align with BFS traversal rules across all algorithms.

## GUI usage
The GUI (`Ex2_GUI`) lets you interactively edit maps and run algorithms:
- Draw points, lines, rectangles, and circles with the toolbar and color palette.
- Toggle cyclic mode (press `C`) to wrap paths and fills across edges.
- Use the action menu to run flood fill or visualize shortest paths; paths are drawn as colored overlays.
- Load and save maps from simple text files (`w h` header followed by rows of integers).

Closest path visualization:

<img width="1048" height="976" alt="image" src="https://github.com/user-attachments/assets/0f19b876-c365-45c3-9e3b-110be2224554" />
# Note for image - green represent the shortest path, black represents the obsticles.

## Testing
- Unit tests: `MapTest.java` covers map operations and algorithms; `Index2DTest.java` validates pixel utilities.
- Manual checks: run `Ex2_GUI` to verify drawing, filling, and shortest-path overlays on custom maps.
