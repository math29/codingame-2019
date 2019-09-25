package com.montecarlo.sample.tictactoe;

import com.montecarlo.Transition;

/**
 * A basic move implementation : who and where...
 *
 * @author antoine vianey
 */
public class TicTacToeTransition implements Transition {

  /**
   * The player owning the move
   */
  private int player;

  /**
   * x coordinate of the move
   */
  private int x;
  /**
   * y coordinate of the move
   */
  private int y;

  public TicTacToeTransition(int x, int y, int player) {
    this.x = x;
    this.y = y;
    this.player = player;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getPlayer() {
    return player;
  }

  public void setPlayer(int player) {
    this.player = player;
  }

  @Override
  public int hashCode() {
    return (player >> 6) | (x >> 3) | y;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof TicTacToeTransition &&
        ((TicTacToeTransition)o).player == player &&
        ((TicTacToeTransition)o).x == x &&
        ((TicTacToeTransition)o).y == y;
  }

  public String toString() {
    return (player == TicTacToeIA.PLAYER_O ? "O" : "X") + " (" + x + ";" + y + ")";
  }

}
