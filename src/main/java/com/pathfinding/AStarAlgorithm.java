package com.pathfinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class AStarAlgorithm {

  static void main(String[] args)
      throws InvalidLetterException, IOException {

    if (args.length != 1) {
      System.out.println("Usage: java A_StarAlgorithm <filename>");
    } else {
      String filename = args[0];

      InputHandler handler = new InputHandler();
      SquareGraph graph = handler.readMap(filename);

      ArrayList<Node> path = graph.executeAStar();

      if (path == null) {
        System.out.println("There is no path to target");
      } else {
        System.out.println("The total number of moves from distance to the target are : " + path.size());
        System.out.println("You want to see the whole path to the target ? (y/n) ");
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine();
        if (response.equals("y")) {
          System.out.println("--- Path to target ---");
          graph.printPath(path);
        }
      }
    }
  }

}

