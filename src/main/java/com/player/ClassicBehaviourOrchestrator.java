package com.player;

import java.util.stream.Collectors;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

class ClassicBehaviourOrchestrator extends BehaviourOrchestrator {

  private static final int NUMBER_OF_RADARS_MAX = 6;

  private static final int NUMBER_OF_TRAPS_MAX = 10;

  ClassicBehaviourOrchestrator(final Board board) {
    super(board);
  }

  @Override void setRobotBehaviours() {
    for (final Entity robot : board.myTeam.robots) {
      if (this.behaviourMap.get(robot.id) == null) {
        if (this.getNumberOfScouts() < 1
            && board.myRadarPos.size() < NUMBER_OF_RADARS_MAX
            && board.getCells().stream().noneMatch(cell -> cell.hasOre() && !isCellBad(cell))) {
          this.behaviourMap.put(robot.id, new ScoutBehaviour(robot, board));
        } else if (this.getNumberOfBombers() < 1
            && board.myTrapPos.size() < NUMBER_OF_TRAPS_MAX) {
          this.behaviourMap.put(robot.id, new BomberBehaviour(robot, board));
        } else {
          this.behaviourMap.put(robot.id, new MinerBehaviour(robot, board));
        }
      }
    }
  }

  private boolean isCellBad(final Cell cell) {
    if(board.myTrapPos.isEmpty()){
      return false;
    }
    Coord cellOptional = board.myTrapPos.parallelStream()
        .filter(trapCoord -> trapCoord.x == cell.coord.x && trapCoord.y == cell.coord.y).findFirst()
        .orElse(null);
    return cellOptional != null;
  }
}
