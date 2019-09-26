package com.player;

import java.util.HashMap;
import java.util.Map;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

abstract class BehaviourOrchestrator {

  Map<Integer, EntityBehaviour> behaviourMap;

  protected Board board;

  BehaviourOrchestrator(final Board board) {
    this.board = board;
    behaviourMap = new HashMap<>();
  }

  abstract void setRobotBehaviours();

//  protected int getNumberOfMiners() {
//    this.behaviourMap.values().stream().filter(entityBehaviour -> instanceof)
//  }

//  protected int getNumberOfScouts() {

//  }
}