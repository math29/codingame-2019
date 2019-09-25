package com.montecarlo.sample.tictactoe;

import com.montecarlo.sample.SampleRunner;

/**
 * Run a game between two TicTacToeIA opponent...
 * 
 * @author antoine vianey
 */
public class TicTacToeRunner extends SampleRunner<TicTacToeTransition> {

    public TicTacToeRunner() {
        // Change the thinking depth value > 0
        super(new TicTacToeIA());
    }
    
    public static void main(String[] args) {
        SampleRunner<TicTacToeTransition> runner = new TicTacToeRunner();
        runner.run();
    }
    
}
