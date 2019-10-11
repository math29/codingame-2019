package com.player.pathfinding;

import java.awt.*;
import java.util.List;

import com.player.model.Board;
import com.player.model.Coord;

public class AStarAlgorithm {

  public static void execute(final Board board, final Coord startPosition, final Coord targetPosition) {
    System.err.println("--- startPosition: x:" + startPosition.getX() + " y: " + startPosition.getY());
    System.err.println("--- targetPosition: x:" + targetPosition.getX() + " y: " + targetPosition.getY());
    InputHandler handler = new InputHandler();
    Graph graph = handler.readBoard(board);
    graph.setStartPosition(new Point(startPosition.getX(), startPosition.getY()));
    graph.setTargetPosition(new Point(targetPosition.getX(), targetPosition.getY()));

    List<Node> path = graph.executeAStar();

    if (path == null) {
      System.err.println("There is no path to target");
    } else {
      System.err.println("The total number of moves from distance to the target are : " + path.size());
      System.err.println("--- Path to target ---");
      graph.printPath(path);
    }
  }
}
