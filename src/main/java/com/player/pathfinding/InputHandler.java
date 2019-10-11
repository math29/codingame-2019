package com.player.pathfinding;

import java.awt.*;

import com.player.model.Board;
import com.player.model.Cell;
import com.player.model.Coord;

class InputHandler {

  private static final int WIDTH = 30;
  private static final int HEIGHT = 15;

  Graph readBoard(final Board board) {

    Graph graph = new Graph(WIDTH, HEIGHT);

    for (int i = 0; i < WIDTH; i++) {
      for (int j = 0; j < HEIGHT; j++) {
        Cell cell = board.getCell(new Coord(i, j));
        if (board.isDangerousEnemyInRange(cell)) {
          Node n = new Node(i, j, "OBSTACLE");
          graph.setMapCell(new Point(i, j), n);
        } else {
          Node n = new Node(i, j, "NORMAL");
          graph.setMapCell(new Point(i, j), n);
        }
      }
    }
    return graph;
  }
}
