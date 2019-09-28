package com.player.behaviours;

import com.player.model.Action;
import com.player.model.Board;
import com.player.model.Entity;

public class LazyBehaviour extends EntityBehaviour {

  public LazyBehaviour(final Entity entity, final Board board) {
    super(entity, board);
    this.NAME = "Lazy";
  }

  @Override public Action getNextAction() {
    return returnAction(Action.none());
  }
}
