package com.player.pathfinding;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph {

  private Node[][] map;
  private Point startPosition;
  private Point targetPosition;
  private Heap<Node> openNodes;
  private Set<Node> closedNodes;

  Graph(int width, int height) {
    map = new Node[width][height];
    startPosition = new Point();
    targetPosition = new Point();
    openNodes = new Heap<>();
    closedNodes = new HashSet<>();
  }

  Node getMapCell(Point coord) {
    return map[(int)coord.getX()][(int)coord.getY()];
  }

  void setMapCell(Point coord, Node n) {
    map[(int)coord.getX()][(int)coord.getY()] = n;
  }

  Point getStartPosition() {
    return startPosition;
  }

  Point getTargetPosition() {
    return targetPosition;
  }

  void setStartPosition(Point coord) {
    startPosition.setLocation(coord);
  }

  void setTargetPosition(Point coord) {
    targetPosition.setLocation(coord);
  }

  int getWidth() {
    return map.length;
  }

  int getHeight() {
    return map[0].length;
  }

  void addToOpenNodes(Node n) {
    n.setOpen();
    openNodes.add(n);
  }

  Node popBestOpenNode() {
    return openNodes.remove();
  }

  void addToClosedNodes(Node n) {
    n.setClosed();
    closedNodes.add(n);
  }

  boolean isInsideMap(Point p) {
    return ((p.getX() >= 0) && (p.getX() < getWidth()) && (p.getY() >= 0) && (p.getY() < getHeight()));
  }

  Set<Node> getNeighbours(Node n) {
    Set<Node> neighbours = new HashSet<>();
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        if (!(i == 0 && j == 0))
          if (isInsideMap(new Point(n.getX() + i, n.getY() + j))) {
            Node temp = getMapCell(new Point(n.getX() + i, n.getY() + j));
            if (!temp.isObstacle())
              neighbours.add(temp);
          }

      }
    }
    return neighbours;
  }

  static double calculateDistance(Point from, Point to) {
    return Math.pow(Math.pow(from.getX() - to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2), 0.5);
  }

  ArrayList<Node> reconstructPath(Node target) {
    ArrayList<Node> path = new ArrayList<>();
    Node current = target;

    while (current.getParent() != null) {
      path.add(current.getParent());
      current = current.getParent();
    }
    Collections.reverse(path);
    return path;
  }

  void printPath(List<Node> path) {
    for (int i = 0; i < path.size(); i++) {
      Node node = path.get(i);
      System.err.println("node : (" + node.getX() + "," + node.getY() + ")");
    }
  }

  ArrayList<Node> executeAStar() {
    Node start = getMapCell(getStartPosition());
    Node target = getMapCell(getTargetPosition());
    addToOpenNodes(start);

    start.setCostFromStart(0);
    start.setTotalCost(start.getCostFromStart() + calculateDistance(start.getPosition(), target.getPosition()));
    while (!openNodes.isEmpty()) {
      Node current = popBestOpenNode();
      if (current.equals(target)) {
        return reconstructPath(target);
      }

      addToClosedNodes(current);
      Set<Node> neighbours = getNeighbours(current);
      for (Node neighbour : neighbours) {
        if (!neighbour.isClosed()) {
          double tentativeCost =
              current.getCostFromStart() + calculateDistance(current.getPosition(), neighbour.getPosition());

          if ((!neighbour.isOpen()) || (tentativeCost < neighbour.getCostFromStart())) {
            neighbour.setParent(current);
            neighbour.setCostFromStart(tentativeCost);
            neighbour.setTotalCost(
                neighbour.getCostFromStart() + calculateDistance(neighbour.getPosition(), start.getPosition()));
            if (!neighbour.isOpen())
              addToOpenNodes(neighbour);
          }
        }

      }
    }

    return null;
  }
}