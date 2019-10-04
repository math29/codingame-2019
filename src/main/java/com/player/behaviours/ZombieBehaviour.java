package com.player.behaviours;

import com.player.model.Action;
import com.player.model.Board;
import com.player.model.Entity;

public class ZombieBehaviour extends EntityBehaviour {

  public ZombieBehaviour(final Entity entity, final Board board) {
    super(entity, board);
    this.NAME = "Zombie";
  }

  @Override public Action getNextAction() {
    return returnAction(Action.none());
  }
}
