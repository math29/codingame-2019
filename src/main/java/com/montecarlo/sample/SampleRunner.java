package com.montecarlo.sample;

import java.util.Set;

import com.montecarlo.MonteCarloTreeSearch;
import com.montecarlo.Node;
import com.montecarlo.Transition;

/**
 * An abstract utility class for testing MCTS implementations.
 *
 * @param <T>
 * @author antoine vianey
 */
public abstract class SampleRunner<T extends Transition> {

  public static interface Listener<T extends Transition> {
    public void onMove(MonteCarloTreeSearch<T, ? extends Node<T>> mcts, T transition, int turn);

    public void onGameOver(MonteCarloTreeSearch<T, ? extends Node<T>> mcts);

    public void onNoPossibleMove(MonteCarloTreeSearch<T, ? extends Node<T>> mcts);
  }

  private MonteCarloTreeSearch<T, ? extends Node<T>> mcts;
  private Listener<T> listener;

  public SampleRunner(MonteCarloTreeSearch<T, ? extends Node<T>> mcts) {
    this.mcts = mcts;
  }

  public void setListener(Listener<T> listener) {
    this.listener = listener;
  }

  public void run() {
    T transition;
    int turn = 0;
    while (!mcts.isOver()) {
      Set<T> transitions = mcts.getPossibleTransitions();
      if (!transitions.isEmpty()) {
        transition = mcts.getBestTransition();
        mcts.doTransition(transition);
        if (listener != null) {
          listener.onMove(mcts, transition, ++turn);
        }
      } else {
        if (listener != null) {
          listener.onNoPossibleMove(mcts);
        }
        // no move for the current player
        // up to next player
        mcts.next();
      }
    }
    if (listener != null) {
      listener.onGameOver(mcts);
    }
  }

}
