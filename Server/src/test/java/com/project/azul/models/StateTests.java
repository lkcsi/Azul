package com.project.azul.models;

import com.project.azul.dto.PickForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class StateTests {
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
    @DisplayName("Detection of end turn")
    void endTurn() {

        assertEquals(State.GAME_READY, game.getState());

        var factory = game.getFactories().get(0);
        var tile = game.getBag().removeTile(TileColor.BLUE);
        var player = game.getCurrentPlayer();

        game.getCenter().addTile(tile);
        game.getFactories().forEach(f -> game.getDrop().addTiles(f.removeTiles()));

        game.getFactories().forEach(t -> assertEquals(0, t.size()));
        player.addTilesToLine(new ArrayList<>(Collections.nCopies(2, new Tile(TileColor.WHITE))) , 1);
        player.addTilesToLine(new ArrayList<>(Collections.nCopies(3, new Tile(TileColor.BLACK))) , 2);
        player.addTilesToLine(new ArrayList<>(Collections.nCopies(4, new Tile(TileColor.RED))) , 3);
        player.addTilesToLine(new ArrayList<>(Collections.nCopies(5, new Tile(TileColor.YELLOW))) , 4);

        //After this pick next turn should triggered
        var pickForm = new PickForm(player.getName(), 0, tile.getColor().name(), 0, false);
        game.pickFromCenter(pickForm);

   //   Check current player
        assertEquals("Dave" ,game.getCurrentPlayer().getName());

        //Check factory fill
        game.getFactories().forEach(t -> assertEquals(4, t.size()));
        //Check center fill
        assertEquals(1, game.getCenter().size());
        assertEquals(true, game.getCenter().getTiles().get(0).getColor() == TileColor.MARKER);

        //Check scoring happens 15 - 1
        assertEquals(14, player.getScore());
    }
    @Test
    @DisplayName("Detection of end of the game")
    void endGame() {
        var player = game.getCurrentPlayer();

        player.addTilesToLine(getTilesFromBag(TileColor.BLUE, 1),0);
        player.wallTiling();

        player.addTilesToLine(getTilesFromBag(TileColor.YELLOW, 1),0);
        player.wallTiling();

        player.addTilesToLine(getTilesFromBag(TileColor.RED, 1),0);
        player.wallTiling();

        player.addTilesToLine(getTilesFromBag(TileColor.BLACK, 1),0);
        player.wallTiling();

        game.getCenter().addTiles(getTilesFromBag(TileColor.WHITE, 1));
        game.getFactories().forEach(f -> game.getDrop().addTiles(f.removeTiles()));

        var pickForm = new PickForm(player.getName(), 0, TileColor.WHITE.name(), 0, false);
        game.pickFromCenter(pickForm);

        assertEquals(State.GAME_OVER, game.getState());
        assertEquals(16, player.getScore());
    }

    private List<Tile> getTilesFromBag(TileColor color, int count){
        var tiles = game.getBag().getTiles(color).stream().limit(count).collect(Collectors.toList());
        game.getBag().getTiles().removeAll(tiles);
        return tiles;
    }
}

