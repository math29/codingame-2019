package com.player.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

class CellTest {

    private Cell cell;

    private Scanner scanner;

    @Mock
    private Board board;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void hasOre() {
        this.cell = new Cell(true, 3, false, true);
        Assertions.assertTrue(cell.hasOre());

        this.cell = new Cell(true, 0, false, true);
        Assertions.assertFalse(cell.hasOre());
    }

    @Test
    void isKnown() {
        this.cell = new Cell(true, 3, true, true);
        Assertions.assertTrue(cell.isKnown());

        this.cell = new Cell(false, 0, true, true);
        Assertions.assertFalse(cell.isKnown());
    }

    @Test
    void getOre() {
        this.cell = new Cell(true, 3, true, true);
        Assertions.assertEquals(3, cell.getOre());

        this.cell = new Cell(false, 0, true, true);
        Assertions.assertEquals(0, cell.getOre());
    }

    @Test
    void isHole() {
        this.cell = new Cell(true, 3, true, true);
        Assertions.assertTrue(cell.isHole());

        this.cell = new Cell(true, 0, false, true);
        Assertions.assertFalse(cell.isHole());
    }

    @Test
    void isSafe() {
        this.cell = new Cell(true, 3, true, true);
        Assertions.assertTrue(cell.isSafe());

        this.cell = new Cell(true, 0, false, false);
        Assertions.assertFalse(cell.isSafe());
    }

    @Test
    void getCoordUnkownCell() {
        Coord coord = new Coord(1, 0);
        scanner = new Scanner("?\n0");

        this.cell = new Cell(scanner, coord);

        Assertions.assertEquals(coord, cell.getCoord());
        Assertions.assertFalse(cell.isKnown());
        Assertions.assertFalse(cell.hasOre());
    }

    @Test
    void getCoordKnowCellNoHole() {
        Coord coord = new Coord(1, 0);
        scanner = new Scanner("3\n0");

        this.cell = new Cell(scanner, coord);

        Assertions.assertEquals(coord, cell.getCoord());
        Assertions.assertTrue(cell.isKnown());
        Assertions.assertTrue(cell.hasOre());
        Assertions.assertEquals(3, cell.getOre());
        Assertions.assertFalse(cell.isHole());
    }

    @Test
    void getCoordKnowCellWithHole() {
        Coord coord = new Coord(1, 0);
        scanner = new Scanner("3\n1");

        this.cell = new Cell(scanner, coord);

        Assertions.assertEquals(coord, cell.getCoord());
        Assertions.assertTrue(cell.isKnown());
        Assertions.assertTrue(cell.hasOre());
        Assertions.assertEquals(3, cell.getOre());
        Assertions.assertTrue(cell.isHole());
    }

    @Test
    void shouldNotHaveAllyTrap() {
        Collection<Coord> myTrapPoos = new ArrayList<>();
        myTrapPoos.add(new Coord(0, 1));
        myTrapPoos.add(new Coord(0, 0));
        myTrapPoos.add(new Coord(1, 1));
        Mockito.when(board.getMyTrapPos()).thenReturn(myTrapPoos);

        Coord coord = new Coord(1, 0);
        scanner = new Scanner("3\n1");
        this.cell = new Cell(scanner, coord);
        Assertions.assertFalse(this.cell.hasAllyTrap(board));
    }

    @Test
    void shouldHaveAllyTrap() {
        Collection<Coord> myTrapPoos = new ArrayList<>();
        myTrapPoos.add(new Coord(0, 1));
        myTrapPoos.add(new Coord(0, 0));
        myTrapPoos.add(new Coord(1, 0));
        Mockito.when(board.getMyTrapPos()).thenReturn(myTrapPoos);

        Coord coord = new Coord(1, 0);
        scanner = new Scanner("3\n1");
        this.cell = new Cell(scanner, coord);
        Assertions.assertTrue(this.cell.hasAllyTrap(board));
    }

    @Test
    void isRadarFree() {
        Collection<Coord> myRadarPoos = new ArrayList<>();
        myRadarPoos.add(new Coord(0, 1));
        myRadarPoos.add(new Coord(0, 0));
        myRadarPoos.add(new Coord(1, 1));
        Mockito.when(board.getMyRadarPos()).thenReturn(myRadarPoos);
        Coord coord = new Coord(1, 0);
        scanner = new Scanner("3\n1");
        this.cell = new Cell(scanner, coord);
        Assertions.assertTrue(this.cell.isRadarFree(board));
    }

    @Test
    void isNotRadarFree() {
        Collection<Coord> myRadarPoos = new ArrayList<>();
        myRadarPoos.add(new Coord(0, 1));
        myRadarPoos.add(new Coord(0, 0));
        myRadarPoos.add(new Coord(1, 0));
        Mockito.when(board.getMyRadarPos()).thenReturn(myRadarPoos);
        Coord coord = new Coord(1, 0);
        scanner = new Scanner("3\n1");
        this.cell = new Cell(scanner, coord);
        Assertions.assertFalse(this.cell.isRadarFree(board));
    }
}