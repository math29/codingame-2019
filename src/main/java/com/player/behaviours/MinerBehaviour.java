package com.player.behaviours;

import java.util.Optional;

import com.player.model.Action;
import com.player.model.Board;
import com.player.model.Cell;
import com.player.model.Coord;
import com.player.model.Entity;
import com.player.model.EntityType;

public class MinerBehaviour extends EntityBehaviour {

  public MinerBehaviour(final Entity entity, final Board board) {
    super(entity, board);
    this.NAME = "Miner" + entity.getId();
  }

  @Override public Action getNextAction() {
    // Miner has cristal on him, bring it to headquarters
    if (entity.getItem() == EntityType.AMADEUSIUM) {
      return returnAction(Action.move(this.getCloserHeadQuarterCell().getCoord()));
    }

    // Find a good cell to mine
    Optional<Cell> closestOre = getClosestSafeInterestingOre();
    if (!closestOre.isPresent()) {
      closestOre = this.getClosestOreCell();
    }

    // Found some! Dig if you are close or, otherwise, move
    if (closestOre.isPresent()) {
      if (closestOre.get().getCoord().distance(entity.getPos()) <= 2) {
        return returnAction(Action.dig(closestOre.get().getCoord()));
      } else {
        return returnAction(Action.move(closestOre.get().getCoord()));
      }
    }

    if (entity.isAtHeadquarters()) {
      return returnAction(Action.move(new Coord(entity.getPos().getX() + 3, entity.getPos().getY())));
    }

    // No christal found, go for default mining
    int i = 0, newX = entity.getPos().getX(), newY = entity.getPos().getY();
    Coord fixedCoord = new Coord(newX, newY);
    while (board.getCell(fixedCoord).isHole() || board.getCell(fixedCoord).hasAllyTrap(board)) {
      switch (i) {
        case 0:
          newX = fixedCoord.getX() - 1 > 0 ? fixedCoord.getX() - 1 : fixedCoord.getX();
          newY = fixedCoord.getY() < board.getHeight() ? fixedCoord.getY() : fixedCoord.getY() - 1;
          break;
        case 1:
          newX = fixedCoord.getX() + 1 < board.getWidth() ? fixedCoord.getX() + 1 : fixedCoord.getX();
          newY = fixedCoord.getY() - 1 > 0 ? fixedCoord.getY() - 1 : fixedCoord.getY();
          break;
        case 2:
          newX = fixedCoord.getX() + 1 < board.getWidth() ? fixedCoord.getX() + 1 : fixedCoord.getX();
          newY = fixedCoord.getY() + 1 < board.getHeight() ? fixedCoord.getY() + 1 : fixedCoord.getY();
          break;
        case 3:
          newX = fixedCoord.getX() - 1 < board.getWidth() ? fixedCoord.getX() - 1 : fixedCoord.getX();
          newY = fixedCoord.getY() + 1 < board.getHeight() ? fixedCoord.getY() + 1 : fixedCoord.getY();
          break;
        default:
          newX = fixedCoord.getX() + 3 < board.getWidth() ? fixedCoord.getX() + 3 : fixedCoord.getX();
          newY = fixedCoord.getY() + 1 < board.getHeight() ? fixedCoord.getY() + 1 : fixedCoord.getY();
      }

      fixedCoord = new Coord(newX, newY);
      i++;
    }
    return returnAction(Action.dig(fixedCoord));
  }

  private Optional<Cell> getClosestSafeInterestingOre() {
    Optional<Cell> closerCell = Optional.empty();
    int minDistance = board.getWidth();
    for (final Cell cell : board.getCells()) {
      int distance = cell.getCoord().distance(this.entity.getPos());
      if (cell.hasOre()
              && distance < minDistance
              && !cell.hasAllyTrap(board)
              && !cell.hasPotentialEnemyTrap()) {
        closerCell = Optional.of(cell);
        minDistance = distance;
      }
    }
    return closerCell;
  }
}
