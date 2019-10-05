package com.player.model;

import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class HistoryTest {

  @Mock
  private Board board;

  @BeforeEach
  void beforeEach() {
    MockitoAnnotations.initMocks(this);
    History.reset();
  }

  @Test
  void shouldBeInInitialState() {
    Assertions.assertEquals(0, History.getNumberOfTurns());
    Assertions.assertEquals(new HashMap<Integer, Board>(), History.getTurns());
    Assertions.assertEquals(Optional.empty(), History.getPreviousTurn());
  }

  @Test
  void recordNewTurn() {
    History.recordNewTurn(board);
    Assertions.assertEquals(board, History.getTurns().get(0));
    Assertions.assertEquals(Optional.of(board), History.getPreviousTurn());
  }

  @Test
  void getNumberOfTurns() {
    Assertions.assertEquals(0, History.getNumberOfTurns());
    History.recordNewTurn(board);
    Assertions.assertEquals(1, History.getNumberOfTurns());
    History.recordNewTurn(board);
    Assertions.assertEquals(2, History.getNumberOfTurns());
  }

  @Test
  void getTurns() {
    History.recordNewTurn(null);
    History.recordNewTurn(board);
    Assertions.assertEquals(board, History.getTurns().get(1));
  }

  @Test
  void getPreviousTurn() {
    History.recordNewTurn(board);
    Assertions.assertEquals(Optional.of(board), History.getPreviousTurn());
  }
}