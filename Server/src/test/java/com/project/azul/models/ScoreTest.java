package com.project.azul.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class ScoreTest {
    private Game game;

    @BeforeEach
    void setUp(){
        this.game = new Game(UUID.randomUUID(), 4);

        game.registerPlayer("Dave");
        game.registerPlayer("Mike");
        game.registerPlayer("Lana");
        game.registerPlayer("Angelina");
    }

    @Test
    @DisplayName("Calculate column score")
    void calculateColumn(){
        var player = game.getPlayers().get(0);
        player.addTilesToLine(new ArrayList<>(Arrays.asList(new Tile(TileColor.BLUE))), 0);
        player.addTilesToLine(new ArrayList<>(Collections.nCopies(2,new Tile(TileColor.WHITE))), 1);
        player.addTilesToLine(new ArrayList<>(Collections.nCopies(3,new Tile(TileColor.BLACK))), 2);
        player.addTilesToLine(new ArrayList<>(Collections.nCopies(4,new Tile(TileColor.RED))), 3);
        player.addTilesToLine(new ArrayList<>(Collections.nCopies(5,new Tile(TileColor.YELLOW))), 4);
        player.wallTiling();
        assertEquals(15, player.getScore());

        player.calculateBonus();
        assertEquals(22, player.getScore());
    }

    @Test
    @DisplayName("Calculate row score")
    void calculateRow(){
        var player = game.getPlayers().get(0);

        player.addTilesToLine(new ArrayList<>(Arrays.asList(new Tile(TileColor.BLUE))), 0);
        player.wallTiling();

        player.addTilesToLine(new ArrayList<>(Arrays.asList(new Tile(TileColor.YELLOW))), 0);
        player.wallTiling();

        player.addTilesToLine(new ArrayList<>(Arrays.asList(new Tile(TileColor.RED))), 0);
        player.wallTiling();

        player.addTilesToLine(new ArrayList<>(Arrays.asList(new Tile(TileColor.BLACK))), 0);
        player.wallTiling();

        player.addTilesToLine(new ArrayList<>(Arrays.asList(new Tile(TileColor.WHITE))), 0);
        player.wallTiling();

        assertEquals(15, player.getScore());

        player.calculateBonus();
        assertEquals(17, player.getScore());
    }


    @Test
    @DisplayName("Calculate cross score")
    void calculateCross(){
        var player = game.getPlayers().get(0);
        player.addTilesToLine(new ArrayList<>(Collections.nCopies(1,new Tile(TileColor.RED))), 0);
        player.addTilesToLine(new ArrayList<>(Collections.nCopies(2,new Tile(TileColor.YELLOW))), 1);
        player.addTilesToLine(new ArrayList<>(Collections.nCopies(4,new Tile(TileColor.WHITE))), 3);
        player.addTilesToLine(new ArrayList<>(Collections.nCopies(5,new Tile(TileColor.BLACK))), 4);
        player.wallTiling();

        assertEquals(6, player.getScore());

        player.addTilesToLine(new ArrayList<>(Collections.nCopies(3,new Tile(TileColor.BLACK))), 2);
        player.wallTiling();
        assertEquals(7, player.getScore());

        player.addTilesToLine(new ArrayList<>(Collections.nCopies(3,new Tile(TileColor.WHITE))), 2);
        player.wallTiling();
        assertEquals(9, player.getScore());

        player.addTilesToLine(new ArrayList<>(Collections.nCopies(3,new Tile(TileColor.BLUE))), 2);
        player.wallTiling();
        assertEquals(17, player.getScore());

        player.addTilesToLine(new ArrayList<>(Collections.nCopies(3,new Tile(TileColor.YELLOW))), 2);
        player.wallTiling();
        assertEquals(21, player.getScore());

        player.addTilesToLine(new ArrayList<>(Collections.nCopies(3,new Tile(TileColor.RED))), 2);
        player.wallTiling();
        assertEquals(26, player.getScore());

        player.calculateBonus();
        assertEquals(35, player.getScore());
    }

    @Test
    @DisplayName("Calculate all color score")
    void calculateAllColor(){
        var player = game.getCurrentPlayer();

        player.addTilesToLine(new ArrayList<>(Arrays.asList(new Tile(TileColor.BLUE))), 0);
        player.addTilesToLine(new ArrayList<>(Collections.nCopies(2,new Tile(TileColor.BLUE))), 1);
        player.addTilesToLine(new ArrayList<>(Collections.nCopies(3,new Tile(TileColor.BLUE))), 2);
        player.addTilesToLine(new ArrayList<>(Collections.nCopies(4,new Tile(TileColor.BLUE))), 3);
        player.addTilesToLine(new ArrayList<>(Collections.nCopies(5,new Tile(TileColor.BLUE))), 4);
        player.wallTiling();

        assertEquals(5, player.getScore());

        player.calculateBonus();

        assertEquals(15, player.getScore());
    }
}
